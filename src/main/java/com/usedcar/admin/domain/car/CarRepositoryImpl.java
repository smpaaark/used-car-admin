package com.usedcar.admin.domain.car;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.usedcar.admin.domain.car.QCar.car;

@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Car> findByCarNumber(String carNumber) {
        return queryFactory.selectFrom(car)
                .where(car.carNumber.eq(carNumber))
                .fetch();
    }

}
