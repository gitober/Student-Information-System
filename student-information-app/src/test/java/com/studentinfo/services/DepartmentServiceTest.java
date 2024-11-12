package com.studentinfo.services;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.repository.DepartmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(departmentRepository);
    }

    @Test
    void testFindDefaultDepartment() {
        Department defaultDepartment = new Department("Default Department");

        when(departmentRepository.findByName("Default Department")).thenReturn(Optional.of(defaultDepartment));

        Department result = departmentService.findDefaultDepartment();

        assertEquals(defaultDepartment, result);
        verify(departmentRepository, times(1)).findByName("Default Department");
    }

    @Test
    void testFindDefaultDepartmentNotFound() {
        Department defaultDepartment = new Department("Default Department");

        when(departmentRepository.findByName("Default Department")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenReturn(defaultDepartment);

        Department result = departmentService.findDefaultDepartment();

        assertEquals(defaultDepartment, result);
        verify(departmentRepository, times(1)).findByName("Default Department");
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

}