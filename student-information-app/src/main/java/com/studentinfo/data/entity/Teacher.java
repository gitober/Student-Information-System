package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    // Getters and setters for courses, subject, and department
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "teacher_courses",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();


    // Constructor
    public Teacher() {
        super(); // Call the parent class (User) constructor
    }

    // Method to retrieve teacher_id
    public Long getTeacherId() {
        return super.getId();
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
