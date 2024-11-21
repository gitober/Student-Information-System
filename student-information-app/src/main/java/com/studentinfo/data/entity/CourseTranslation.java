package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "course_translation")
public class CourseTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Make sure this is present

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "locale", nullable = false, length = 5)
    private String locale;

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "translated_value", nullable = false)
    private String translatedValue;

    // Default constructor
    public CourseTranslation() {}

    // Constructor with fields
    public CourseTranslation(String locale, String fieldName, String translatedValue) {
        this.locale = locale;
        this.fieldName = fieldName;
        this.translatedValue = translatedValue;
    }
}
