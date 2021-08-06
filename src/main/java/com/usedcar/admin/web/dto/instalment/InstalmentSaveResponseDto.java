package com.usedcar.admin.web.dto.instalment;

import com.usedcar.admin.domain.instalment.Instalment;
import com.usedcar.admin.domain.instalment.InstalmentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class InstalmentSaveResponseDto {

    private Long id;
    private InstalmentStatus instalmentStatus;

    @Builder
    public InstalmentSaveResponseDto(Instalment instalment) {
        this.id = instalment.getId();
        this.instalmentStatus = instalment.getInstalmentStatus();
    }

}
