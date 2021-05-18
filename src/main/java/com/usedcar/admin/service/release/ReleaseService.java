package com.usedcar.admin.service.release;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseRepository;
import com.usedcar.admin.exception.AlreadyReleasedCarException;
import com.usedcar.admin.exception.NotFoundCarException;
import com.usedcar.admin.exception.NotFoundReleaseException;
import com.usedcar.admin.web.dto.release.ReleaseFindAllResponseDto;
import com.usedcar.admin.web.dto.release.ReleaseFindResponseDto;
import com.usedcar.admin.web.dto.release.ReleaseSaveRequestDto;
import com.usedcar.admin.web.dto.release.ReleaseSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final CarRepository carRepository;

    /**
     * 차량 출고
     */
    @Transactional
    public ReleaseSaveResponseDto save(ReleaseSaveRequestDto requestDto) {
        Long carId = requestDto.getCarId();
        Car car = carRepository.findCarStatus(carId).orElseThrow(() -> new NotFoundCarException("존재하지 않는 차량입니다.(carId: " + carId + ")"));
        validateCarSave(car);

        Release release = requestDto.toEntity();
        car.release(release.getReleaseDate());
        release.updateCar(car);

        return new ReleaseSaveResponseDto(releaseRepository.save(release));
    }

    /**
     * 출고 차량 전체 조회(최신순)
     */
    public List<ReleaseFindAllResponseDto> findAllDesc() {
        List<Release> releaseList = releaseRepository.findAllDesc();

        return releaseList.stream().map(ReleaseFindAllResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 출고 차량 1개 조회
     */
    public ReleaseFindResponseDto findById(Long releaseId) {
        Release release = releaseRepository.findById(releaseId).orElseThrow(() -> new NotFoundReleaseException("존재하지 않는 출고 번호입니다.(releaseId: " + releaseId + ")"));

        return new ReleaseFindResponseDto(release);
    }

    /**
     * 출고 차량 중복 체크
     */
    private void validateCarSave(Car car) {
        if (car.getStatus() == CarStatus.RELEASE) {
            throw new AlreadyReleasedCarException("이미 출고된 차량입니다.(carId: " + car.getId() + ")");
        }
    }
}
