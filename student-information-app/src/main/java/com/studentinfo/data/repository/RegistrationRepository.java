package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Registration;
import com.studentinfo.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Custom query to find courses by student number
    @Query("SELECT c FROM Course c JOIN Registration r ON c.courseId = r.courseId WHERE r.studentNumber = :studentNumber")
    List<Course> findCoursesByStudentNumber(@Param("studentNumber") Long studentNumber);

    // Custom query to find students enrolled in a course by course ID
    @Query("SELECT r.student FROM Registration r WHERE r.courseId = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

}
