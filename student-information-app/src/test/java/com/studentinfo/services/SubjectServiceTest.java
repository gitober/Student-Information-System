package com.studentinfo.services;

import com.studentinfo.data.entity.Subject;
import com.studentinfo.data.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        Subject subject1 = new Subject("Mathematics", null);
        Subject subject2 = new Subject("Physics", null);
        List<Subject> subjects = Arrays.asList(subject1, subject2);

        when(subjectRepository.findAll()).thenReturn(subjects);

        // Act
        List<Subject> result = subjectService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Mathematics", result.get(0).getName());
        assertEquals("Physics", result.get(1).getName());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void testFindDefaultSubjectWhenPresent() {
        // Arrange
        Subject defaultSubject = new Subject("Default Subject", null);
        when(subjectRepository.findByName("Default Subject")).thenReturn(Optional.of(defaultSubject));

        // Act
        Subject result = subjectService.findDefaultSubject();

        // Assert
        assertNotNull(result);
        assertEquals("Default Subject", result.getName());
        verify(subjectRepository, times(1)).findByName("Default Subject");
        verify(subjectRepository, times(0)).save(any(Subject.class)); // Ensure save is not called
    }

    @Test
    void testFindDefaultSubjectWhenAbsent() {
        // Arrange
        when(subjectRepository.findByName("Default Subject")).thenReturn(Optional.empty());
        Subject newSubject = new Subject("Default Subject", null);
        when(subjectRepository.save(any(Subject.class))).thenReturn(newSubject);

        // Act
        Subject result = subjectService.findDefaultSubject();

        // Assert
        assertNotNull(result);
        assertEquals("Default Subject", result.getName());
        verify(subjectRepository, times(1)).findByName("Default Subject");
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }
}
