package com.studentinfo.services;

import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> getGradesByStudentNumber(Long studentNumber) {
        return gradeRepository.findByStudentNumber(studentNumber);
    }

    public List<Grade> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourse_CourseId(courseId);
    }

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public void deleteGrade(Integer gradeId) {
        gradeRepository.deleteById(gradeId);
    }
}
