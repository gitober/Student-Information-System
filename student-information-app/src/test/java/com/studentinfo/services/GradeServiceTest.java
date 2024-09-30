package com.studentinfo.services;

import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.repository.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGrades() {
        // Arrange
        when(gradeRepository.findAll()).thenReturn(List.of(new Grade()));

        // Act
        List<Grade> result = gradeService.getAllGrades();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    void testSaveGrade() {
        // Arrange
        Grade grade = new Grade();
        when(gradeRepository.save(grade)).thenReturn(grade);

        // Act
        Grade result = gradeService.saveGrade(grade);

        // Assert
        assertNotNull(result);
        verify(gradeRepository, times(1)).save(grade);
    }

    @Test
    void testUpdateGradeExisting() {
        // Arrange
        Integer gradeId = 1;
        Grade grade = new Grade();
        when(gradeRepository.existsById(gradeId)).thenReturn(true);
        when(gradeRepository.save(grade)).thenReturn(grade);

        // Act
        Grade result = gradeService.updateGrade(gradeId, grade);

        // Assert
        assertNotNull(result);
        verify(gradeRepository, times(1)).existsById(gradeId);
        verify(gradeRepository, times(1)).save(grade);
    }

    @Test
    void testUpdateGradeNonExisting() {
        // Arrange
        Integer gradeId = 1;
        Grade grade = new Grade();
        when(gradeRepository.existsById(gradeId)).thenReturn(false);

        // Act
        Grade result = gradeService.updateGrade(gradeId, grade);

        // Assert
        assertNull(result);
        verify(gradeRepository, times(1)).existsById(gradeId);
        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void testDeleteGrade() {
        // Arrange
        Integer gradeId = 1;
        when(gradeRepository.existsById(gradeId)).thenReturn(true);

        // Act
        boolean result = gradeService.deleteGrade(gradeId);

        // Assert
        assertTrue(result);
        verify(gradeRepository, times(1)).existsById(gradeId);
        verify(gradeRepository, times(1)).deleteById(gradeId);
    }

    @Test
    void testGetGradesByStudentNumber() {
        // Arrange
        Long studentNumber = 100L;
        when(gradeRepository.findByStudentNumber(studentNumber)).thenReturn(List.of(new Grade()));

        // Act
        List<Grade> result = gradeService.getGradesByStudentNumber(studentNumber);

        // Assert
        assertNotNull(result);
        verify(gradeRepository, times(1)).findByStudentNumber(studentNumber);
    }

    @Test
    void testGetGradesByCourseId() {
        // Arrange
        Long courseId = 200L;
        when(gradeRepository.findByCourse_CourseId(courseId)).thenReturn(List.of(new Grade()));

        // Act
        List<Grade> result = gradeService.getGradesByCourseId(courseId);

        // Assert
        assertNotNull(result);
        verify(gradeRepository, times(1)).findByCourse_CourseId(courseId);
    }
}
