package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "teacher_translation")
public class TeacherTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "locale", nullable = false, length = 5)
    private String locale;

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "translated_value", nullable = false, length = 255)
    private String translatedValue;

    // Default constructor
    public TeacherTranslation() {}

    // Constructor with fields
    public TeacherTranslation(String locale, String fieldName, String translatedValue) {
        this.locale = locale;
        this.fieldName = fieldName;
        this.translatedValue = translatedValue;
    }
}
