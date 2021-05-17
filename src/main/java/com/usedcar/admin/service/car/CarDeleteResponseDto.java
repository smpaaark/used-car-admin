package com.usedcar.admin.service.car;

import com.usedcar.admin.domain.car.Car;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CarDeleteResponseDto {

    private Long id;

    public CarDeleteResponseDto(Car car) {
        this.id = car.getId();
    }

}
