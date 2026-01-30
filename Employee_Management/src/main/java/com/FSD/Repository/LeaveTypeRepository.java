package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.LeaveTypeEntity;

// ERROR WAS HERE: Ensure this is an 'interface', NOT a 'class', and remove @Entity if present
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, Long> {
}