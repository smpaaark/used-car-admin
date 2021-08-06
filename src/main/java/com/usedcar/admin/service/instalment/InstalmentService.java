package com.usedcar.admin.service.instalment;

import com.usedcar.admin.domain.capital.Capital;
import com.usedcar.admin.domain.capital.CapitalRepository;
import com.usedcar.admin.domain.instalment.Instalment;
import com.usedcar.admin.domain.instalment.InstalmentRepository;
import com.usedcar.admin.exception.NotFoundCapitalException;
import com.usedcar.admin.web.dto.instalment.InstalmentSaveRequestDto;
import com.usedcar.admin.web.dto.instalment.InstalmentSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class InstalmentService {

    private final CapitalRepository capitalRepository;
    private final InstalmentRepository instalmentRepository;

    /**
     * 할부 등록
     */
    public InstalmentSaveResponseDto save(InstalmentSaveRequestDto requestDto) {
        // 캐피탈 조회
        Long capitalId = requestDto.getCapitalId();
        Capital capital = capitalRepository.findById(capitalId).orElseThrow(() -> new NotFoundCapitalException("존재하지 않는 캐피탈입니다. (capitalId: " + capitalId + ")"));
        
        // 할부 생성
        Instalment instalment = requestDto.toEntity();
        instalment.create(capital);
        
        // 할부 등록
        instalmentRepository.save(instalment);

        return InstalmentSaveResponseDto.builder().instalment(instalment).build();
    }

}
