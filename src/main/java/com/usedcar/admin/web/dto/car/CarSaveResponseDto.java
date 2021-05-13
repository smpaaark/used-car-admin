package com.usedcar.admin.web.dto.car;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CarSaveResponseDto {

    private Long id;

    @Builder
    public CarSaveResponseDto(Long id) {
        this.id = id;
    }
}
