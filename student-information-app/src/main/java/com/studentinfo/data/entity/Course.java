package com.studentinfo.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_plan", nullable = false)
    private String coursePlan;

    @Column(name = "duration", nullable = false)
    private Integer duration;  // Changed to Integer for flexibility

    // Default constructor (required by JPA)
    public Course() {}

    // Constructor with all fields
    public Course(Long courseId, String courseName, String coursePlan, Integer duration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.coursePlan = coursePlan;
        this.duration = duration;
    }

    // Constructor without courseId
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

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", coursePlan='" + coursePlan + '\'' +
                ", duration=" + duration +
                '}';
    }
}
