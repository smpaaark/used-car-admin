package com.usedcar.admin.web.dto.car;

import com.usedcar.admin.domain.car.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@ToString
public class CarUpdateRequestDto {

    @NotNull
    private Category category;
    @NotEmpty
    private String model;
    @NotEmpty
    private String color;
    @NotEmpty
    private String productionYear;
    @NotEmpty
    private String staff;

    @Builder
    public CarUpdateRequestDto(Category category, String model, String color, String productionYear, String staff) {
        this.category = category;
        this.model = model;
        this.color = color;
        this.productionYear = productionYear;
        this.staff = staff;
    }

}
