package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.DepartmentHeadEntity;

/**
 * Spring Data JPA repository for the DepartmentHeadEntity.
 * This interface handles all the database operations for the department_head table.
 */
@Repository
public interface DepartmentHeadRepository extends JpaRepository<DepartmentHeadEntity, Integer> {
}
