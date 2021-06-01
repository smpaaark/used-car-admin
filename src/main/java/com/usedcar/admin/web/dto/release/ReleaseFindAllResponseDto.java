package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.release.Release;
import com.usedcar.admin.domain.release.ReleaseStatus;
import com.usedcar.admin.web.dto.car.CarFindAllResponseDto;
import com.usedcar.admin.web.dto.payment.PaymentFindAllResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReleaseFindAllResponseDto {

    private Long id;
    private String staff;
    private String salesStaff;
    private int price;
    private ReleaseStatus status;
    private String statusString;
    private boolean isStatusReady;
    private boolean isStatusComplete;
    private boolean isStatusCancel;
    private LocalDateTime releaseDate;
    private String formattedReleaseDate;
    private CarFindAllResponseDto car;
    private List<PaymentFindAllResponseDto> payments;

    public ReleaseFindAllResponseDto(Release entity) {
        this.id = entity.getId();
        this.staff = entity.getStaff();
        this.salesStaff = entity.getSalesStaff();
        this.price = entity.getPrice();
        this.status = entity.getStatus();
        if (status == ReleaseStatus.READY) {
            this.statusString = "입금 대기";
            this.isStatusReady = true;
        } else if (status == ReleaseStatus.COMPLETE) {
            this.statusString = "출고 완료";
            this.isStatusComplete = true;
        } else {
            this.statusString = "출고 취소";
            this.isStatusCancel = true;
        }
        this.releaseDate = entity.getReleaseDate();
        this.formattedReleaseDate = this.releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.car = new CarFindAllResponseDto(entity.getCar());
        this.payments = new ArrayList<>();
        for (Payment payment : entity.getPayments()) {
            this.payments.add(new PaymentFindAllResponseDto(payment));
        }
    }

    public void formattingReleaseDate() {
        this.formattedReleaseDate = this.releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
