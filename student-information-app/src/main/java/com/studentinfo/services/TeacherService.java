package com.studentinfo.services;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Ensures all public methods are transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    // Retrieve a teacher by their ID
    public Optional<Teacher> get(Long id) {
        return teacherRepository.findById(id);
    }

    // Save or update a teacher entity
    public Teacher save(Teacher teacher) {
        // Additional validation or processing can be added here if needed
        return teacherRepository.save(teacher);
    }

    // Delete a teacher by their ID
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    // Retrieve all teachers from the database
    public List<Teacher> list() {
        return teacherRepository.findAll();
    }

    // Find teachers by department name
    public List<Teacher> findByDepartment(String departmentName) {
        return teacherRepository.findByDepartment_Name(departmentName);
    }
}
