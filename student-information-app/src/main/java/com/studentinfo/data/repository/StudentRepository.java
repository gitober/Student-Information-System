package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    // Find a student by username
    Student findByUsername(String username);

    // Find students enrolled in a specific course (by course ID)
    List<Student> findByCourses_CourseId(Long courseId);

    // Find a student by studentNumber
    Optional<Student> findByStudentNumber(Long studentNumber);
}
