package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.ShiftEntity;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Integer> {
}