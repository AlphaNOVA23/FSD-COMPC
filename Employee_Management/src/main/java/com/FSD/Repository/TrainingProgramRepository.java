package com.FSD.Repository;

import com.FSD.Entity.TrainingProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgramEntity, Integer> {
}
