package com.usedcar.admin.domain.car;

import java.util.List;
import java.util.Optional;

public interface CarRepositoryCustom {

    public Long countByCarNumber(String carNumber);

    public List<Car> findAllDesc();

    public Optional<Car> findCarStatus(Long carId);

    public List<Car> findNormal();

}
