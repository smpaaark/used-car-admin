package com.usedcar.admin.service.release;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.CarRepository;
import com.usedcar.admin.domain.car.CarStatus;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseRepository;
import com.usedcar.admin.domain.release.ReleaseSearch;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.exception.AlreadyCanceledReleaseException;
import com.usedcar.admin.exception.AlreadyReleasedCarException;
import com.usedcar.admin.exception.NotFoundCarException;
import com.usedcar.admin.exception.NotFoundReleaseException;
import com.usedcar.admin.web.dto.release.*;
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
        release.create(car);

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
     * 출고 상태 변경
     */
    @Transactional
    public ReleaseUpdateResponseDto update(Long releaseId, ReleaseUpdateRequestDto requestDto) {
        Release release = releaseRepository.findById(releaseId).orElseThrow(() -> new NotFoundReleaseException("존재하지 않는 출고 번호입니다.(releaseId: " + releaseId + ")"));
        validateReleaseStatus(release);
        
        ReleaseStatus requestStatus = requestDto.getStatus();
        if (requestStatus == ReleaseStatus.CANCEL) {
            release.cancel();
        } else if (requestStatus == ReleaseStatus.COMPLETE) {
            release.complete();
        }

        return new ReleaseUpdateResponseDto(release);
    }

    /**
     * 출고 차량 검색
     */
    public List<ReleaseFindAllResponseDto> findByReleaseSearch(ReleaseSearch releaseSearch) {
        List<Release> releaseList = releaseRepository.findByReleaseSearch(releaseSearch);

        return releaseList.stream().map(ReleaseFindAllResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 출고 상태 체크
     */
    private void validateReleaseStatus(Release release) {
        if (release.getStatus() == ReleaseStatus.CANCEL) {
            throw new AlreadyCanceledReleaseException("이미 취소 처리되어 출고 상태를 변경할 수 없습니다.(releaseId: " + release.getId() + ")");
        }
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
