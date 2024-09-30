package com.studentinfo.controller;

import com.studentinfo.data.entity.Course;
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

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses); // Return 200 OK with the list of courses
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return courseService.getCourseById(courseId)
                .map(ResponseEntity::ok) // Return 200 OK with the found course
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 Not Found if course not found
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course, @RequestParam List<Long> teacherIds) {
        // Retrieve teachers using the provided IDs
        List<Teacher> teachers = teacherService.listByIds(teacherIds);

        // Check if all provided teacher IDs exist
        if (teachers.size() != teacherIds.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return 400 Bad Request if any teacher ID is invalid
        }

        // Save the course with the teachers
        Course savedCourse = courseService.saveCourse(course, teachers);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

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

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        boolean wasDeleted = courseService.deleteCourse(courseId);
        if (!wasDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if course not found
        }
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }
}
