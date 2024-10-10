package com.studentinfo.services;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.AttendanceRepository;
import com.studentinfo.data.repository.CourseRepository;
import com.studentinfo.data.repository.StudentRepository;
import com.studentinfo.data.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional // Ensures all public methods are transactional
public class TeacherService {

    // Repositories
    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, AttendanceRepository attendanceRepository,
                          StudentRepository studentRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    // CRUD Operations for Teacher

    // Retrieve a teacher by their ID
    public Optional<Teacher> get(Long id) {
        return teacherRepository.findById(id);
    }

    // Retrieve a teacher by their username
    public Optional<Teacher> getTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username);
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

    // Retrieval Operations for Teacher

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

    // Retrieve a teacher by their username with courses
    public Optional<Teacher> getTeacherByUsernameWithCourses(String username) {
        return teacherRepository.findTeacherByUsernameWithCourses(username);
    }

    // Operations for Attendance

    // Save or update an attendance record
    public Attendance saveAttendanceRecord(Attendance record) {
        return attendanceRepository.save(record);
    }

    // Retrieve all attendance records
    public List<Attendance> getAttendanceRecords() {
        return attendanceRepository.findAll();
    }

    // Retrieve attendance records for the courses taught by the teacher
    public List<Attendance> getAttendanceRecordsForTeacher(Long teacherId) {
        List<Course> teacherCourses = getCoursesForTeacher(teacherId);
        return attendanceRepository.findByCourseIn(teacherCourses);
    }

    // Delete an attendance record
    public void deleteAttendanceRecord(Attendance record) {
        attendanceRepository.delete(record);
    }

    // Operations for Courses

    // Retrieve courses taught by a teacher
    public List<Course> getCoursesForTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findTeacherWithCourses(teacherId);
        if (teacher != null) {
            return teacher.getCourses();
        }
        return Collections.emptyList();
    }

    // Retrieve a list of teachers by their IDs
    public List<Teacher> listByIds(List<Long> ids) {
        return teacherRepository.findAllById(ids);
    }

    // Retrieve all teachers from the database
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Operations for Students

    // Retrieve all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

}
