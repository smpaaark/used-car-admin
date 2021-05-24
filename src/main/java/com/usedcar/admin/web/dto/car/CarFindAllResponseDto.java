package com.usedcar.admin.web.dto.car;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.domain.car.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CarFindAllResponseDto {

    private Long id;
    private String carNumber;
    private String vin;
    private Category category;
    private String model;
    private String color;
    private String productionYear;
    private String staff;
    private LocalDateTime purchaseDate;
    private String status;
    private LocalDateTime releaseDate;

    public CarFindAllResponseDto(Car entity) {
        this.id = entity.getId();
        this.carNumber = entity.getCarNumber();
        this.vin = entity.getVin();
        this.category = entity.getCategory();
        this.model = entity.getModel();
        this.color = entity.getColor();
        this.productionYear = entity.getProductionYear();
        this.staff = entity.getStaff();
        this.purchaseDate = entity.getPurchaseDate();
        CarStatus status = entity.getStatus();
        if (status == CarStatus.NORMAL) {
            this.status = "출고 가능";
        } else if (status == CarStatus.RELEASE) {
            this.status = "출고 완료";
        }
        this.releaseDate = entity.getReleaseDate();
    }

}
