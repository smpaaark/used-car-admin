package com.usedcar.admin.domain.car;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;

    @After
    public void clean() throws Exception {
        carRepository.deleteAll();
    }

    @Test
    public void 차량_갯수_체크() throws Exception {
        // given
        String carNumber1 = "04구4716";
        String carNumber2 = "05구4716";
        String carNumber3 = "06구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        String staff = "박성민";

        carRepository.save(getCar(carNumber1, vin, category, model, color, productionYear, purchaseDate, staff));
        carRepository.save(getCar(carNumber2, vin, category, model, color, productionYear, purchaseDate, staff));
        carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));

        // when
        int count = carRepository.countByCarNumber(carNumber1);

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void 차량_전체_조회하기() throws Exception {
        // given
        String carNumber1 = "04구4716";
        String carNumber2 = "05구4716";
        String carNumber3 = "06구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        String staff = "박성민";

        carRepository.save(getCar(carNumber1, vin, category, model, color, productionYear, purchaseDate, staff));
        carRepository.save(getCar(carNumber2, vin, category, model, color, productionYear, purchaseDate, staff));
        carRepository.save(getCar(carNumber3, vin, category, model, color, productionYear, purchaseDate, staff));

        // when
        List<Car> carList = carRepository.findAllDesc();

        // then
        assertThat(carList.size()).isEqualTo(3);
        Car car = carList.get(0);
        assertThat(car.getId()).isGreaterThan(carList.get(1).getId());
        assertThat(car.getCarNumber()).isEqualTo(carNumber3);
        assertThat(car.getVin()).isEqualTo(vin);
        assertThat(car.getCategory()).isEqualTo(category);
        assertThat(car.getModel()).isEqualTo(model);
        assertThat(car.getColor()).isEqualTo(color);
        assertThat(car.getProductionYear()).isEqualTo(productionYear);
        assertThat(car.getPurchaseDate()).isNotNull();
        assertThat(car.getStatus()).isNotEqualTo(CarStatus.DELETE);
        assertThat(car.getCreatedDate()).isAfter(purchaseDate);
        assertThat(car.getModifiedDate()).isAfter(purchaseDate);
        assertThat(car.getStaff()).isEqualTo(staff);
    }

    @Test
    public void 차량_상태_조회() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        String staff = "박성민";

        Long id = carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff)).getId();

        // when
        Optional<Car> optionalCar = carRepository.findCarStatus(id);

        // then
        Car car = optionalCar.get();
        assertThat(car.getStatus()).isEqualTo(CarStatus.NORMAL);
    }
    
    @Test
    public void QueryDsl_테스트() throws Exception {
        // given
        String carNumber = "04구4716";
        String vin = "12345678";
        Category category = Category.DOMESTIC;
        String model = "더 뉴 K5";
        String color = "검정";
        String productionYear = "2018";
        LocalDateTime purchaseDate = LocalDateTime.of(2021, 05, 12, 0, 0);
        String staff = "박성민";

        Long id = carRepository.save(getCar(carNumber, vin, category, model, color, productionYear, purchaseDate, staff)).getId();

        // when
        List<Car> cars = carRepository.findByCarNumber(carNumber);

        // then
        assertThat(cars.size()).isEqualTo(1);
        assertThat(cars.get(0).getCarNumber()).isEqualTo(carNumber);
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