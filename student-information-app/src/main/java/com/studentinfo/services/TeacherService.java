package com.studentinfo.services;

import com.studentinfo.data.entity.*;
import com.studentinfo.data.repository.AttendanceRepository;
import com.studentinfo.data.repository.CourseRepository;
import com.studentinfo.data.repository.StudentRepository;
import com.studentinfo.data.repository.TeacherRepository;
import com.studentinfo.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository; // Add UserRepository

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, AttendanceRepository attendanceRepository,
                          StudentRepository studentRepository, CourseRepository courseRepository,
                          UserRepository userRepository) { // Add UserRepository to constructor
        this.teacherRepository = teacherRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository; // Initialize UserRepository
    }

    @Transactional
    public Teacher save(Teacher teacher) {
        Optional<Teacher> existingTeacher = teacherRepository.findByUsername(teacher.getUsername());

        if (existingTeacher.isPresent()) {
            teacher.setId(existingTeacher.get().getId());
        } else {
            Optional<User> existingUser = userRepository.findByUsername(teacher.getUsername());
            if (existingUser.isPresent()) {
                throw new DataIntegrityViolationException("A user with this username already exists.");
            }
        }

        return teacherRepository.saveAndFlush(teacher);
    }



    public Optional<Teacher> get(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username);
    }

    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    public List<Teacher> list() {
        return teacherRepository.findAll();
    }

    // Retrieval Operations for Teacher

    public List<Teacher> findByDepartment(String departmentName) {
        return teacherRepository.findByDepartment_Name(departmentName);
    }

    public List<Teacher> findBySubject(String subjectName) {
        return teacherRepository.findBySubject_Name(subjectName);
    }

    public List<Teacher> findByDepartmentAndSubject(String departmentName, String subjectName) {
        return teacherRepository.findByDepartment_NameAndSubject_Name(departmentName, subjectName);
    }

    public Optional<Teacher> getTeacherByUsernameWithCourses(String username) {
        return teacherRepository.findTeacherByUsernameWithCourses(username);
    }

    // Operations for Attendance

    public Attendance saveAttendanceRecord(Attendance attendanceRecord) {
        return attendanceRepository.save(attendanceRecord);
    }


    public List<Attendance> getAttendanceRecords() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getAttendanceRecordsForTeacher(Long teacherId) {
        List<Course> teacherCourses = getCoursesForTeacher(teacherId);

        // Using logger instead of System.out
        logger.info("Teacher ID: {} is teaching {} courses.", teacherId, teacherCourses.size());

        List<Attendance> attendanceRecords = attendanceRepository.findByCourseIn(teacherCourses);

        logger.info("Attendance records retrieved for teacher ID {}: {}", teacherId, attendanceRecords.size());

        return attendanceRecords;
    }

    public void deleteAttendanceRecord(Attendance attendanceRecord) {
        attendanceRepository.delete(attendanceRecord);
    }

    // Operations for Courses

    public List<Course> getCoursesForTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findTeacherWithCourses(teacherId);
        if (teacher != null) {
            return teacher.getCourses();
        }
        return Collections.emptyList();
    }

    public List<Teacher> listByIds(List<Long> ids) {
        return teacherRepository.findAllById(ids);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Operations for Students

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
