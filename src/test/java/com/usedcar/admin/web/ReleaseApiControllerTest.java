package com.usedcar.admin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.payment.PaymentRepository;
import com.usedcar.admin.domain.payment.PaymentType;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseRepository;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.web.dto.payment.PaymentRequestDto;
import com.usedcar.admin.web.dto.release.ReleaseSaveRequestDto;
import com.usedcar.admin.web.dto.release.ReleaseUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReleaseApiControllerTest {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void clean() {
        paymentRepository.deleteAll();
        releaseRepository.deleteAll();
        carRepository.deleteAll();

    }

    @Before
    public void init() {
        String carNumber1 = "04구4716";
        String carNumber2 = "05구4716";
        String carNumber3 = "06구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        String staff = "박성민";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);

        carRepository.save(getCar(carNumber1, vin, category, model, color, productionYear, purchaseDate, staff));
        carRepository.save(getCar(carNumber2, vin, category, model, color, productionYear, purchaseDate, staff));
        carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));
    }
    
    @Test
    @Transactional
    public void 차량_출고() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        Long carId = 1L;
        ReleaseStatus status = ReleaseStatus.READY;

        List<PaymentRequestDto> paymentRequestDtos = new ArrayList<>();
        PaymentRequestDto cashRequestDto = PaymentRequestDto.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        paymentRequestDtos.add(cashRequestDto);

        PaymentRequestDto cardRequestDto = PaymentRequestDto.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        paymentRequestDtos.add(cardRequestDto);

        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .carId(carId)
                .status(status)
                .payments(paymentRequestDtos)
                .build();

        // when
        mvc.perform(post("/api/release/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").value(1L));
        
        // then
        List<Release> list = releaseRepository.findAll();
        Release release = list.get(0);
        Car car = carRepository.findById(carId).get();
        List<Payment> payments = release.getPayments();
        assertThat(release.getId()).isGreaterThan(0L);
        assertThat(release.getStaff()).isEqualTo(staff);
        assertThat(release.getSalesStaff()).isEqualTo(salesStaff);
        assertThat(release.getPrice()).isEqualTo(price);
        assertThat(release.getDeposit()).isEqualTo(deposit);
        assertThat(release.getStatus()).isEqualTo(status);
        assertThat(release.getCreatedDate()).isNotNull();
        assertThat(release.getModifiedDate()).isNotNull();
        assertThat(car.getStatus()).isEqualTo(CarStatus.RELEASE);
        assertThat(car.getReleaseDate()).isEqualTo(release.getReleaseDate());
        assertThat(payments.size()).isEqualTo(2);
        assertThat(payments.get(0).getRelease()).isEqualTo(release);
    }
    
    @Test
    public void 차량_출고_필수값_오류() throws Exception {
        // given
        List<PaymentRequestDto> paymentRequestDtos = new ArrayList<>();
        paymentRequestDtos.add(PaymentRequestDto.builder().build());
        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .payments(paymentRequestDtos)
                .build();

        // when
        mvc.perform(post("/api/release/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("필수값 오류"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").exists());
    }
    
    @Test
    public void 차량_출고_존재하지_않는_차량() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        Long carId = 0L;
        ReleaseStatus status = ReleaseStatus.READY;

        List<PaymentRequestDto> paymentRequestDtos = new ArrayList<>();
        PaymentRequestDto cashRequestDto = PaymentRequestDto.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        paymentRequestDtos.add(cashRequestDto);

        PaymentRequestDto cardRequestDto = PaymentRequestDto.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        paymentRequestDtos.add(cardRequestDto);

        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .carId(carId)
                .status(status)
                .payments(paymentRequestDtos)
                .build();

        // when
        mvc.perform(post("/api/release/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    @Transactional
    public void 차량_출고_이미_출고된_차량() throws Exception {
        // given
        Car car = carRepository.findById(1L).get();
        car.release(LocalDateTime.now());

        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        Long carId = 1L;
        ReleaseStatus status = ReleaseStatus.READY;

        List<PaymentRequestDto> paymentRequestDtos = new ArrayList<>();
        PaymentRequestDto cashRequestDto = PaymentRequestDto.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        paymentRequestDtos.add(cashRequestDto);

        PaymentRequestDto cardRequestDto = PaymentRequestDto.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        paymentRequestDtos.add(cardRequestDto);

        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .carId(carId)
                .status(status)
                .payments(paymentRequestDtos)
                .build();

        // when
        mvc.perform(post("/api/release/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    @Transactional
    public void 출고_차량_전체_조회_최신순() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        Car car1 = carRepository.findById(1L).get();
        Car car2 = carRepository.findById(2L).get();
        Car car3 = carRepository.findById(3L).get();

        List<Payment> payments1 = new ArrayList<>();
        Payment cashPayment1 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments1.add(cashPayment1);

        Payment cardPayment1 = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments1.add(cardPayment1);

        List<Payment> payments2 = new ArrayList<>();
        Payment cashPayment2 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments2.add(cashPayment2);

        Payment cardPayment2 = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments2.add(cardPayment2);

        List<Payment> payments3 = new ArrayList<>();
        Payment cashPayment3 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments3.add(cashPayment3);

        Payment cardPayment3 = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments3.add(cardPayment3);

        Release release1 = getRelease(staff, salesStaff, price, deposit, status, car1, payments1);
        Release release2 = getRelease(staff, salesStaff, price, deposit, status, car2, payments2);
        Release release3 = getRelease(staff, salesStaff, price, deposit, status, car3, payments3);
        release1.create(car1);
        release2.create(car2);
        release3.create(car3);
        releaseRepository.save(release1);
        releaseRepository.save(release2);
        releaseRepository.save(release3);
        
        // when
        mvc.perform(get("/api/releaseList")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data[0].id").value(3L));
    }

    @Test
    @Transactional
    public void 출고_차량_1개_조회() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        Car car = carRepository.findById(1L).get();

        List<Payment> payments = new ArrayList<>();
        Payment cashPayment1 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments.add(cashPayment1);

        Payment cardPayment = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments.add(cardPayment);

        Release release = releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car, payments));
        release.create(car);

        // when
        mvc.perform(get("/api/release/" + release.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    public void 출고_차량_1개_조회_존재하지_않는_출고번호() throws Exception {
        // given

        // when
        mvc.perform(get("/api/release/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());

        // then
    }

    @Test
    @Transactional
    public void 출고_취소() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        Car car = carRepository.findById(1L).get();

        List<Payment> payments = new ArrayList<>();
        Payment cashPayment1 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments.add(cashPayment1);

        Payment cardPayment = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments.add(cardPayment);

        Release release = releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car, payments));
        release.create(car);

        ReleaseUpdateRequestDto requestDto = ReleaseUpdateRequestDto.builder()
                .status(ReleaseStatus.CANCEL)
                .build();

        // when
        mvc.perform(put("/api/release/" + release.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").value(1L));

        // then
        List<Release> list = releaseRepository.findAll();
        Release result = list.get(0);
        assertThat(result.getStatus()).isEqualTo(ReleaseStatus.CANCEL);
        assertThat(result.getCar().getStatus()).isEqualTo(CarStatus.NORMAL);
    }

    @Test
    @Transactional
    public void 출고_완료_처리() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        Car car = carRepository.findById(1L).get();

        List<Payment> payments = new ArrayList<>();
        Payment cashPayment1 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments.add(cashPayment1);

        Payment cardPayment = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments.add(cardPayment);

        Release release = releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car, payments));
        release.create(car);

        ReleaseUpdateRequestDto requestDto = ReleaseUpdateRequestDto.builder()
                .status(ReleaseStatus.COMPLETE)
                .build();

        // when
        mvc.perform(put("/api/release/" + release.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").value(1L));

        // then
        List<Release> list = releaseRepository.findAll();
        Release result = list.get(0);
        assertThat(result.getStatus()).isEqualTo(ReleaseStatus.COMPLETE);
        assertThat(result.getCar().getStatus()).isEqualTo(CarStatus.RELEASE);
    }

    @Test
    public void 출고_상태_변경_필수값_오류() throws Exception {
        // given
        ReleaseUpdateRequestDto requestDto = ReleaseUpdateRequestDto.builder().build();

        // when
        mvc.perform(put("/api/release/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void 출고_상태_변경_존재하지_않는_출고번호() throws Exception {
        // given
        ReleaseUpdateRequestDto requestDto = ReleaseUpdateRequestDto.builder()
                .status(ReleaseStatus.CANCEL)
                .build();

        // when
        mvc.perform(put("/api/release/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Transactional
    public void 출고_상태_변경_이미_취소된_상태() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        Car car = carRepository.findById(1L).get();

        List<Payment> payments = new ArrayList<>();
        Payment cashPayment1 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(deposit)
                .instalment(0)
                .build();
        payments.add(cashPayment1);

        Payment cardPayment = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - deposit)
                .instalment(36)
                .capital("현대")
                .build();
        payments.add(cardPayment);

        Release release = releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car, payments));
        release.create(car);
        release.cancel();

        ReleaseUpdateRequestDto requestDto = ReleaseUpdateRequestDto.builder()
                .status(ReleaseStatus.CANCEL)
                .build();

        // when
        mvc.perform(put("/api/release/" + release.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private Release getRelease(String staff, String salesStaff, int price, int deposit, ReleaseStatus status, Car car, List<Payment> payments) {
        return Release.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .status(status)
                .car(car)
                .payments(payments)
                .build();
    }

    private Car getCar(String carNumber, String vin, Category category, String model, String color, String productionYear, LocalDateTime purchaseDate, String staff) {
        return Car.builder()
                .carNumber(carNumber)
                .vin(vin)
                .category(category)
                .model(model)
                .color(color)
                .productionYear(productionYear)
                .purchaseDate(purchaseDate)
                .staff(staff)
                .build();
    }

}