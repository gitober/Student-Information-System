package com.studentinfo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false, unique = true)
    private int userID;

    @Column(name = "Username", unique = true, nullable = false)
    private int username;

    @Column(name = "Password", nullable = false)
    private int password;

    @Column(name = "Role", nullable = false)
    private int role; // Consider changing to Enum if representing roles more explicitly.

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
