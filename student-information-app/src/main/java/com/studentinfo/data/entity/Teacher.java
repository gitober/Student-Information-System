package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    // Many-to-Many relationship with courses
    @ManyToMany
    @JoinTable(
            name = "teacher_courses",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    // Getters and setters for courses
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // Getters and setters for subject and department
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

}
