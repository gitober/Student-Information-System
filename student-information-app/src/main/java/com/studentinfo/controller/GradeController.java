package com.studentinfo.controller;

import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    // Get all grades
    @GetMapping("/student/{studentNumber}")
    public ResponseEntity<List<Grade>> getGradesByStudentNumber(@PathVariable Long studentNumber) {
        List<Grade> grades = gradeService.getGradesByStudentNumber(studentNumber);
        return ResponseEntity.ok(grades); // Return 200 OK with the list of grades (empty or not)
    }

    // Get grade by ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourseId(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourseId(courseId);
        return ResponseEntity.ok(grades); // Return 200 OK with the list of grades (empty or not)
    }

    // Create a new grade
    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        Grade savedGrade = gradeService.saveGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade); // Return 201 Created with the saved grade
    }

    // Update grade by ID
    @PutMapping("/{gradeId}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Integer gradeId, @RequestBody Grade grade) {
        Grade updatedGrade = gradeService.updateGrade(gradeId, grade); // Directly get the Grade object
        if (updatedGrade == null) {  // If the grade is not found or updated, return 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedGrade); // Return 200 OK with the updated grade
    }

    // Delete grade by ID
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer gradeId) {
        boolean wasDeleted = gradeService.deleteGrade(gradeId);
        if (!wasDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if grade not found
        }
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }
}
