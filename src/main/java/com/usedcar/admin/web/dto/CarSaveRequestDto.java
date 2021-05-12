package com.usedcar.admin.web.dto;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class CarSaveRequestDto {

    @NotEmpty
    private String carNumber;
    @NotEmpty
    private String vin;
    @NotNull
    private Category category;
    @NotEmpty
    private String model;
    @NotEmpty
    private String color;
    @NotEmpty
    private String productionYear;

    @Builder
    public CarSaveRequestDto(String carNumber, String vin, Category category, String model, String color, String productionYear) {
        this.carNumber = carNumber;
        this.vin = vin;
        this.category = category;
        this.model = model;
        this.color = color;
        this.productionYear = productionYear;
    }

    public Car toEntity() {
        return Car.builder()
                .carNumber(carNumber)
                .vin(vin)
                .category(category)
                .model(model)
                .color(color)
                .productionYear(productionYear)
                .build();
    }

}
