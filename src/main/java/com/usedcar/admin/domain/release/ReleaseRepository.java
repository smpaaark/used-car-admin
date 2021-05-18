package com.usedcar.admin.domain.release;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @Query("SELECT r FROM Release r ORDER BY r.id DESC")
    public List<Release> findAllDesc();

}
