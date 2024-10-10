package com.studentinfo.services;

import com.studentinfo.data.entity.*;
import com.studentinfo.data.repository.CourseRepository;
import com.studentinfo.data.repository.RegistrationRepository;
import com.studentinfo.data.repository.StudentRepository;
import com.studentinfo.data.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    // Repositories for interacting with the database
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CourseService(StudentRepository studentRepository, CourseRepository courseRepository,
                         RegistrationRepository registrationRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    // CRUD Operations

    // Create or update a Course with Teachers
    @Transactional
    public Course saveCourse(Course course, List<Teacher> teachers) {
        // Save the course
        Course savedCourse = courseRepository.save(course);

        // Add the course to each teacher's list of courses and save the relationship
        for (Teacher teacher : teachers) {
            Teacher persistentTeacher = teacherRepository.findById(teacher.getTeacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacher.getTeacherId()));
            if (!persistentTeacher.getCourses().contains(savedCourse)) {
                persistentTeacher.getCourses().add(savedCourse);
            }
            teacherRepository.save(persistentTeacher); // Persist the relationship
        }

        return savedCourse;
    }

    // Update an existing Course with Teachers
    public Course updateCourse(Long courseId, Course course, List<Teacher> teachers) {
        if (!courseRepository.existsById(courseId)) {
            return null; // Return null if course doesn't exist
        }
        course.setCourseId(courseId); // Ensure ID is set for the correct course
        course.setTeachers(teachers); // Set the list of teachers
        return courseRepository.save(course); // Save the updated course
    }

    // Get all Courses from the database
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Retrieve a Course by its ID
    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    // Delete a Course by its ID
    public boolean deleteCourse(Long courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return true; // Successful deletion
        }
        return false; // Course not found
    }

    // Enrollment and Registration Operations

    // Enroll a student in a course
    public void enrollInCourse(Long studentNumber, Long batchId, Long courseId, double coursePayment) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with student number: " + studentNumber));

        // Create a new registration record
        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setBatchId(batchId);
        registration.setCourseId(courseId);
        registration.setCoursePayment(coursePayment);
        registration.setRegistrationDay(LocalDate.now());
        registration.setStudentNumber(student.getStudentNumber()); // Ensure student number is set
        registration.setUser(student); // Set user (assuming this is the intended behavior)

        // Save the registration
        registrationRepository.save(registration);
    }

    // Retrieve enrolled courses for a student by their student number
    public List<Course> getEnrolledCourses(Long studentNumber) {
        return registrationRepository.findCoursesByStudentNumber(studentNumber);
    }

    // Retrieve enrolled students for a course by its course ID
    public List<Student> getEnrolledStudents(Long courseId) {
        return registrationRepository.findStudentsByCourseId(courseId);
    }

    // Additional Operations

    // Retrieve all available courses (same as getAllCourses, can be adjusted or removed)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAll();
    }

    // Retrieve courses for a specific student by student ID
    public List<Course> getCoursesForStudent(Long studentId) {
        return courseRepository.findCoursesByStudentId(studentId);
    }

    // Retrieve students enrolled in a course by course ID (duplicate method, can be combined or removed)
    public List<Student> getEnrolledStudentsByCourseId(Long courseId) {
        return registrationRepository.findStudentsByCourseId(courseId);
    }

    // Retrieve courses taught by a specific teacher by their teacher ID
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));
        return teacher.getCourses(); // Return the list of courses taught by the teacher
    }

}
