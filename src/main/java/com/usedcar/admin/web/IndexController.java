package com.usedcar.admin.web;

import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.service.car.CarService;
import com.usedcar.admin.web.dto.car.CarFindAllResponseDto;
import com.usedcar.admin.web.dto.car.CarFindResponseDto;
import com.usedcar.admin.web.dto.car.CarSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final CarService carService;

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

    @GetMapping("/car/findAll")
    public String carFindAll(Model model) {
        log.info("\n\n=== carFindAll start ===");
        List<CarFindAllResponseDto> cars = carService.findAllDesc();
        model.addAttribute("cars", cars);
        log.info("\n\n=== carFindAll end ===");

        return "/car/car-find-all";
    }

    @GetMapping("/car/find/{carId}")
    public String carFind(@PathVariable("carId") Long carId, Model model) {
        log.info("\n\n=== carFind start ===");
        CarFindResponseDto car = carService.findById(carId);
        model.addAttribute("car", car);

        List<Integer> years = new ArrayList<>();
        years.add(Integer.parseInt(car.getProductionYear()));
        for (int i = 2021; i >= 1990; i--) {
            years.add(i);
        }
        model.addAttribute("years", years);

        List<String> category = new ArrayList<>();
        if (car.getCategory() == Category.DOMESTIC) {
            category.add("국산");
            category.add("외제");
        } else {
            category.add("외제");
            category.add("국산");
        }
        model.addAttribute("category", category);
        log.info("\n\n=== carFind end ===");

        return "/car/car-update";
    }

    @GetMapping("/car/findNormal")
    public String carFindNormal(Model model) {
        log.info("\n\n=== carFindNormal start ===");
        List<CarFindAllResponseDto> cars = carService.findNormal();
        model.addAttribute("cars", cars);

        log.info("\n\n=== carFindNormal end ===");

        return "/car/car-find-normal";
    }

}
