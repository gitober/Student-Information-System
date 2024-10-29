package com.studentinfo.controller;

import com.studentinfo.data.dto.CourseCreationRequestDTO;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.CourseTranslation;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;

    @Autowired
    public CourseController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    // Create a new course with translations
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseCreationRequestDTO request) {
        Course course = request.getCourse();
        List<Long> teacherIds = request.getTeacherIds();
        List<CourseTranslation> translations = request.getTranslations();

        // Retrieve teachers using the provided IDs
        List<Teacher> teachers = teacherService.listByIds(teacherIds);

        // Check if all provided teacher IDs exist
        if (teachers.size() != teacherIds.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return 400 Bad Request if any teacher ID is invalid
        }

        // Save the course with the teachers and translations
        Course savedCourse = courseService.saveCourse(course, teachers, translations);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }


    // Get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses); // Return 200 OK with the list of courses
    }

    // Get course by ID
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return courseService.getCourseById(courseId)
                .map(ResponseEntity::ok) // Return 200 OK with the found course
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 Not Found if course not found
    }

    // Update course by ID
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @RequestBody Course course,
            @RequestParam List<Long> teacherIds) {

        // Retrieve teachers using the provided IDs
        List<Teacher> teachers = teacherService.listByIds(teacherIds);

        // Check if all provided teacher IDs exist
        if (teachers.size() != teacherIds.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return 400 Bad Request if any teacher ID is invalid
        }

        // Call the updated course service method
        Course updatedCourse = courseService.updateCourse(courseId, course, teachers);

        if (updatedCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if no course found for update
        }

        return ResponseEntity.ok(updatedCourse); // Return 200 OK with the updated course
    }

    // Delete course by ID
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        boolean wasDeleted = courseService.deleteCourse(courseId);
        if (!wasDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if course not found
        }
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }

    // Get translated course name by course ID and locale
    @GetMapping("/{courseId}/translated-name")
    public ResponseEntity<String> getTranslatedCourseName(
            @PathVariable Long courseId,
            @RequestParam String locale) {
        String translatedName = courseService.getTranslatedName(courseId, locale);
        return ResponseEntity.ok(translatedName);
    }

    // Get translated course plan by course ID and locale
    @GetMapping("/{courseId}/translated-plan")
    public ResponseEntity<String> getTranslatedCoursePlan(
            @PathVariable Long courseId,
            @RequestParam String locale) {
        String translatedPlan = courseService.getTranslatedPlan(courseId, locale);
        return ResponseEntity.ok(translatedPlan);
    }
}
