package com.studentinfo.services;

import com.studentinfo.data.dto.TeacherDTO;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public Optional<Teacher> update(Long id, Teacher updatedTeacher) {
        return teacherRepository.findById(id)
                .map(existingTeacher -> {
                    existingTeacher.setFirstName(updatedTeacher.getFirstName());
                    existingTeacher.setLastName(updatedTeacher.getLastName());
                    existingTeacher.setDepartment(updatedTeacher.getDepartment());
                    existingTeacher.setSubject(updatedTeacher.getSubject());
                    existingTeacher.setPhoneNumber(updatedTeacher.getPhoneNumber());
                    existingTeacher.setEmail(updatedTeacher.getEmail());
                    return teacherRepository.save(existingTeacher);
                });
    }

    public Optional<Teacher> updateTeacherProfile(TeacherDTO teacherDTO) {
        return teacherRepository.findById(teacherDTO.getId())
                .map(existingTeacher -> {
                    existingTeacher.setName(teacherDTO.getName()); // Update name using getName() from UserDTO
                    existingTeacher.setDepartment(teacherDTO.getDepartment());
                    existingTeacher.setSubject(teacherDTO.getSubject());
                    existingTeacher.setPhoneNumber(teacherDTO.getPhoneNumber());
                    existingTeacher.setEmail(teacherDTO.getEmail());
                    return teacherRepository.save(existingTeacher);
                });
    }
}
