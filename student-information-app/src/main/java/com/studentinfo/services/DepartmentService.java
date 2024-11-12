package com.studentinfo.services;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Retrieve all departments
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    // Find a default department, creating one if none exists
    public Department findDefaultDepartment() {
        Optional<Department> defaultDepartment = departmentRepository.findByName("Default Department");
        return defaultDepartment.orElseGet(() -> {
            Department newDepartment = new Department("Default Department");
            return departmentRepository.save(newDepartment);
        });
    }

    // Load a department with teachers and subjects initialized
    @Transactional
    public Optional<Department> findDepartmentWithRelations(Long departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        department.ifPresent(dept -> {
            // Initialize relationships
            Hibernate.initialize(dept.getTeachers());
            Hibernate.initialize(dept.getSubjects());
        });
        return department;
    }
}
