package com.studentinfo.controller;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.list();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 if student not found
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent); // Return 201 Created
    }

    // Update student by ID
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Optional<Student> existingStudentOptional = studentService.get(id);

        if (existingStudentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if student not found
        }

        Student existingStudent = existingStudentOptional.get();

        // Update the necessary fields explicitly

        // Update first name
        if (student.getFirstName() != null) {
            existingStudent.setFirstName(student.getFirstName());
        }

        // Update last name
        if (student.getLastName() != null) {
            existingStudent.setLastName(student.getLastName());
        }

        // Update email
        if (student.getEmail() != null) {
            existingStudent.setEmail(student.getEmail());
        }

        // Update phone number
        if (student.getPhoneNumber() != null) {
            existingStudent.setPhoneNumber(student.getPhoneNumber());
        }

        // Update student class
        if (student.getStudentClass() != null) {
            existingStudent.setStudentClass(student.getStudentClass());
        }

        // Update grade
        if (student.getGrade() != null) {
            existingStudent.setGrade(student.getGrade());
        }

        // Save the updated student
        Student updatedStudent = studentService.save(existingStudent);

        return ResponseEntity.ok(updatedStudent); // Return 200 OK with the updated student
    }

    // Delete student by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Optional<Student> existingStudent = studentService.get(id);
        if (existingStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if student not found
        }
        studentService.delete(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }
}