package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

    // Custom query to find grades by student number
    List<Grade> findByStudentNumber(Long studentNumber);

    // Custom query to find grades by course ID
    List<Grade> findByCourse_CourseId(Long courseId);
}
