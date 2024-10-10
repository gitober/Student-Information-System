package com.studentinfo.controller;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Get all teachers
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.list();
        return ResponseEntity.ok(teachers); // Return 200 OK with the list of teachers
    }

    // Get teacher by ID
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return teacherService.get(id)
                .map(ResponseEntity::ok) // Return 200 OK with the found teacher
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 Not Found if teacher not found
    }

    // Create a new teacher
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher createdTeacher = teacherService.save(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher); // Return 201 Created with the saved teacher
    }

    // Update teacher by ID
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Optional<Teacher> existingTeacherOptional = teacherService.get(id);

        if (existingTeacherOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if teacher not found
        }

        Teacher existingTeacher = existingTeacherOptional.get();

        // Update the necessary fields explicitly

        // Update username
        if (teacher.getUsername() != null) {
            existingTeacher.setUsername(teacher.getUsername());
        }

        // Update email
        if (teacher.getEmail() != null) {
            existingTeacher.setEmail(teacher.getEmail());
        }

        // Update first name
        if (teacher.getFirstName() != null) {
            existingTeacher.setFirstName(teacher.getFirstName());
        }

        // Update last name
        if (teacher.getLastName() != null) {
            existingTeacher.setLastName(teacher.getLastName());
        }

        // Update phone number
        if (teacher.getPhoneNumber() != null) {
            existingTeacher.setPhoneNumber(teacher.getPhoneNumber());
        }

        // Save the updated teacher
        Teacher updatedTeacher = teacherService.save(existingTeacher);

        return ResponseEntity.ok(updatedTeacher); // Return 200 OK with the updated teacher
    }

    // Delete teacher by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> existingTeacher = teacherService.get(id);
        if (existingTeacher.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if teacher not found
        }
        teacherService.delete(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }
}
