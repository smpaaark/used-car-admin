package com.usedcar.admin.domain.release;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long>, ReleaseRepositoryCustom {

}
