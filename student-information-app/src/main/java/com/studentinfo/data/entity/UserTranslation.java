package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_translation")
public class UserTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "locale", nullable = false, length = 5)
    private Language locale; // Updated to use Language enum

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "translated_value", nullable = false, length = 255)
    private String translatedValue;

    // Default constructor
    public UserTranslation() {}

    // Constructor with fields
    public UserTranslation(User user, Language locale, String fieldName, String translatedValue) {
        this.user = user;
        this.locale = locale;
        this.fieldName = fieldName;
        this.translatedValue = translatedValue;
    }
}
