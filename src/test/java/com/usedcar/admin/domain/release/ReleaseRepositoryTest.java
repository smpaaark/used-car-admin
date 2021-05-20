package com.usedcar.admin.domain.release;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        ReleaseStatus status = ReleaseStatus.READY;

        Car car = carRepository.findById(1L).get();
        car.release(LocalDateTime.now());

        // when
        releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car));
        
        // then
        List<Release> list = releaseRepository.findAll();
        Release release = list.get(0);
        assertThat(release.getId()).isGreaterThan(0L);
        assertThat(release.getStaff()).isEqualTo(staff);
        assertThat(release.getSalesStaff()).isEqualTo(salesStaff);
        assertThat(release.getPrice()).isEqualTo(price);
        assertThat(release.getDeposit()).isEqualTo(deposit);
        assertThat(release.getStatus()).isEqualTo(status);
        assertThat(release.getReleaseDate()).isNotNull();
        assertThat(release.getCar()).isEqualTo(car);
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
        List<Release> list = releaseRepository.findAllDesc();
        
        // then
        Release release = list.get(0);
        assertThat(release.getId()).isEqualTo(3L);
    }

    @Test
    @Transactional
    public void 차량_1개_조회() throws Exception {
        // given
        String staff = "박성민";
        String salesStaff = "이재광";
        int price = 30000000;
        int deposit = 15000000;
        ReleaseStatus status = ReleaseStatus.READY;

        Car car = carRepository.findById(1L).get();
        car.release(LocalDateTime.now());

        Release release1 = releaseRepository.save(getRelease(staff, salesStaff, price, deposit, status, car));

        // when
        Release release2 = releaseRepository.findById(release1.getId()).get();

        // then
        assertThat(release2.getId()).isEqualTo(release1.getId());
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