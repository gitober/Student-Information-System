package com.studentinfo.services;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Registration;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.repository.CourseRepository;
import com.studentinfo.data.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    // Repositories
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, RegistrationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
    }

    // CRUD Operations

    // Create or Update a Course
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    // Update an existing Course
    public Course updateCourse(Long courseId, Course course) {
        if (!courseRepository.existsById(courseId)) {
            return null; // Return null if course doesn't exist
        }
        course.setCourseId(courseId); // Ensure the ID is set to update the correct course
        return courseRepository.save(course);
    }

    // Get All Courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get Course by ID
    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    // Delete a Course by ID
    public boolean deleteCourse(Long courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return true; // Return true if deletion was successful
        }
        return false; // Return false if course not found
    }

    // Enrollment and Registration Operations

    // Enroll a student in a Course
    public void enrollInCourse(Long studentNumber, Long batchId, Long courseId, double coursePayment) {
        Registration registration = new Registration();
        registration.setStudentNumber(studentNumber);
        registration.setBatchId(batchId); // This can be null now
        registration.setCourseId(courseId);
        registration.setCoursePayment(coursePayment);
        registration.setRegistrationDay(LocalDate.now());

        registrationRepository.save(registration);
    }

    // Get Enrolled Courses for a Student by student number
    public List<Course> getEnrolledCourses(Long studentNumber) {
        return registrationRepository.findCoursesByStudentNumber(studentNumber);
    }

    // Get Enrolled Students for a Course by course ID
    public List<Student> getEnrolledStudents(Long courseId) {
        return registrationRepository.findStudentsByCourseId(courseId);
    }

    // Additional Operations

    // Get Available Courses
    public List<Course> getAvailableCourses() {
        return courseRepository.findAll();
    }

    // Get Courses for a specific student by student ID
    public List<Course> getCoursesForStudent(Long studentId) {
        return courseRepository.findCoursesByStudentId(studentId);
    }

    // Get Courses by Teacher ID (Retrieve students enrolled in a course)
    public List<Student> getEnrolledStudentsByCourseId(Long courseId) {
        return registrationRepository.findStudentsByCourseId(courseId);
    }
}
