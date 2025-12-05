// com/campus/entity/AttendanceRecord.java
package com.campus.entity;

import java.util.Date;

public class AttendanceRecord {
    private Integer id;
    private Integer studentId;
    private Integer courseID;
    private Date attendanceDate;
    private Date attendanceTime;
    private String status; // PRESENT/ABSENT/LATE
    private String remarks;
    private Integer createdBy;
    private Date createTime;

    // 添加关联对象（用于查询显示）
//    private Student student;
//    private Course course;
//    private Teacher teacher;

    // getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public Integer getCourseId() { return courseID; }
    public void setCourseId(Integer courseID) { this.courseID = courseID; }

    public Date getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(Date attendanceDate) { this.attendanceDate = attendanceDate; }

    public Date getAttendanceTime() { return attendanceTime; }
    public void setAttendanceTime(Date attendanceTime) { this.attendanceTime = attendanceTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}