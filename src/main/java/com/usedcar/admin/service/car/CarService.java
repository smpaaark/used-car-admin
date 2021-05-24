package com.usedcar.admin.service.car;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.exception.AlreadyReleasedCarException;
import com.usedcar.admin.exception.DuplicatedCarNumberException;
import com.usedcar.admin.exception.NotFoundCarException;
import com.usedcar.admin.web.dto.car.CarUpdateRequestDto;
import com.usedcar.admin.web.dto.car.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    /**
     * 차량 매입
     */
    @Transactional
    public CarSaveResponseDto save(CarSaveRequestDto requestDto) {
        validateDuplicateCar(requestDto);
        return new CarSaveResponseDto(carRepository.save(requestDto.toEntity()));
    }

    /**
     * 차량 전체 조회 (최신순)
     */
    public List<CarFindAllResponseDto> findAllDesc() {
        List<Car> cars = carRepository.findAllDesc();

        return cars.stream().map(CarFindAllResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 차량 1개 조회
     */
    public CarFindResponseDto findById(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new NotFoundCarException("존재하지 않는 차량입니다.(carId: " + carId + ")"));
        return new CarFindResponseDto(car);
    }

    /**
     * 차량 정보 수정
     */
    @Transactional
    public CarUpdateResponseDto update(Long carId, CarUpdateRequestDto requestDto) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new NotFoundCarException("존재하지 않는 차량입니다.(carId: " + carId + ")"));
        car.update(requestDto);

        return new CarUpdateResponseDto(car);
    }

    /**
     * 차량 삭제
     */
    @Transactional
    public CarDeleteResponseDto delete(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new NotFoundCarException("존재하지 않는 차량입니다.(carId: " + carId + ")"));
        validateCarReleased(car);
        car.delete();

        return new CarDeleteResponseDto(car);
    }

    /**
     * 출고 가능한 차량 목록 조회
     */
    public List<CarFindAllResponseDto> findNormal() {
        List<Car> cars = carRepository.findNormal();

        return cars.stream().map(CarFindAllResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 차량 출고 여부 체크
     */
    private void validateCarReleased(Car car) {
        if (car.getStatus() == CarStatus.RELEASE) {
            throw new AlreadyReleasedCarException("출고중인 차량은 삭제할 수 없습니다. 출고 취소 후 삭제해 주세요.");
        }
    }

    /**
     * 차량 매입 중복 체크
     */
    private void validateDuplicateCar(CarSaveRequestDto requestDto) {
        int count = carRepository.countByCarNumber(requestDto.getCarNumber());
        if (count > 0) {
            throw new DuplicatedCarNumberException("이미 매입되어있는 차량입니다.(차량번호: " + requestDto.getCarNumber() + ")");
        }
    }
}
