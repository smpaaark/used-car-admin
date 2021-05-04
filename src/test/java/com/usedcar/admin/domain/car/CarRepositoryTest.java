package com.usedcar.admin.domain.car;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;

    @After
    public void cleanUp() {
        carRepository.deleteAll();
    }

    @Test
    public void 차량매입_불러오기() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.now();

        carRepository.save(Car.builder()
                .carNumber(carNumber)
                .vin(vin)
                .category(category)
                .model(model)
                .color(color)
                .productionYear(productionYear)
                .purchaseDate(purchaseDate)
                .build());

        // when
        List<Car> carList = carRepository.findAll();

        // then
        Car car = carList.get(0);
        assertThat(car.getCarNumber()).isEqualTo(carNumber);
        assertThat(car.getVin()).isEqualTo(vin);
        assertThat(car.getCategory()).isEqualTo(category);
        assertThat(car.getModel()).isEqualTo(model);
        assertThat(car.getColor()).isEqualTo(color);
        assertThat(car.getProductionYear()).isEqualTo(productionYear);
        assertThat(car.getPurchaseDate()).isAfter(purchaseDate);
    }

}