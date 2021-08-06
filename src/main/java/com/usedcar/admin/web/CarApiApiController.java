package com.usedcar.admin.web;

import com.usedcar.admin.domain.car.CarSearch;
import com.usedcar.admin.service.car.CarDeleteResponseDto;
import com.usedcar.admin.service.car.CarService;
import com.usedcar.admin.web.dto.car.CarUpdateRequestDto;
import com.usedcar.admin.web.dto.CommonResponseDto;
import com.usedcar.admin.web.dto.car.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CarApiApiController extends ExceptionApiController {

    private final CarService carService;

    @PostMapping("/api/car")
    public ResponseEntity saveCar(@RequestBody @Valid CarSaveRequestDto requestDto, Errors errors) {
        log.info("\n\n=== saveCar start ===\n* requestDto: " + requestDto + "\n");
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()), "필수값 오류", errors));
        }

        CarSaveResponseDto responseDto = carService.save(requestDto);
        log.info("\n\n=== saveCar end ===\n* savedCarId: " + responseDto.getId() + "\n");
        return ResponseEntity.created(null).body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.CREATED.value()), "SUCCESS", responseDto));
    }

    @GetMapping("/api/cars")
    public ResponseEntity findCars() {
        log.info("\n\n=== findCars start ===");
        List<CarFindAllResponseDto> responseDtos = carService.findAllDesc();
        log.info("\n\n=== findCars end ===\n* carsCount: " + responseDtos.size() + "\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDtos));
    }

    @GetMapping("/api/cars/normal")
    public ResponseEntity findCarsNormal() {
        log.info("\n\n=== findCarsNormal start ===");
        List<CarFindAllResponseDto> responseDtos = carService.findNormal();
        log.info("\n\n=== findCarsNormal end ===\n* carsCount: " + responseDtos.size() + "\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDtos));
    }

    @GetMapping("/api/car/{carId}")
    public ResponseEntity findCar(@PathVariable("carId") Long carId) {
        log.info("\n\n=== findCar start ===\n* carId: " + carId + "\n");
        CarFindResponseDto responseDto = carService.findById(carId);
        log.info("\n\n=== findCar end ===\n* findCar: " + responseDto + "\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDto));
    }

    @PutMapping("/api/car/{carId}")
    public ResponseEntity updateCar(@PathVariable("carId") Long carId, @RequestBody @Valid CarUpdateRequestDto requestDto, Errors errors) {
        log.info("\n\n=== updateCar start ===\n* carId: " + carId + "\n* requestDto: " + requestDto);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()), "필수값 오류", errors));
        }
        CarUpdateResponseDto responseDto = carService.update(carId, requestDto);
        log.info("\n\n=== updateCar end ===\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDto));
    }

    @DeleteMapping("/api/car/{carId}")
    public ResponseEntity deleteCar(@PathVariable("carId") Long carId) {
        log.info("\n\n=== deleteCar start ===\n* carId: " + carId);
        CarDeleteResponseDto responseDto = carService.delete(carId);
        log.info("\n\n=== deleteCar end ===\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDto));
    }

    @GetMapping("/api/car/search")
    public ResponseEntity carSearch(@ModelAttribute CarSearch carSearch) {
        log.info("\n\n=== carSearch start ===\n* carSearch: " + carSearch + "\n");
        List<CarFindAllResponseDto> responseDtos = carService.findByCarSearch(carSearch);
        log.info("\n\n=== carSearch end ===\n* carsCount: " + responseDtos.size() + "\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDtos));
    }

}
