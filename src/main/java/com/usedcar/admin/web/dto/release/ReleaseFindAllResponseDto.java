package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.car.Category;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseStatus;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Getter
public class ReleaseFindAllResponseDto {

    private Long id;
    private String staff;
    private String salesStaff;
    private int price;
    private int deposit;
    private ReleaseStatus status;
    private LocalDateTime releaseDate;
    private Car car;

    public ReleaseFindAllResponseDto(Release entity) {
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
