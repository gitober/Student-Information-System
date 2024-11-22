package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "department_translation")
public class DepartmentTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "locale", nullable = false, length = 5)
    private String locale;

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "translated_value", nullable = false)
    private String translatedValue;

    // Default constructor
    public DepartmentTranslation() {}

    // Constructor with fields
    public DepartmentTranslation(Long departmentId, String locale, String fieldName, String translatedValue) {
        this.departmentId = departmentId;
        this.locale = locale;
        this.fieldName = fieldName;
        this.translatedValue = translatedValue;
    }
}
