package com.usedcar.admin.web.dto.payment;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.payment.PaymentType;
import lombok.Getter;

@Getter
public class PaymentFindAllResponseDto {

    private Long id;
    private PaymentType paymentType;
    private int pay_amount;
    private int instalment;
    private String capital;

    public PaymentFindAllResponseDto(Payment entity) {
        this.id = entity.getId();
        this.paymentType = entity.getPaymentType();
        this.pay_amount = entity.getPay_amount();
        this.instalment = entity.getInstalment();
        this.capital = entity.getCapital();
    }
}
