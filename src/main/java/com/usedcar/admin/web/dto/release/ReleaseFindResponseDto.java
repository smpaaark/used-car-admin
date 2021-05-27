package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.web.dto.car.CarFindResponseDto;
import com.usedcar.admin.web.dto.payment.PaymentFindAllResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReleaseFindResponseDto {

    private Long id;
    private String staff;
    private String salesStaff;
    private int price;
    private ReleaseStatus status;
    private LocalDateTime releaseDate;
    private CarFindResponseDto car;
    private List<PaymentFindResponseDto> payments;

    public ReleaseFindResponseDto(Release entity) {
        this.id = entity.getId();
        this.staff = entity.getStaff();
        this.salesStaff = entity.getSalesStaff();
        this.price = entity.getPrice();
        this.status = entity.getStatus();
        this.releaseDate = entity.getReleaseDate();
        this.car = new CarFindResponseDto(entity.getCar());
        this.payments = new ArrayList<>();
        for (Payment payment : entity.getPayments()) {
            this.payments.add(new PaymentFindResponseDto(payment));
        }
    }

}
