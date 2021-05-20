package com.usedcar.admin.web.dto.payment;

import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.domain.payment.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PaymentRequestDto {

    private PaymentType paymentType;
    private int pay_amount;
    private int instalment;
    private String capital;

    @Builder
    public PaymentRequestDto(PaymentType paymentType, int pay_amount, int instalment, String capital) {
        this.paymentType = paymentType;
        this.pay_amount = pay_amount;
        this.instalment = instalment;
        this.capital = capital;
    }

    public Payment toEntity() {
        return Payment.builder()
                .paymentType(paymentType)
                .pay_amount(pay_amount)
                .instalment(instalment)
                .capital(capital)
                .build();
    }

}
