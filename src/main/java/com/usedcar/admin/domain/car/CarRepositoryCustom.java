package com.usedcar.admin.domain.car;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface CarRepositoryCustom {

    public List<Car> findByCarNumber(String carNumber);

}
