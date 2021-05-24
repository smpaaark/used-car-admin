package com.usedcar.admin.web.dto.car;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.Category;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class CarFindResponseDto {

    private Long id;
    private String carNumber;
    private String vin;
    private Category category;
    private String model;
    private String color;
    private String productionYear;
    private String staff;
    private String purchaseDate;

    public CarFindResponseDto(Car entity) {
        this.id = entity.getId();
        this.carNumber = entity.getCarNumber();
        this.vin = entity.getVin();
        this.category = entity.getCategory();
        this.model = entity.getModel();
        this.color = entity.getColor();
        this.productionYear = entity.getProductionYear();
        this.staff = entity.getStaff();
        this.purchaseDate = entity.getPurchaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
