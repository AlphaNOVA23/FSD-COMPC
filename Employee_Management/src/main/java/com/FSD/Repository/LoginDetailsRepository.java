package com.FSD.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.LoginDetailsEntity;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetailsEntity, Integer> {
    LoginDetailsEntity findByUsername(String username);
}