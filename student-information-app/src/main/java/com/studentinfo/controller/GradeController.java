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

    @GetMapping("/student/{studentNumber}")
    public ResponseEntity<List<Grade>> getGradesByStudentNumber(@PathVariable Long studentNumber) {
        List<Grade> grades = gradeService.getGradesByStudentNumber(studentNumber);
        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no grades found
        }
        return ResponseEntity.ok(grades); // Return 200 OK with the list of grades
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourseId(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourseId(courseId);
        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no grades found
        }
        return ResponseEntity.ok(grades); // Return 200 OK with the list of grades
    }

    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        Grade savedGrade = gradeService.saveGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade); // Return 201 Created with the saved grade
    }

    @PutMapping("/{gradeId}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Integer gradeId, @RequestBody Grade grade) {
        Grade updatedGrade = gradeService.updateGrade(gradeId, grade);
        if (updatedGrade == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no grade found for update
        }
        return ResponseEntity.ok(updatedGrade); // Return 200 OK with the updated grade
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer gradeId) {
        boolean wasDeleted = gradeService.deleteGrade(gradeId);
        if (!wasDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if grade not found
        }
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }
}
