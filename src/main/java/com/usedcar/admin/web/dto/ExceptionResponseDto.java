package com.usedcar.admin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponseDto {

    private String message;

    @Builder
    public ExceptionResponseDto(String message) {
        this.message = message;
    }
}
