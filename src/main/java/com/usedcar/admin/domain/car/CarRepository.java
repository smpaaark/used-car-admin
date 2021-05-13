package com.usedcar.admin.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    public int countByCarNumber(String carNumber);

    public List<Car> findAllByOrderByIdDesc();

}
