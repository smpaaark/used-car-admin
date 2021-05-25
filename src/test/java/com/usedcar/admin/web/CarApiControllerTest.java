package com.usedcar.admin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.web.dto.car.CarUpdateRequestDto;
import com.usedcar.admin.web.dto.car.CarSaveRequestDto;
import org.junit.After;
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
public class CarApiControllerTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void clean() throws Exception {
        carRepository.deleteAll();
    }

    @Test
    public void 차량_매입하기() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        String staff = "박성민";
        CarSaveRequestDto requestDto = CarSaveRequestDto.builder()
                .carNumber(carNumber)
                .vin(vin)
                .category(category)
                .model(model)
                .color(color)
                .productionYear(productionYear)
                .staff(staff)
                .build();

        // when
        mvc.perform(post("/api/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").exists());

        // then
        List<Car> list = carRepository.findAll();
        Car car = list.get(0);
        assertThat(car.getId()).isGreaterThan(0L);
        assertThat(car.getCarNumber()).isEqualTo(carNumber);
        assertThat(car.getVin()).isEqualTo(vin);
        assertThat(car.getCategory()).isEqualTo(category);
        assertThat(car.getModel()).isEqualTo(model);
        assertThat(car.getColor()).isEqualTo(color);
        assertThat(car.getProductionYear()).isEqualTo(productionYear);
        assertThat(car.getPurchaseDate()).isNotNull();
        assertThat(car.getStatus()).isEqualTo(CarStatus.NORMAL);
        assertThat(car.getStaff()).isEqualTo(staff);
        assertThat(car.getReleaseDate()).isNull();
    }
    
    @Test
    public void 차량_매입_필수값_오류() throws Exception {
        // given
        CarSaveRequestDto requestDto = CarSaveRequestDto.builder().build();

        // when
        mvc.perform(post("/api/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("필수값 오류"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data[0].objectName").exists())
                .andExpect(jsonPath("$.data[0].field").exists())
                .andExpect(jsonPath("$.data[0].defaultMessage").exists());
    }

    @Test
    public void 차량_매입_중복된_차량번호() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        String staff = "박성민";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff));

        CarSaveRequestDto requestDto = CarSaveRequestDto.builder()
                .carNumber(carNumber)
                .vin(vin)
                .category(category)
                .model(model)
                .color(color)
                .productionYear(productionYear)
                .staff(staff)
                .build();

        mvc.perform(post("/api/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists());
    }
    
    @Test
    public void 차량_전체_조회_최신순() throws Exception {
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
        carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));

        // when
        mvc.perform(get("/api/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data[0].id").isNotEmpty());
    }

    @Test
    public void 차량_전체_조회_0개() throws Exception {
        // given

        // when
        mvc.perform(get("/api/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void 차량_1개_조회() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        String staff = "박성민";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);

        Long carId = carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff)).getId();

        // when
        mvc.perform(get("/api/car/" + carId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    public void 차량_1개_조회_존재하지_않는_차량() throws Exception {
        // given

        // when
        mvc.perform(get("/api/car/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void 차량_정보_수정() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        String staff = "박성민";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);

        Long carId = carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff)).getId();

        Category category2 = Category.FOREIGN;
        String model2 = "G70";
        String color2 = "흰색";
        String productionYear2 = "2021";
        CarUpdateRequestDto requestDto = CarUpdateRequestDto.builder()
                .category(category2)
                .model(model2)
                .color(color2)
                .productionYear(productionYear2)
                .build();

        // when
        mvc.perform(put("/api/car/" + carId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").value(carId));

        // then
        List<Car> list = carRepository.findAll();
        assertThat(list.get(0).getCarNumber()).isEqualTo(carNumber);
        assertThat(list.get(0).getVin()).isEqualTo(vin);
        assertThat(list.get(0).getStaff()).isEqualTo(staff);
        assertThat(list.get(0).getCategory()).isEqualTo(category2);
        assertThat(list.get(0).getModel()).isEqualTo(model2);
        assertThat(list.get(0).getColor()).isEqualTo(color2);
        assertThat(list.get(0).getProductionYear()).isEqualTo(productionYear2);
    }
    
    @Test
    public void 차량_정보_수정_존재하지_않는_차량() throws Exception {
        // given
        CarUpdateRequestDto requestDto = CarUpdateRequestDto.builder().build();

        // when
        mvc.perform(put("/api/car/0")
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
    public void 차량_삭제() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        String staff = "박성민";

        Long carId = carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff)).getId();

        // when
        mvc.perform(delete("/api/car/" + carId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data.id").value(carId));
        
        // then
        List<Car> list = carRepository.findAll();
        Car car = list.get(0);
        assertThat(car.getStatus()).isEqualTo(CarStatus.DELETE);
    }

    @Test
    public void 차량_삭제_존재하지_않는_차량() throws Exception {
        // given

        // when
        mvc.perform(delete("/api/car/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    @Transactional
    public void 차량_삭제_이미_출고중인_차량() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        String staff = "박성민";

        Car car = carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff));
        car.release(LocalDateTime.now());

        // when
        mvc.perform(delete("/api/car/" + car.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.responseDate").exists())
                .andExpect(jsonPath("$.data").isEmpty());
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