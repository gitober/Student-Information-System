package com.studentinfo.services;

import com.studentinfo.data.dto.StudentDTO;
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

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Student> get(Long id) {
        return studentRepository.findById(id);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> list() {
        return studentRepository.findAll();
    }

    public Optional<Student> update(Long id, Student updatedStudent) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setFirstName(updatedStudent.getFirstName());
                    existingStudent.setLastName(updatedStudent.getLastName());
                    existingStudent.setGrade(updatedStudent.getGrade());
                    existingStudent.setStudentClass(updatedStudent.getStudentClass());
                    existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
                    existingStudent.setEmail(updatedStudent.getEmail());
                    return studentRepository.save(existingStudent);
                });
    }

    public Optional<Student> updateStudentProfile(StudentDTO studentDTO) {
        return studentRepository.findById(studentDTO.getId())
                .map(existingStudent -> {
                    existingStudent.setName(studentDTO.getName()); // Update name using getName() from UserDTO
                    existingStudent.setGrade(studentDTO.getGrade());
                    existingStudent.setStudentClass(studentDTO.getStudentClass());
                    existingStudent.setPhoneNumber(studentDTO.getPhoneNumber());
                    existingStudent.setEmail(studentDTO.getEmail());
                    return studentRepository.save(existingStudent);
                });
    }
}
