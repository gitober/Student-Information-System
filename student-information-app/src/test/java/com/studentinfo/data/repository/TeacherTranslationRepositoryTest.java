package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
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

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Create and save a Department instance
        Department department = new Department("Science");
        department = departmentRepository.saveAndFlush(department); // Save and flush to ensure it exists in the database

        // Create and save a Subject instance (make sure it's linked to the Department)
        Subject subject = new Subject("Mathematics", department);  // Set a subject for the teacher
        subject = subjectRepository.saveAndFlush(subject);  // Save and flush to ensure it exists in the database

        // Create and save a Teacher instance with all required fields
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john.doe@example.com"); // Ensure the email is set
        teacher.setUsername("johndoe");           // Ensure the username is set
        teacher.setUserType("TEACHER");           // Set the appropriate user type
        teacher.setDepartment(department);        // Set the department
        teacher.setSubject(subject);              // Set the subject (this is the key change)

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
