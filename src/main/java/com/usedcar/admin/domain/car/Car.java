package com.usedcar.admin.domain.car;

import com.usedcar.admin.domain.BaseTimeEntity;
import com.usedcar.admin.web.dto.CarUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Car extends BaseTimeEntity {

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

    @Enumerated(EnumType.STRING)
    private CarStatus status = CarStatus.NORMAL;

    @Builder
    public Car(String carNumber, String vin, Category category, String model, String color, String productionYear, LocalDateTime purchaseDate) {
        this.carNumber = carNumber;
        this.vin = vin;
        this.category = category;
        this.model = model;
        this.color = color;
        this.productionYear = productionYear;
        this.purchaseDate = purchaseDate == null ? LocalDateTime.now() : purchaseDate;
    }

    public void update(CarUpdateRequestDto requestDto) {
        this.category = requestDto.getCategory();
        this.model = requestDto.getModel();
        this.color = requestDto.getColor();
        this.productionYear = requestDto.getProductionYear();
    }

    public void delete() {
        this.status = CarStatus.DELETE;
    }
}
