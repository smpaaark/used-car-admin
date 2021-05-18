package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReleaseFindResponseDto {

    private Long id;
    private String staff;
    private String salesStaff;
    private int price;
    private int deposit;
    private ReleaseStatus status;
    private LocalDateTime releaseDate;
    private Car car;

    public ReleaseFindResponseDto(Release entity) {
        this.id = entity.getId();
        this.staff = entity.getStaff();
        this.salesStaff = entity.getSalesStaff();
        this.price = entity.getPrice();
        this.deposit = entity.getDeposit();
        this.status = entity.getStatus();
        this.releaseDate = entity.getReleaseDate();
        this.car = entity.getCar();
    }

}
