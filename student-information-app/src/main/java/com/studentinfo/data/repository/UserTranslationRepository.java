package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Language;
import com.studentinfo.data.entity.UserTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTranslationRepository extends JpaRepository<UserTranslation, Long> {

    // Retrieve translations by user ID and locale
    List<UserTranslation> findByUser_IdAndLocale(Long userId, Language locale);

    // Retrieve a specific translation by user ID, locale, and field name
    List<UserTranslation> findByLocale(Language locale);
}

