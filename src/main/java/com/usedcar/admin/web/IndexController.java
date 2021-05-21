package com.usedcar.admin.web;

import com.usedcar.admin.web.dto.car.CarSaveRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index() {
        log.info("\n\n=== index start ===");
        log.info("\n\n=== index end ===");
        return "index";
    }

    @GetMapping("/car/save")
    public String carSave(Model model) {
        log.info("\n\n=== carSave start ===");
        List<Integer> years = new ArrayList<>();
        for (int i = 2021; i >= 1990; i--) {
            years.add(i);
        }

        model.addAttribute("requestDto", new CarSaveRequestDto());
        model.addAttribute("years", years);

        log.info("\n\n=== carSave end ===");
        return "/car/car-save";
    }

}
