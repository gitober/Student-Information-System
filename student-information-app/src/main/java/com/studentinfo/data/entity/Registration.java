package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @Column(name = "registration_day")
    private LocalDate registrationDay;

    @Column(name = "course_payment")
    private Double coursePayment;

    @Column(name = "student_number")
    private Long studentNumber;

    @ManyToOne
    @JoinColumn(name = "student_number", referencedColumnName = "student_number", insertable = false, updatable = false)
    private Student student;

    @Column(name = "batch_id", nullable = true)
    private Long batchId;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
