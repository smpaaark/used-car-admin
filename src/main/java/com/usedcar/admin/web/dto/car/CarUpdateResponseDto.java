package com.usedcar.admin.web.dto.car;

import com.usedcar.admin.domain.car.Car;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CarUpdateResponseDto {

    private Long id;

    public CarUpdateResponseDto(Car car) {
        this.id = car.getId();
    }
}
