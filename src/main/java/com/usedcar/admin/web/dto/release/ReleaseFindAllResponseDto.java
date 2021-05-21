package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.web.dto.car.CarFindAllResponseDto;
import com.usedcar.admin.web.dto.payment.PaymentFindAllResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReleaseFindAllResponseDto {

    private Long id;
    private String staff;
    private String salesStaff;
    private int price;
    private int deposit;
    private ReleaseStatus status;
    private LocalDateTime releaseDate;
    private CarFindAllResponseDto car;
    private List<PaymentFindAllResponseDto> payments;

    public ReleaseFindAllResponseDto(Release entity) {
        this.id = entity.getId();
        this.staff = entity.getStaff();
        this.salesStaff = entity.getSalesStaff();
        this.price = entity.getPrice();
        this.deposit = entity.getDeposit();
        this.status = entity.getStatus();
        this.releaseDate = entity.getReleaseDate();
        this.car = new CarFindAllResponseDto(entity.getCar());
        this.payments = new ArrayList<>();
        for (Payment payment : entity.getPayments()) {
            this.payments.add(new PaymentFindAllResponseDto(payment));
        }
    }

}
