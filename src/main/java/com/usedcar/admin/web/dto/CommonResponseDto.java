package com.usedcar.admin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommonResponseDto<T> {

    private String status;
    private String message;
    private T data;
    private LocalDateTime responseDate;

    @Builder
    public CommonResponseDto(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.responseDate = LocalDateTime.now();
    }

    public static <T> CommonResponseDto createResponseDto(String status, String message, T data) {
        return CommonResponseDto.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto createResponseDto(String status, String message) {
        return CommonResponseDto.builder()
                .status(status)
                .message(message)
                .build();
    }

}
