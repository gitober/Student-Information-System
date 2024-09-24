package com.studentinfo.data.repository;

import com.studentinfo.data.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    // Custom query to find records by student ID and course ID
    List<AttendanceRecord> findByStudent_StudentNumberAndCourse_CourseId(Long studentNumber, Long courseId);

    // Find attendance records by course ID
    List<AttendanceRecord> findByCourse_CourseId(Long courseId);

    // Find attendance records by student ID
    List<AttendanceRecord> findByStudent_Id(Long studentId);
}
