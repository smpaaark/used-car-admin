package com.usedcar.admin.domain.car;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long id;

    private String carNumber;
    private String vin;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String model;
    private String color;
    private String productionYear;
    private LocalDateTime purchaseDate;

    @Builder
    public Car(String carNumber, String vin, Category category, String model, String color, String productionYear, LocalDateTime purchaseDate) {
        this.carNumber = carNumber;
        this.vin = vin;
        this.category = category;
        this.model = model;
        this.color = color;
        this.productionYear = productionYear;
        this.purchaseDate = purchaseDate;
    }

}
