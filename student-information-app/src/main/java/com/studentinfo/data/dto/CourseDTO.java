package com.studentinfo.data.dto;

public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String coursePlan;
    private int duration;

    // Getters and Setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCoursePlan() { return coursePlan; }
    public void setCoursePlan(String coursePlan) { this.coursePlan = coursePlan; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
