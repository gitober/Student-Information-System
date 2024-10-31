package com.studentinfo.data.repository;

import com.studentinfo.data.entity.TeacherTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherTranslationRepository extends JpaRepository<TeacherTranslation, Long> {
    List<TeacherTranslation> findByTeacherIdAndLocale(Long teacherId, String locale); // Verify this method is defined
    List<TeacherTranslation> findByLocale(String locale);
}
