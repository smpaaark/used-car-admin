package com.usedcar.admin.web.dto;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class CarUpdateRequestDto {

    private Category category;
    private String model;
    private String color;
    private String productionYear;

    @Builder
    public CarUpdateRequestDto(Category category, String model, String color, String productionYear) {
        this.category = category;
        this.model = model;
        this.color = color;
        this.productionYear = productionYear;
    }

}
