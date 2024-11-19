package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Custom query to find records by student number and course ID
    List<Attendance> findByStudent_StudentNumberAndCourse_CourseId(Long studentNumber, Long courseId);

    // Find attendance records by course ID
    List<Attendance> findByCourse_CourseId(Long courseId);

    // Find attendance records by course name
    List<Attendance> findByCourseIn(List<Course> courses);


}
