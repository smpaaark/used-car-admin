package com.usedcar.admin.web;

import com.usedcar.admin.web.dto.ExceptionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handler(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ExceptionResponseDto.builder()
                        .message(e.getMessage())
                        .build());
    }

}
