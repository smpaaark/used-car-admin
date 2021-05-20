package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.release.Release;
import lombok.Getter;

@Getter
public class ReleaseUpdateResponseDto {

    private Long id;

    public ReleaseUpdateResponseDto(Release entity) {
        this.id = entity.getId();
    }

}
