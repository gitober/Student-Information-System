package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Add the findByName method to find a department by its name
    Optional<Department> findByName(String name);
}