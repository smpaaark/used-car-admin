package com.usedcar.admin.domain.release;

import com.usedcar.admin.domain.BaseTimeEntity;
import com.usedcar.admin.domain.car.Car;
import com.usedcar.admin.domain.payment.Payment;
import com.usedcar.admin.web.dto.release.ReleaseSaveRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class Release extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "release_id")
    private Long id;

    private String staff;
    private String salesStaff;
    private int price;
    private int deposit;

    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;

    private LocalDateTime releaseDate = LocalDateTime.now();

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "release", cascade = ALL)
    private List<Payment> payments = new ArrayList<>();

    @Builder
    public Release(String staff, String salesStaff, int price, int deposit, ReleaseStatus status, Car car, List<Payment> payments) {
        this.staff = staff;
        this.salesStaff = salesStaff;
        this.price = price;
        this.deposit = deposit;
        this.car = car;
        this.status = status;
        for (Payment payment : payments) {
            this.payments.add(payment);
        }
    }

    public void create(Car car) {
        car.release(this.releaseDate);
        this.car = car;

        for (Payment payment : payments) {
            payment.create(this);
        }
    }

    public void cancel() {
        this.status = ReleaseStatus.CANCEL;
        this.car.cancelRelease();
    }

    public void complete() {
        this.status = ReleaseStatus.COMPLETE;
    }
}
