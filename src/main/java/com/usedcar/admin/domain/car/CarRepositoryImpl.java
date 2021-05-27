package com.usedcar.admin.domain.car;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.usedcar.admin.domain.car.QCar.car;

@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByCarNumber(String carNumber) {
        return queryFactory
                .selectFrom(car)
                .where(car.carNumber.eq(carNumber),
                        car.status.ne(CarStatus.DELETE))
                .fetchCount();
    }

    @Override
    public List<Car> findAllDesc() {
        return queryFactory
                .selectFrom(car)
                .where(car.status.ne(CarStatus.DELETE))
                .orderBy(car.id.desc())
                .fetch();
    }

    @Override
    public Optional<Car> findCarStatus(Long carId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(car)
                .where(car.id.eq(carId),
                        car.status.ne(CarStatus.DELETE))
                .fetchOne());
    }

    @Override
    public List<Car> findNormal() {
        return queryFactory
                .selectFrom(car)
                .where(car.status.eq(CarStatus.NORMAL))
                .orderBy(car.id.desc())
                .fetch();
    }

}
