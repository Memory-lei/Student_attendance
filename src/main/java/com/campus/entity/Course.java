// com/campus/entity/Course.java
package com.campus.entity;

public class Course {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Integer teacherId;
    private String description;

    // getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public Integer getTeacherId() { return teacherId; }
    public void setTeacherId(Integer teacherId) { this.teacherId = teacherId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
