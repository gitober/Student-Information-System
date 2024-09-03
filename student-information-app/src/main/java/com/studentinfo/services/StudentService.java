package com.studentinfo.services;

import com.studentinfo.entity.Student;
import com.studentinfo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student registerStudent(Student student) {
        // Perform any additional logic specific to students here
        return studentRepository.save(student);
    }

    // Add other student-specific operations if needed
}
