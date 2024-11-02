package com.studentinfo.data.repository;

import com.studentinfo.data.entity.DepartmentTranslation;
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
class DepartmentTranslationRepositoryTest {

    @Autowired
    private DepartmentTranslationRepository departmentTranslationRepository;

    private DepartmentTranslation departmentTranslation1;
    private DepartmentTranslation departmentTranslation2;

    @BeforeEach
    void setUp() {
        // Create and save DepartmentTranslation instances
        departmentTranslation1 = new DepartmentTranslation(1L, "EN", "departmentName", "Translated Department 1");
        departmentTranslation1.setId(1L);
        departmentTranslationRepository.saveAndFlush(departmentTranslation1);

        departmentTranslation2 = new DepartmentTranslation(1L, "FI", "departmentName", "Käännetty osasto 1");
        departmentTranslation2.setId(2L);
        departmentTranslationRepository.saveAndFlush(departmentTranslation2);
    }

    @AfterEach
    void tearDown() {
        departmentTranslationRepository.deleteAll();
    }

    @Test
    void testFindByDepartmentIdAndLocale() {
        List<DepartmentTranslation> translations = departmentTranslationRepository.findByDepartmentIdAndLocale(1L, "EN");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Translated Department 1", translations.get(0).getTranslatedValue());
    }

    @Test
    void testFindByLocale() {
        List<DepartmentTranslation> translations = departmentTranslationRepository.findByLocale("FI");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Käännetty osasto 1", translations.get(0).getTranslatedValue());
    }
}
