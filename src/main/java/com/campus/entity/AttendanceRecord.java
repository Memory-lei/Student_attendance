// com/campus/entity/AttendanceRecord.java
package com.campus.entity;

import java.util.Date;

public class AttendanceRecord {
    private Integer id;
    private String studentId;
    private String courseName;
    private Date attendanceDate;
    private Date attendanceTime;
    private String status; // PRESENT/ABSENT/LATE
    private String remarks;
    private String createdBy;
    private Date createTime;

    // getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Date getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(Date attendanceDate) { this.attendanceDate = attendanceDate; }

    public Date getAttendanceTime() { return attendanceTime; }
    public void setAttendanceTime(Date attendanceTime) { this.attendanceTime = attendanceTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}