package com.usedcar.admin.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT count(c) FROM Car c WHERE c.carNumber = :carNumber AND c.status != 'DELETE'")
    public int countByCarNumber(String carNumber);

    @Query("SELECT c FROM Car c WHERE c.status != 'DELETE' ORDER BY c.id DESC")
    public List<Car> findAllDesc();

}
