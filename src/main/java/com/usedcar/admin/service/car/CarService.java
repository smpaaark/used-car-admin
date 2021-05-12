package com.usedcar.admin.service.car;

import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.web.dto.CarSaveRequestDto;
import com.usedcar.admin.web.dto.CarSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    @Transactional
    public CarSaveResponseDto save(CarSaveRequestDto requestDto) {
        validateDuplicateCar(requestDto);
        Long id = carRepository.save(requestDto.toEntity()).getId();

        return CarSaveResponseDto.builder()
                .id(id)
                .build();
    }

    private void validateDuplicateCar(CarSaveRequestDto requestDto) {
        int count = carRepository.countByCarNumber(requestDto.getCarNumber());
        if (count > 0) {
            throw new IllegalArgumentException("이미 매입되어있는 차량입니다. 차량번호: " + requestDto.getCarNumber());
        }
    }

}
