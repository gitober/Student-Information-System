package com.studentinfo.services;

import com.studentinfo.data.entity.Subject;
import com.studentinfo.data.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    // Repository
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    // CRUD Operations

    // Retrieve all subjects
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    // Additional Operations

    // Find a default subject, creating one if none exists
    public Subject findDefaultSubject() {
        Optional<Subject> defaultSubject = subjectRepository.findByName("Default Subject");
        return defaultSubject.orElseGet(() -> {
            Subject newSubject = new Subject("Default Subject", null); // Assign null or a default department
            return subjectRepository.save(newSubject);
        });
    }
}
