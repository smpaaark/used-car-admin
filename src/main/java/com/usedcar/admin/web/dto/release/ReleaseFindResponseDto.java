package com.usedcar.admin.web.dto.release;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.payment.PaymentType;
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
    private boolean isCanceled;
    private LocalDateTime releaseDate;
    private CarFindResponseDto car;
    private List<PaymentFindResponseDto> payments;
    private int cash_pay_amount;
    private int card_pay_amount;
    private int instalment;
    private String capital;

    public ReleaseFindResponseDto(Release entity) {
        this.id = entity.getId();
        this.staff = entity.getStaff();
        this.salesStaff = entity.getSalesStaff();
        this.price = entity.getPrice();
        this.status = entity.getStatus();
        if (status == ReleaseStatus.CANCEL) {
            this.isCanceled = true;
        }
        this.releaseDate = entity.getReleaseDate();
        this.car = new CarFindResponseDto(entity.getCar());
        this.payments = new ArrayList<>();
        for (Payment payment : entity.getPayments()) {
            PaymentFindResponseDto dto = new PaymentFindResponseDto(payment);
            this.payments.add(dto);

            if (dto.getPaymentType() == PaymentType.CASH) {
                this.cash_pay_amount = dto.getPay_amount();
            } else {
                this.card_pay_amount = dto.getPay_amount();
                this.instalment = dto.getInstalment();
                this.capital = dto.getCapital();
            }
        }
    }

}
