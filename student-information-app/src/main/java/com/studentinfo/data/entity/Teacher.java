package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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
        return super.getId(); // Assuming 'id' in the User class corresponds to the teacher_id in the database
    }

    // Getters and setters for courses, subject, and department
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

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

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
