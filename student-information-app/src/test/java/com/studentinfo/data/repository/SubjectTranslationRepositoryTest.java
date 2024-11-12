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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
class SubjectTranslationRepositoryTest {

    @Autowired
    private SubjectTranslationRepository subjectTranslationRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Long subjectId;

    @BeforeEach
    void setUp() {
        // Set up department
        Department department = new Department();
        department.setDepartmentName("Test Department");
        department = departmentRepository.saveAndFlush(department);

        // Set up subject
        Subject subject = new Subject();
        subject.setName("Sample Subject");
        subject.setDepartment(department);
        subject = subjectRepository.saveAndFlush(subject);
        subjectId = subject.getId();

        // Create and save SubjectTranslations
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
        assertEquals("EN", translations.get(0).getLocale());
        assertEquals(subjectId, translations.get(0).getSubject().getId());
    }

    @Test
    void testFindByLocale() {
        List<SubjectTranslation> translations = subjectTranslationRepository.findByLocale("FI");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Käännetty aihe 1", translations.get(0).getTranslatedValue());
        assertEquals("FI", translations.get(0).getLocale());
    }

    @Test
    void testFindBySubjectIdAndLocaleNoTranslation() {
        // Assuming subjectId is valid but no translation exists for 'DE' locale
        List<SubjectTranslation> translations = subjectTranslationRepository.findBySubjectIdAndLocale(subjectId, "DE");
        assertNotNull(translations);
        assertEquals(0, translations.size(), "Expected no translations for 'DE' locale");
    }

    @Test
    void testFindBySubjectIdAndLocaleWithInvalidSubjectId() {
        // Assuming subjectId is invalid (non-existent in the DB)
        Long invalidSubjectId = 9999L;
        List<SubjectTranslation> translations = subjectTranslationRepository.findBySubjectIdAndLocale(invalidSubjectId, "EN");
        assertNotNull(translations);
        assertEquals(0, translations.size(), "Expected no translations for an invalid subjectId");
    }

    @Test
    void testFindByLocaleWithMultipleResults() {
        // Add a second subject and translation to test if the query handles multiple subjects
        Department department = new Department();
        department.setDepartmentName("Test Department 2");
        department = departmentRepository.saveAndFlush(department);

        Subject subject2 = new Subject();
        subject2.setName("Another Subject");
        subject2.setDepartment(department);
        subject2 = subjectRepository.saveAndFlush(subject2);

        SubjectTranslation subjectTranslation1 = new SubjectTranslation("EN", "subjectName", "Translated Subject 2");
        subjectTranslation1.setSubject(subject2);
        subjectTranslationRepository.saveAndFlush(subjectTranslation1);

        List<SubjectTranslation> translations = subjectTranslationRepository.findByLocale("EN");
        assertNotNull(translations);
        assertTrue(translations.size() > 1, "Expected multiple translations for the 'EN' locale");
    }

    @Test
    void testFindByLocaleNull() {
        List<SubjectTranslation> translations = subjectTranslationRepository.findByLocale(null);
        assertNotNull(translations);
        assertEquals(0, translations.size(), "Expected no translations for null locale");
    }
}