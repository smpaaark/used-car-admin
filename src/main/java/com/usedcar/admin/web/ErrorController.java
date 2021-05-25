package com.usedcar.admin.web;

import com.usedcar.admin.exception.NotFoundCarException;
import com.usedcar.admin.web.dto.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@Slf4j
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Model model, Exception e) {
        log.info("\n\n=== Exception ===\n* message: " + e.getMessage());
        model.addAttribute("message", e.getMessage());

        return "error";
    }

    @ExceptionHandler(NotFoundCarException.class)
    public String notFoundCarHandler(Model model, NotFoundCarException e) {
        log.info("\n\n=== NotFoundCarException ===\n* message: " + e.getMessage());
        model.addAttribute("message", e.getMessage());

        return "error";
    }

}
