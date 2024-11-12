package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serial;
import java.io.Serializable; // Import Serializable
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Setter
@Getter
@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
    public Course() {}

    public Course(String courseName, String coursePlan, Integer duration) {
        this.courseName = courseName;
        this.coursePlan = coursePlan;
        this.duration = duration;
    }

    // Additional Methods
    public String getFormattedDateRange() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(duration);
        Locale currentLocale = LocaleContextHolder.getLocale();

        DateTimeFormatter formatter;
        if (currentLocale.getLanguage().equals("zh")) {
            formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", currentLocale);
        } else if (currentLocale.equals(Locale.forLanguageTag("fi-FI"))) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", currentLocale);
        } else if (currentLocale.equals(Locale.forLanguageTag("ru-RU"))) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", currentLocale);
        } else {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", currentLocale);
        }

        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

    @ManyToMany(mappedBy = "courses")
    private Collection<Student> students;

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }
}
