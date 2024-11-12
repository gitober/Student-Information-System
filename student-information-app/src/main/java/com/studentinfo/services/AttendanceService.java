package com.studentinfo.services;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AttendanceService {

    // Repository for managing attendance data
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // CRUD Operations

    // Retrieve all attendance records from the database
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // Retrieve a single attendance record by its ID
    public Optional<Attendance> getAttendanceById(Long attendanceId) {
        return attendanceRepository.findById(attendanceId);
    }

    // Create a new attendance record in the database
    public Attendance createAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    // Update an existing attendance record by its ID
    public Attendance updateAttendance(Long attendanceId, Attendance attendance) {
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new NoSuchElementException("Attendance record with ID " + attendanceId + " not found");
        }
        attendance.setAttendanceId(attendanceId); // Ensure the ID is correct
        return attendanceRepository.save(attendance);
    }

    // Delete an attendance record by its ID
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

}
