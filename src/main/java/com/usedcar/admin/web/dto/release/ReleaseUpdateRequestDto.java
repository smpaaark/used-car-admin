package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.release.ReleaseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@ToString
public class ReleaseUpdateRequestDto {

    @NotNull
    private ReleaseStatus status;

    @Builder
    public ReleaseUpdateRequestDto(ReleaseStatus status) {
        this.status = status;
    }

}
