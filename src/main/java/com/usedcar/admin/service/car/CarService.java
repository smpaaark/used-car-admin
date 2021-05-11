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
    public Long save(CarSaveRequestDto requestDto) {
        return carRepository.save(requestDto.toEntity()).getId();
    }

}
