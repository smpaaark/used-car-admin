package com.usedcar.admin.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryCustom {

    @Query("SELECT c FROM Car c WHERE c.status = 'NORMAL' ORDER BY c.id DESC")
    public List<Car> findNormal();

}
