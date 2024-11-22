package com.studentinfo.services;

import com.studentinfo.data.entity.*;
import com.studentinfo.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslationService {

    private final CourseTranslationRepository courseTranslationRepository;
    private final DepartmentTranslationRepository departmentTranslationRepository;
    private final SubjectTranslationRepository subjectTranslationRepository;
    private final UserTranslationRepository userTranslationRepository;

    @Autowired
    public TranslationService(
            CourseTranslationRepository courseTranslationRepository,
            DepartmentTranslationRepository departmentTranslationRepository,
            SubjectTranslationRepository subjectTranslationRepository,
            TeacherTranslationRepository teacherTranslationRepository,
            UserTranslationRepository userTranslationRepository) {
        this.courseTranslationRepository = courseTranslationRepository;
        this.departmentTranslationRepository = departmentTranslationRepository;
        this.subjectTranslationRepository = subjectTranslationRepository;
        this.userTranslationRepository = userTranslationRepository;
    }

    // User Translations
    public List<UserTranslation> getUserTranslationsByLocale(Language locale) {
        return userTranslationRepository.findByLocale(locale);
    }

    public List<UserTranslation> getUserTranslations(Long userId, Language locale) {
        return userTranslationRepository.findByUser_IdAndLocale(userId, locale);
    }

    public void saveUserTranslations(List<UserTranslation> translations) {
        userTranslationRepository.saveAll(translations);
    }

    // Course Translations
    public List<CourseTranslation> getCourseTranslationsByLocale(String locale) {
        return courseTranslationRepository.findByLocale(locale);
    }

    public List<CourseTranslation> getCourseTranslations(Long courseId, String locale) {
        return courseTranslationRepository.findByCourse_CourseIdAndLocale(courseId, locale);
    }

    // Department Translations
    public List<DepartmentTranslation> getDepartmentTranslationsByLocale(String locale) {
        return departmentTranslationRepository.findByLocale(locale);
    }

    public List<SubjectTranslation> getSubjectTranslations(Long subjectId, String locale) {
        return subjectTranslationRepository.findBySubjectIdAndLocale(subjectId, locale);
    }

    public void saveCourseTranslations(List<CourseTranslation> translations) {
        courseTranslationRepository.saveAll(translations);
    }
}
