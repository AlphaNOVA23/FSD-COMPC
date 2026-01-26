package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FSD.Entity.DepartmentEntity;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {}

