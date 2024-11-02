package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Setter
@Getter
@Entity
@Table(name = "course")
public class Course {

    // Getters and Setters
    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_plan", nullable = false)
    private String coursePlan;

    @Column(name = "duration", nullable = false)
    private Integer duration; // Duration in days

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers;

    // Constructors
    // Default constructor required by JPA
    public Course() {}

    // Constructor accepting all fields (except for ID, which is auto-generated)
    public Course(String courseName, String coursePlan, Integer duration) {
        this.courseName = courseName;
        this.coursePlan = coursePlan;
        this.duration = duration;
    }


    // Additional Methods
    // Method to calculate and display start and end dates based on duration
    public String getFormattedDateRange() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(duration);
        Locale currentLocale = LocaleContextHolder.getLocale();

        DateTimeFormatter formatter;
        if (currentLocale.getLanguage().equals("ch")) {
            formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.forLanguageTag("ch"));
        } else if (Locale.UK.equals(currentLocale)) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.UK);
        } else if (Locale.forLanguageTag("fi-FI").equals(currentLocale)) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", currentLocale);
        } else if (Locale.forLanguageTag("ru-RU").equals(currentLocale)) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", currentLocale);
        } else {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
        }


        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

}
