package com.studentinfo.services;

import com.studentinfo.data.entity.*;
import com.studentinfo.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final TranslationService translationService; // Inject TranslationService

    @Autowired
    public CourseService(StudentRepository studentRepository, CourseRepository courseRepository,
                         RegistrationRepository registrationRepository, TeacherRepository teacherRepository,
                         TranslationService translationService) { // Add TranslationService to constructor
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.translationService = translationService; // Initialize TranslationService
    }

    @Transactional
    public Course saveCourse(Course course, List<Teacher> teachers, List<CourseTranslation> translations) {
        // Save the course entity
        Course savedCourse = courseRepository.save(course);

        // Set up the relationship with teachers
        for (Teacher teacher : teachers) {
            Teacher persistentTeacher = teacherRepository.findById(teacher.getTeacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacher.getTeacherId()));
            if (!persistentTeacher.getCourses().contains(savedCourse)) {
                persistentTeacher.getCourses().add(savedCourse);
                teacherRepository.save(persistentTeacher); // Save each teacher with the new course relationship
            }
        }

        // Set the course reference in each translation and save them through TranslationService
        for (CourseTranslation translation : translations) {
            translation.setCourse(savedCourse); // Associate translation with the saved course
        }
        translationService.saveCourseTranslations(translations); // Save translations through TranslationService

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

    // Retrieve all available courses (same as getAllCourses, can be adjusted or removed)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAll();
    }


    // Retrieve students enrolled in a course by course ID (duplicate method, can be combined or removed)
    public List<Student> getEnrolledStudentsByCourseId(Long courseId) {
        return registrationRepository.findStudentsByCourseId(courseId);
    }


    // Method to get translated course name
    public String getTranslatedName(Long courseId, String languageCode) {
        return getTranslation(courseId, "course_name", languageCode);
    }

    // Method to get translated course plan
    public String getTranslatedPlan(Long courseId, String languageCode) {
        return getTranslation(courseId, "course_plan", languageCode);
    }

    // Helper method to fetch translation based on field name and language code
    private String getTranslation(Long courseId, String fieldName, String languageCode) {
        Optional<CourseTranslation> translation = translationService.getCourseTranslations(courseId, languageCode).stream()
                .filter(t -> t.getFieldName().equals(fieldName))
                .findFirst();
        return translation.map(CourseTranslation::getTranslatedValue).orElse(""); // Return translation or empty string
    }
}
