package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom query to find courses by name
    List<Course> findByCourseName(String courseName);

    // Custom query to find courses by duration
    List<Course> findByDuration(int duration);
}
