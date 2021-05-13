package com.usedcar.admin.service.car;

import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.exception.DuplicatedCarNumberException;
import com.usedcar.admin.web.dto.car.CarSaveRequestDto;
import com.usedcar.admin.web.dto.car.CarSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Long id = carRepository.save(requestDto.toEntity()).getId();

        return CarSaveResponseDto.builder()
                .id(id)
                .build();
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
