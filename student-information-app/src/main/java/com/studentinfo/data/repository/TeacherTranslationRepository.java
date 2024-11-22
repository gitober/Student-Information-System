package com.studentinfo.data.repository;

import com.studentinfo.data.entity.TeacherTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherTranslationRepository extends JpaRepository<TeacherTranslation, Long> {

    // Retrieve translations by teacher ID and locale
    List<TeacherTranslation> findByTeacherIdAndLocale(Long teacherId, String locale);

    // Retrieve a specific translation by teacher ID, locale, and field name
    List<TeacherTranslation> findByLocale(String locale);
}
