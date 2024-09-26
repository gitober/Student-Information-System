package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom query to find courses by name
    List<Course> findByCourseName(String courseName);

    // Custom query to find courses by duration
    List<Course> findByDuration(int duration);

    // Find courses taught by a specific teacher
    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Course> findByTeacherId(Long teacherId);

    // Custom query to find courses by student ID
    @Query("SELECT c FROM Course c JOIN Registration r ON c.courseId = r.courseId WHERE r.studentNumber = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

}
