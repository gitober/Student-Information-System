package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    // Repository
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CRUD Operations

    // Save or update a student entity
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    // Retrieve a student by ID
    public Optional<Student> get(Long id) {
        return studentRepository.findById(id);
    }

    // Delete a student by ID
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    // Retrieve all students
    public List<Student> list() {
        return studentRepository.findAll();
    }

    // Additional Operations

    // Retrieve students by course ID
    public List<Student> getStudentsByCourseId(Long courseId) {
        return studentRepository.findByCourses_CourseId(courseId);
    }

    // Retrieve a student by their student number
    public Student getStudentByNumber(Long studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber).orElse(null);
    }
}
