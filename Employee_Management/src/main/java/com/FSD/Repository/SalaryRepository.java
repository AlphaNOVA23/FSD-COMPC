package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.SalaryEntity;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryEntity, Integer> {
    // You might find this useful later for fetching salary by employee
    // Optional<SalaryEntity> findByEmployee_EmployeeId(Integer employeeId);
}