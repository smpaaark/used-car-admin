package com.usedcar.admin.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usedcar.admin.service.instalment.InstalmentService;
import com.usedcar.admin.web.dto.CommonResponseDto;
import com.usedcar.admin.web.dto.instalment.InstalmentSaveRequestDto;
import com.usedcar.admin.web.dto.instalment.InstalmentSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InstalmentApiController extends ExceptionApiController {

    private final InstalmentService instalmentService;
    private final ObjectMapper objectMapper;

    /**
     * 할부 등록
     */
    @PostMapping("/api/instalment")
    public ResponseEntity saveInstalment(@RequestBody @Valid InstalmentSaveRequestDto requestDto, Errors errors) throws JsonProcessingException {
        // 요청 로그 출력
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n");
        requestLog.append("=== saveInstalment start ===\n");
        requestLog.append("* request\n");
        requestLog.append("  * " + objectMapper.writeValueAsString(requestDto) + "\n");
        log.info(requestLog.toString());
        
        // 필수값 검증
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()), "필수값 오류", errors));
        }
        
        // 할부 등록
        InstalmentSaveResponseDto instalmentSaveResponseDto = instalmentService.save(requestDto);
        
        // 응답 본문 생성
        CommonResponseDto responseDto = CommonResponseDto.createResponseDto(String.valueOf(HttpStatus.CREATED.value()), "SUCCESS", instalmentSaveResponseDto);
        
        // 응답 로그 생성
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n");
        responseLog.append("=== saveInstalment Success ===\n");
        responseLog.append("* instalmentId: " + instalmentSaveResponseDto.getId() + "\n");
        responseLog.append("* Response\n");
        responseLog.append("  * " + objectMapper.writeValueAsString(responseDto) + "\n");
        log.info(responseLog.toString());

        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

}
