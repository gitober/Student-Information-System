package com.studentinfo.services;

import com.studentinfo.entity.Teacher;
import com.studentinfo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher registerTeacher(Teacher teacher) {
        // Perform any additional logic specific to teachers here
        return teacherRepository.save(teacher);
    }

    // Add other teacher-specific operations if needed
}
