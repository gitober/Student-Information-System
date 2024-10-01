package com.studentinfo.services;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    // Repository
    @Autowired
    private AttendanceRepository attendanceRepository;

    // CRUD Operations

    // Retrieve all attendance records
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // Retrieve attendance by ID
    public Optional<Attendance> getAttendanceById(Long attendanceId) {
        return attendanceRepository.findById(attendanceId);
    }

    // Create a new attendance record
    public Attendance createAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    // Update an existing attendance record
    public Attendance updateAttendance(Long attendanceId, Attendance attendance) {
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new RuntimeException("Attendance record not found");
        }
        attendance.setAttendanceId(attendanceId);
        return attendanceRepository.save(attendance);
    }

    // Delete an attendance record
    public boolean deleteAttendance(Long attendanceId) {
        if (attendanceRepository.existsById(attendanceId)) {
            attendanceRepository.deleteById(attendanceId);
            return true; // Deletion was successful
        }
        return false; // Record not found, deletion not successful
    }

    // Additional Operations

    // Retrieve attendance records by course ID
    public List<Attendance> getAttendanceByCourseId(Long courseId) {
        return attendanceRepository.findByCourse_CourseId(courseId);
    }

    // Retrieve attendance records by student number and course ID
    public List<Attendance> getAttendanceByStudentNumberAndCourseId(Long studentNumber, Long courseId) {
        return attendanceRepository.findByStudent_StudentNumberAndCourse_CourseId(studentNumber, courseId);
    }

    public List<Attendance> findByStudent_StudentNumberAndCourse_CourseId(Long studentNumber, Long courseId) {
        return attendanceRepository.findByStudent_StudentNumberAndCourse_CourseId(studentNumber, courseId);
    }



}
