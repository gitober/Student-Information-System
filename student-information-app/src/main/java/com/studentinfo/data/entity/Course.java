package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {

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



    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePlan() {
        return coursePlan;
    }

    public void setCoursePlan(String coursePlan) {
        this.coursePlan = coursePlan;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    // Additional Methods
    // Method to calculate and display start and end dates based on duration
    public String getFormattedDateRange() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(duration);

        // Format the date as "dd.MM.yyyy - dd.MM.yyyy"
        return startDate.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " - " + endDate.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
