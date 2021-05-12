package com.usedcar.admin.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    public int countByCarNumber(String carNumber);

}
