package com.FSD.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.AccountActivityEntity;

@Repository
public interface AccountActivityRepository extends JpaRepository<AccountActivityEntity, Integer> {
    List<AccountActivityEntity> findByCredential_UserId(Integer userId);
}