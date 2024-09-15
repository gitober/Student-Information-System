package com.studentinfo.services;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    // Method to find a default department, creating one if none exists
    public Department findDefaultDepartment() {
        Optional<Department> defaultDepartment = departmentRepository.findByName("Default Department");
        return defaultDepartment.orElseGet(() -> {
            Department newDepartment = new Department("Default Department");
            return departmentRepository.save(newDepartment);
        });
    }
}
