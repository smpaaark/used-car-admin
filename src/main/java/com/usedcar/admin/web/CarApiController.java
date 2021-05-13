package com.usedcar.admin.web;

import com.usedcar.admin.service.car.CarService;
import com.usedcar.admin.web.dto.CommonResponseDto;
import com.usedcar.admin.web.dto.car.CarSaveRequestDto;
import com.usedcar.admin.web.dto.car.CarSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CarApiController extends ExceptionController {

    private final CarService carService;

    @PostMapping("api/car")
    public ResponseEntity saveCar(@RequestBody @Valid CarSaveRequestDto requestDto, Errors errors) {
        log.info("\n\n=== saveCar start ===\n* requestDto: " + requestDto + "\n");
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()), "필수값 오류", errors));
        }

        CarSaveResponseDto responseDto = carService.save(requestDto);
        log.info("\n\n=== saveCar end ===\n* savedCarId: " + responseDto.getId() + "\n");
        return ResponseEntity.created(null).body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.CREATED.value()), "SUCCESS", responseDto));
    }

}
