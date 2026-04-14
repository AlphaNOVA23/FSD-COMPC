package com.FSD.Repository;

import com.FSD.Entity.EmployeeTrainingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTrainingRepository extends JpaRepository<EmployeeTrainingEntity, Integer> {
    List<EmployeeTrainingEntity> findByEmployee_EmployeeId(Integer employeeId);
    List<EmployeeTrainingEntity> findByTrainingProgram_TrainingId(Integer trainingId);
}
