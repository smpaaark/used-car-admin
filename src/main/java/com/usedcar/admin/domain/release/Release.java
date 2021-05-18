package com.usedcar.admin.domain.release;

import com.usedcar.admin.domain.BaseTimeEntity;
import com.usedcar.admin.domain.car.Car;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Builder
    public Release(String staff, String salesStaff, int price, int deposit, Car car, ReleaseStatus status) {
        this.staff = staff;
        this.salesStaff = salesStaff;
        this.price = price;
        this.deposit = deposit;
        this.car = car;
        this.status = status;
    }

    public void updateCar(Car car) {
        this.car = car;
    }

}
