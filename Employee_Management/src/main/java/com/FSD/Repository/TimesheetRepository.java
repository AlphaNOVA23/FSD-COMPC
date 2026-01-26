package com.FSD.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.TimesheetEntity;

@Repository
public interface TimesheetRepository extends JpaRepository<TimesheetEntity, Integer> {
    List<TimesheetEntity> findByEmployee_EmployeeId(Integer employeeId);
    List<TimesheetEntity> findByWorkDate(LocalDate workDate);
}