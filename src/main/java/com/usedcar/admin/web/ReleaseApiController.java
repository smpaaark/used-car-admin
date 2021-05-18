package com.usedcar.admin.web;

import com.usedcar.admin.service.release.ReleaseService;
import com.usedcar.admin.web.dto.CommonResponseDto;
import com.usedcar.admin.web.dto.release.ReleaseFindAllResponseDto;
import com.usedcar.admin.web.dto.release.ReleaseFindResponseDto;
import com.usedcar.admin.web.dto.release.ReleaseSaveRequestDto;
import com.usedcar.admin.web.dto.release.ReleaseSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReleaseApiController extends ExceptionController {

    private final ReleaseService releaseService;

    @PostMapping("/api/release")
    public ResponseEntity release(@RequestBody @Valid ReleaseSaveRequestDto requestDto, Errors errors) {
        log.info("\n\n=== release start ===\n* requestDto: " + requestDto + "\n");
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()), "필수값 오류", errors));
        }

        ReleaseSaveResponseDto responseDto = releaseService.save(requestDto);
        log.info("\n\n=== release end ===\n* releaseId: " + responseDto.getId() + "\n");
        return ResponseEntity.created(null).body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.CREATED.value()), "SUCCESS", responseDto));
    }

    @GetMapping("/api/releaseList")
    public ResponseEntity findReleaseList() {
        log.info("\n\n=== findReleaseList start ===\n");
        List<ReleaseFindAllResponseDto> responseDtos = releaseService.findAllDesc();
        log.info("\n\n=== findReleaseList end ===\n* releaseCount: " + responseDtos.size() + "\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDtos));
    }

    @GetMapping("/api/release/{releaseId}")
    public ResponseEntity findRelease(@PathVariable("releaseId") Long releaseId) {
        log.info("\n\n=== findRelease start ===\n* releaseId: " + releaseId + "\n");
        ReleaseFindResponseDto responseDto = releaseService.findById(releaseId);
        log.info("\n\n=== findRelease end ===\n* findRelease: " + responseDto + "\n");
        return ResponseEntity.ok().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.OK.value()), "SUCCESS", responseDto));
    }

}
