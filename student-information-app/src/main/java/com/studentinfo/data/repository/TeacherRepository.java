package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    // Find a teacher by username, assuming you have a unique username field inherited from User
    Teacher findByUsername(String username);

    // Find teachers by department name
    List<Teacher> findByDepartment_Name(String departmentName);

    // Find teachers by subject name
    List<Teacher> findBySubject_Name(String subjectName);

    // Find teachers by both department name and subject name
    List<Teacher> findByDepartment_NameAndSubject_Name(String departmentName, String subjectName);
}
