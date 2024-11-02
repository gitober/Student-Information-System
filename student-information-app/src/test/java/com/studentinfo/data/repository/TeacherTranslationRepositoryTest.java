package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.entity.TeacherTranslation;
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
class TeacherTranslationRepositoryTest {

    @Autowired
    private TeacherTranslationRepository teacherTranslationRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Create and save a Teacher instance with all required fields
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john.doe@example.com"); // Ensure the email is set
        teacher.setUsername("johndoe");           // Ensure the username is set
        teacher.setUserType("TEACHER");           // Set the appropriate user type
        // Set other required fields if necessary

        teacher = teacherRepository.saveAndFlush(teacher); // Save and flush to ensure it exists in the database

        // Create and save TeacherTranslation instances
        TeacherTranslation translation1 = new TeacherTranslation("EN", "teacherName", "Translated Name 1");
        translation1.setTeacher(teacher);
        teacherTranslationRepository.saveAndFlush(translation1);

        TeacherTranslation translation2 = new TeacherTranslation("FI", "teacherName", "Käännetty nimi 1");
        translation2.setTeacher(teacher);
        teacherTranslationRepository.saveAndFlush(translation2);
    }


    @AfterEach
    void tearDown() {
        teacherTranslationRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void testFindByTeacherIdAndLocale() {
        List<TeacherTranslation> translations = teacherTranslationRepository.findByTeacherIdAndLocale(teacher.getId(), "EN");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Translated Name 1", translations.get(0).getTranslatedValue());
    }

    @Test
    void testFindByLocale() {
        List<TeacherTranslation> translations = teacherTranslationRepository.findByLocale("FI");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Käännetty nimi 1", translations.get(0).getTranslatedValue());
    }
}
