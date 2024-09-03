package com.studentinfo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherID; // Add this field if Teacher has its own unique ID; otherwise, use User's ID.

    @Column(name = "Student_name", nullable = false)
    private int studentName;

    @Column(name = "Address", nullable = false)
    private int address;

    @Column(name = "Phone_number", nullable = false)
    private int phoneNumber;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    // Constructors
    public Teacher() {
    }

    public Teacher(int studentName, int address, int phoneNumber, User user) {
        this.studentName = studentName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    // Getters and Setters
    public int getStudentName() {
        return studentName;
    }

    public void setStudentName(int studentName) {
        this.studentName = studentName;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "studentName=" + studentName +
                ", address=" + address +
                ", phoneNumber=" + phoneNumber +
                ", user=" + user +
                '}';
    }
}
