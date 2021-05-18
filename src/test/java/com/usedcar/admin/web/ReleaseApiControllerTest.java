package com.usedcar.admin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseRepository;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.web.dto.release.ReleaseSaveRequestDto;
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
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void clean() {
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
    public void 차량_출고() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        Long carId = 1L;
        ReleaseStatus status = ReleaseStatus.READY;
        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .carId(carId)
                .status(status)
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
    }
    
    @Test
    public void 차량_출고_필수값_오류() throws Exception {
        // given
        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder().build();
        
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
        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .carId(carId)
                .status(status)
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
        ReleaseSaveRequestDto requestDto = ReleaseSaveRequestDto.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .carId(carId)
                .status(status)
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

        car1.release(LocalDateTime.now());
        car2.release(LocalDateTime.now());
        car3.release(LocalDateTime.now());

        releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car1));
        releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car2));
        releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car3));
        
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
        car.release(LocalDateTime.now());

        Release release = releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car));

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

    private Release getRelease(String staff, String salesStaff, int price, int deposit, ReleaseStatus status, Car car) {
        return Release.builder()
                .staff(staff)
                .salesStaff(salesStaff)
                .price(price)
                .deposit(deposit)
                .status(status)
                .car(car)
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