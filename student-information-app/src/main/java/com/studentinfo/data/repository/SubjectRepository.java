package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // Add the findByName method to find a subject by its name
    Optional<Subject> findByName(String name);
}