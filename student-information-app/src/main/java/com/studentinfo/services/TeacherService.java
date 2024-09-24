package com.studentinfo.services;

import com.studentinfo.data.entity.AttendanceRecord; // Import your AttendanceRecord entity
import com.studentinfo.data.repository.AttendanceRecordRepository; // Import the repository for AttendanceRecord
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Ensures all public methods are transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final AttendanceRecordRepository attendanceRecordRepository; // Add this line

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, AttendanceRecordRepository attendanceRecordRepository) {
        this.teacherRepository = teacherRepository;
        this.attendanceRecordRepository = attendanceRecordRepository; // Initialize the repository
    }

    // Retrieve a teacher by their ID
    public Optional<Teacher> get(Long id) {
        return teacherRepository.findById(id);
    }

    // Save or update a teacher entity
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    // Delete a teacher by their ID
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    // Retrieve all teachers from the database
    public List<Teacher> list() {
        return teacherRepository.findAll();
    }

    // Find teachers by department name
    public List<Teacher> findByDepartment(String departmentName) {
        return teacherRepository.findByDepartment_Name(departmentName);
    }

    // Find teachers by subject name
    public List<Teacher> findBySubject(String subjectName) {
        return teacherRepository.findBySubject_Name(subjectName);
    }

    // Find teachers by both department name and subject name
    public List<Teacher> findByDepartmentAndSubject(String departmentName, String subjectName) {
        return teacherRepository.findByDepartment_NameAndSubject_Name(departmentName, subjectName);
    }

    // Save or update an attendance record
    public AttendanceRecord saveAttendanceRecord(AttendanceRecord record) {
        return attendanceRecordRepository.save(record);
    }

    // Retrieve all attendance records
    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecordRepository.findAll();
    }

    // Delete an attendance record
    public void deleteAttendanceRecord(AttendanceRecord record) {
        attendanceRecordRepository.delete(record);
    }
}
