package com.usedcar.admin.web;

import com.usedcar.admin.exception.DuplicatedCarNumberException;
import com.usedcar.admin.web.dto.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        log.info("\n\n=== Exception ===\n* message: " + e.getMessage());
        return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "시스템 오류"));
    }
    
    @ExceptionHandler(DuplicatedCarNumberException.class)
    public ResponseEntity<?> duplicatedCarNumberHandler(DuplicatedCarNumberException e) {
        log.info("\n\n=== DuplicatedCarNumberException ===\n* message: " + e.getMessage());
        return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage()));
    }

}
