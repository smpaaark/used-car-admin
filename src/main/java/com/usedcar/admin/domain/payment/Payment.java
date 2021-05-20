package com.usedcar.admin.domain.payment;

import com.usedcar.admin.domain.BaseTimeEntity;
import com.usedcar.admin.domain.release.Release;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private int pay_amount;
    private int instalment;
    private String capital;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "release_id")
    private Release release;

    @Builder
    public Payment(PaymentType paymentType, int pay_amount, int instalment, String capital, Release release) {
        this.paymentType = paymentType;
        this.pay_amount = pay_amount;
        this.instalment = instalment;
        this.capital = capital;
        this.release = release;
    }

    public void create(Release release) {
        this.release = release;
    }

}
