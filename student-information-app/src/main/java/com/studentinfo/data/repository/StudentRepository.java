package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    // Find a student by username
    Student findByUsername(String username);

    // Find students by their grade
    List<Student> findByGrade(String grade);

    // Find students by studentClass (matching the entity field name)
    List<Student> findByStudentClass(String studentClass);

    // Find students by both grade and studentClass
    List<Student> findByGradeAndStudentClass(String grade, String studentClass);

    // Find students enrolled in a specific course (by course ID)
    List<Student> findByCourses_CourseId(Long courseId); // This should now work after fixing the 'courses' mapping

    // Find a student by studentNumber
    Optional<Student> findByStudentNumber(Long studentNumber);
}
