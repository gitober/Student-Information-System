package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    // Find a teacher by username, assuming you have a unique username field inherited from User
    Teacher findByUsername(String username);

    // Find teachers by department
    List<Teacher> findByDepartment(String department);

    // Find teachers by subject
    List<Teacher> findBySubject(String subject);

    // Find teachers by both department and subject
    List<Teacher> findByDepartmentAndSubject(String department, String subject);
}
