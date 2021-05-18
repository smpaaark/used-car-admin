package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.release.Release;
import lombok.Getter;

@Getter
public class ReleaseSaveResponseDto {

    private Long id;

    public ReleaseSaveResponseDto(Release release) {
        this.id = release.getId();
    }

}
