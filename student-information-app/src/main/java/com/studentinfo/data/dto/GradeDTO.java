package com.studentinfo.data.dto;

import java.time.LocalDate;

public class GradeDTO {
    private Integer gradeId;
    private String grade;
    private LocalDate gradingDay;
    private Integer studentNumber;
    private Integer courseId;

    // Getters and Setters
    public Integer getGradeId() { return gradeId; }
    public void setGradeId(Integer gradeId) { this.gradeId = gradeId; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public LocalDate getGradingDay() { return gradingDay; }
    public void setGradingDay(LocalDate gradingDay) { this.gradingDay = gradingDay; }
    public Integer getStudentNumber() { return studentNumber; }
    public void setStudentNumber(Integer studentNumber) { this.studentNumber = studentNumber; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
}
