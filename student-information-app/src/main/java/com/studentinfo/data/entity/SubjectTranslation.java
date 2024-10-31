package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "subject_translation")
public class SubjectTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "locale", nullable = false, length = 5)
    private String locale;

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "translated_value", nullable = false, length = 255)
    private String translatedValue;

    // Default constructor
    public SubjectTranslation() {}

    // Constructor with fields
    public SubjectTranslation(String locale, String fieldName, String translatedValue) {
        this.locale = locale;
        this.fieldName = fieldName;
        this.translatedValue = translatedValue;
    }
}
