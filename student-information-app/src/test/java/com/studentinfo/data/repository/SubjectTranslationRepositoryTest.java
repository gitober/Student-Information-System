package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
import com.studentinfo.data.entity.SubjectTranslation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
class SubjectTranslationRepositoryTest {

    @Autowired
    private SubjectTranslationRepository subjectTranslationRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository; // Ensure this is available

    private Long subjectId; // Class-level variable

    @BeforeEach
    void setUp() {
        // Create and save a Department instance
        Department department = new Department();
        department.setDepartmentName("Test Department");
        department = departmentRepository.saveAndFlush(department);

        // Create and save a Subject instance
        Subject subject = new Subject();
        subject.setName("Sample Subject");
        subject.setDepartment(department);
        subject = subjectRepository.saveAndFlush(subject);

        // Capture the subject ID
        subjectId = subject.getId();

        // Create and save SubjectTranslation instances
        SubjectTranslation subjectTranslation1 = new SubjectTranslation("EN", "subjectName", "Translated Subject 1");
        subjectTranslation1.setSubject(subject);
        subjectTranslationRepository.saveAndFlush(subjectTranslation1);

        SubjectTranslation subjectTranslation2 = new SubjectTranslation("FI", "subjectName", "Käännetty aihe 1");
        subjectTranslation2.setSubject(subject);
        subjectTranslationRepository.saveAndFlush(subjectTranslation2);
    }

    @AfterEach
    void tearDown() {
        subjectTranslationRepository.deleteAll();
        subjectRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void testFindBySubjectIdAndLocale() {
        List<SubjectTranslation> translations = subjectTranslationRepository.findBySubjectIdAndLocale(subjectId, "EN");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Translated Subject 1", translations.get(0).getTranslatedValue());
    }

    @Test
    void testFindByLocale() {
        List<SubjectTranslation> translations = subjectTranslationRepository.findByLocale("FI");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Käännetty aihe 1", translations.get(0).getTranslatedValue());
    }
}
