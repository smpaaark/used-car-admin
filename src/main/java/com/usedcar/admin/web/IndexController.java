package com.usedcar.admin.web;

import com.usedcar.admin.config.auth.dto.SessionUser;
import com.usedcar.admin.domain.car.CarSearch;
import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.service.car.CarService;
import com.usedcar.admin.service.release.ReleaseService;
import com.usedcar.admin.web.dto.car.CarFindAllResponseDto;
import com.usedcar.admin.web.dto.car.CarFindResponseDto;
import com.usedcar.admin.web.dto.car.CarSaveRequestDto;
import com.usedcar.admin.web.dto.release.ReleaseFindAllResponseDto;
import com.usedcar.admin.web.dto.release.ReleaseFindResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController extends ErrorController {

    private final CarService carService;
    private final ReleaseService releaseService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        log.info("\n\n=== index start ===");

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }

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

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }

        log.info("\n\n=== carSave end ===");
        return "/car/car-save";
    }

    @GetMapping("/car/findAll")
    public String carFindAll(Model model) {
        log.info("\n\n=== carFindAll start ===");
        List<CarFindAllResponseDto> cars = carService.findAllDesc();
        model.addAttribute("cars", cars);
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }

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

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        log.info("\n\n=== carFind end ===");

        return "/car/car-update";
    }

    @GetMapping("/car/findNormal")
    public String carFindNormal(Model model) {
        log.info("\n\n=== carFindNormal start ===");
        List<CarFindAllResponseDto> cars = carService.findNormal();
        model.addAttribute("cars", cars);

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        log.info("\n\n=== carFindNormal end ===");

        return "/car/car-find-normal";
    }

    @GetMapping("/release")
    public String release(Model model, @RequestParam Long carId, @RequestParam String carNumber, @RequestParam String carModel, @RequestParam String color) {
        log.info("\n\n=== release start ===");
        model.addAttribute("carId", carId);
        model.addAttribute("carNumber", carNumber);
        model.addAttribute("model", carModel);
        model.addAttribute("color", color);

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        log.info("\n\n=== release end ===");
        return "/release/release";
    }

    @GetMapping("/release/findAll")
    public String releaseFindAll(Model model) {
        log.info("\n\n=== releaseFindAll start ===");
        List<ReleaseFindAllResponseDto> releaseList = releaseService.findAllDesc();
        for (ReleaseFindAllResponseDto dto : releaseList) {
            dto.formattingReleaseDate();
        }
        model.addAttribute("releaseList", releaseList);

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        log.info("\n\n=== releaseFindAll end ===");

        return "/release/release-find-all";
    }

    @GetMapping("/release/find/{releaseId}")
    public String releaseFind(@PathVariable("releaseId") Long releaseId, Model model) {
        log.info("\n\n=== releaseFind start ===");
        ReleaseFindResponseDto release = releaseService.findById(releaseId);
        model.addAttribute("release", release);
        
        List<String> statusList = new ArrayList<>();
        if (release.getStatus() == ReleaseStatus.READY) {
            statusList.add("입금 대기");
            statusList.add("출고 완료");
            statusList.add("출고 취소");
        } else if (release.getStatus() == ReleaseStatus.COMPLETE){
            statusList.add("출고 완료");
            statusList.add("입금 대기");
            statusList.add("출고 취소");
        } else {
            statusList.add("출고 취소");
        }
        model.addAttribute("statusList", statusList);

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        log.info("\n\n=== releaseFind end ===");

        return "/release/release-update";
    }

}
