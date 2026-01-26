package com.FSD.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.ResponsibilityEntity;

@Repository
public interface ResponsibilityRepository extends JpaRepository<ResponsibilityEntity, Integer> {
}