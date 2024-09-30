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
import java.util.stream.Collectors;

@Service
public class CourseService {

    // Repositories
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

    // Method to create or update a Course with Teachers
    @Transactional
    public Course saveCourse(Course course, List<Teacher> teachers) {
        // Save the course first
        Course savedCourse = courseRepository.save(course);

        // Load each teacher from the database and establish the relationship
        for (Teacher teacher : teachers) {
            Teacher persistentTeacher = teacherRepository.findById(teacher.getTeacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacher.getTeacherId()));

            // Add the course to the teacher's list of courses
            if (!persistentTeacher.getCourses().contains(savedCourse)) {
                persistentTeacher.getCourses().add(savedCourse);
            }

            // Save the teacher to persist the relationship
            teacherRepository.save(persistentTeacher);
        }

        return savedCourse;
    }

    // Update an existing Course with Teachers
    public Course updateCourse(Long courseId, Course course, List<Teacher> teachers) {
        if (!courseRepository.existsById(courseId)) {
            return null; // Return null if course doesn't exist
        }
        course.setCourseId(courseId); // Ensure the ID is set to update the correct course
        course.setTeachers(teachers); // Update the list of teachers
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

    public void enrollInCourse(Long studentNumber, Long batchId, Long courseId, double coursePayment) {
        // Find the student by student number
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with student number: " + studentNumber));

        // Create a new registration
        Registration registration = new Registration();
        registration.setStudent(student); // Set the student object directly
        registration.setBatchId(batchId);
        registration.setCourseId(courseId);
        registration.setCoursePayment(coursePayment);
        registration.setRegistrationDay(LocalDate.now());

        // Set student_number directly if needed
        registration.setStudentNumber(student.getStudentNumber()); // Ensure student number is set

        // Set the user in registration (user_id)
        registration.setUser(student); // Assuming you want to set user as well

        // Save the registration
        registrationRepository.save(registration);
    }



    // Get Enrolled Courses for a Student by student number
    public List<Course> getEnrolledCourses(Long studentNumber) {
        System.out.println("Debug: Fetching enrolled courses for student number: " + studentNumber);
        List<Course> courses = registrationRepository.findCoursesByStudentNumber(studentNumber);
        System.out.println("Debug: Enrolled courses retrieved: " + courses.size());
        return courses;
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
        List<Student> students = registrationRepository.findStudentsByCourseId(courseId);
        return students;
    }
}
