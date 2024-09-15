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

    public Optional<Teacher> get(Long id) {
        return teacherRepository.findById(id);
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    public List<Teacher> list() {
        return teacherRepository.findAll();
    }
}