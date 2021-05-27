package com.usedcar.admin.domain.release;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.payment.PaymentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReleaseRepositoryTest {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private CarRepository carRepository;

    @Before
    public void clean() {
        releaseRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 차량_출고() throws Exception {
        // given
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
        Car car = carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));

        staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        ReleaseStatus status = ReleaseStatus.READY;
        car.release(LocalDateTime.now());

        int cashAmount = 15000000;
        List<Payment> payments = new ArrayList<>();
        Payment cashPayment = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(cashAmount)
                .instalment(0)
                .build();
        payments.add(cashPayment);

        Payment cardPayment = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - cashAmount)
                .instalment(36)
                .capital("현대")
                .build();
        payments.add(cardPayment);

        // when
        releaseRepository.save(getRelease(staff, salesStaff, price, status, car, payments));
        
        // then
        List<Release> list = releaseRepository.findAll();
        Release release = list.get(0);
        assertThat(release.getId()).isGreaterThan(0L);
        assertThat(release.getStaff()).isEqualTo(staff);
        assertThat(release.getSalesStaff()).isEqualTo(salesStaff);
        assertThat(release.getPrice()).isEqualTo(price);
        assertThat(release.getStatus()).isEqualTo(status);
        assertThat(release.getReleaseDate()).isNotNull();
        assertThat(release.getCar()).isEqualTo(car);
    }

    @Test
    @Transactional
    public void 출고_차량_전체_조회_최신순() throws Exception {
        // given
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

        Car car1 = carRepository.save(getCar(carNumber1, vin, category, model, color, productionYear, purchaseDate, staff));
        Car car2 = carRepository.save(getCar(carNumber2, vin, category, model, color, productionYear, purchaseDate, staff));
        Car car3 = carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));

        staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int cashAmount = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        List<Payment> payments1 = new ArrayList<>();
        Payment cashPayment1 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(cashAmount)
                .instalment(0)
                .build();
        payments1.add(cashPayment1);

        Payment cardPayment1 = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - cashAmount)
                .instalment(36)
                .capital("현대")
                .build();
        payments1.add(cardPayment1);

        List<Payment> payments2 = new ArrayList<>();
        Payment cashPayment2 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(cashAmount)
                .instalment(0)
                .build();
        payments2.add(cashPayment2);

        Payment cardPayment2 = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - cashAmount)
                .instalment(36)
                .capital("현대")
                .build();
        payments2.add(cardPayment2);

        List<Payment> payments3 = new ArrayList<>();
        Payment cashPayment3 = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(cashAmount)
                .instalment(0)
                .build();
        payments3.add(cashPayment3);

        Payment cardPayment3 = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - cashAmount)
                .instalment(36)
                .capital("현대")
                .build();
        payments3.add(cardPayment3);

        Release release1 = getRelease(staff, salesStaff, price, status, car1, payments1);
        Release release2 = getRelease(staff, salesStaff, price, status, car2, payments2);
        Release release3 = getRelease(staff, salesStaff, price, status, car3, payments3);
        release1.create(car1);
        release2.create(car2);
        release3.create(car3);
        releaseRepository.save(release1);
        releaseRepository.save(release2);
        Release savedRelease = releaseRepository.save(release3);

        // when
        List<Release> list = releaseRepository.findAllDesc();
        
        // then
        Release release = list.get(0);
        assertThat(release.getId()).isEqualTo(savedRelease.getId());
    }

    @Test
    @Transactional
    public void 차량_1개_조회() throws Exception {
        // given
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
        Car car = carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));

        staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int cashAmount = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        car.release(LocalDateTime.now());

        List<Payment> payments = new ArrayList<>();
        Payment cashPayment = Payment.builder()
                .paymentType(PaymentType.CASH)
                .pay_amount(cashAmount)
                .instalment(0)
                .build();
        payments.add(cashPayment);

        Payment cardPayment = Payment.builder()
                .paymentType(PaymentType.CARD)
                .pay_amount(price - cashAmount)
                .instalment(36)
                .capital("현대")
                .build();
        payments.add(cardPayment);

        Release release1 = releaseRepository.save(getRelease(staff, salesStaff, price, status, car, payments));

        // when
        Release release2 = releaseRepository.findById(release1.getId()).get();

        // then
        assertThat(release2.getId()).isEqualTo(release1.getId());
    }

    private Release getRelease(String staff, String salesStaff, int price, ReleaseStatus status, Car car, List<Payment> payments) {
        return Release.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
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