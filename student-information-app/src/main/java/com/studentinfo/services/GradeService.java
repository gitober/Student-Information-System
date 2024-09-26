package com.studentinfo.services;

import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    // Repository
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    // CRUD Operations

    // Save or create a new grade
    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    // Update an existing grade
    public Grade updateGrade(Integer gradeId, Grade grade) {
        if (!gradeRepository.existsById(gradeId)) {
            return null; // Return null if the grade doesn't exist
        }
        grade.setGradeId(gradeId); // Ensure the ID is set to update the correct grade
        return gradeRepository.save(grade);
    }

    // Delete a grade by ID
    public boolean deleteGrade(Integer gradeId) {
        if (gradeRepository.existsById(gradeId)) {
            gradeRepository.deleteById(gradeId);
            return true; // Return true if the deletion was successful
        }
        return false; // Return false if the grade was not found
    }

    // Retrieval Operations

    // Get grades by student number
    public List<Grade> getGradesByStudentNumber(Long studentNumber) {
        return gradeRepository.findByStudentNumber(studentNumber);
    }

    // Get grades by course ID
    public List<Grade> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourse_CourseId(courseId);
    }
}
