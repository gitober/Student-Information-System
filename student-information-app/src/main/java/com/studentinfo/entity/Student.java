package com.studentinfo.entity;

import com.studentinfo.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Student_number", nullable = false)
    private int studentNumber;

    @Column(name = "Firstname", nullable = false)
    private int firstname;

    @Column(name = "Lastname", nullable = false)
    private int lastname;

    @Column(name = "Birthday", nullable = false)
    private int birthday;

    @Column(name = "Email", nullable = false)
    private int email;

    @Column(name = "Phone_number_", nullable = false)
    private int phoneNumber;

    @Column(name = "Address", nullable = false)
    private int address;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    // Getters and Setters
    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getFirstname() {
        return firstname;
    }

    public void setFirstname(int firstname) {
        this.firstname = firstname;
    }

    public int getLastname() {
        return lastname;
    }

    public void setLastname(int lastname) {
        this.lastname = lastname;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
