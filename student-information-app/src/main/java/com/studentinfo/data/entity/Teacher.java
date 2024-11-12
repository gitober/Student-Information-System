package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Add serialVersionUID

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;  // This field will be serialized

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;  // This field will be serialized

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "teacher_courses",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();  // This field will also be serialized

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(getId(), teacher.getId()) &&
                Objects.equals(subject, teacher.subject) &&
                Objects.equals(department, teacher.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), subject, department);
    }
}
