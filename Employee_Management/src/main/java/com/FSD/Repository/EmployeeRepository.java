package com.FSD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    // Derived Query Method: Spring Data JPA creates the query automatically based on method name
    // Matches: GET /api/employees/name/{name}
    List<EmployeeEntity> findByEmployeeNameContainingIgnoreCase(String name);

    // Custom JPQL Query: To find employees who are Department Heads
    // Since departmentHeadRole is a relationship, we check if the inner join exists or is not null
    // Matches: GET /api/employees/with-department-head-role
    @Query("SELECT e FROM EmployeeEntity e JOIN e.departmentHeadRole d")
    List<EmployeeEntity> findEmployeesWithDepartmentHeadRole();
}