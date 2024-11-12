package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    private String address;
    private String grade;
    private String studentClass;

    // Many-to-many relationship with Course (like in Teacher)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "registration",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    )
    private Set<Course> courses = new HashSet<>(); // Updated to non-transient

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId()) &&
                Objects.equals(address, student.address) &&
                Objects.equals(grade, student.grade) &&
                Objects.equals(studentClass, student.studentClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), address, grade, studentClass);
    }
}
