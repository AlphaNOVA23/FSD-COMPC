package com.FSD.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.PositionDetailsEntity;

@Repository
public interface PositionDetailsRepository extends JpaRepository<PositionDetailsEntity, Integer> {
}