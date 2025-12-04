// com/campus/service/AttendanceService.java
package com.campus.service;

import com.campus.entity.AttendanceRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    // 记录考勤
    boolean recordAttendance(AttendanceRecord record);

    // 补签考勤
    boolean makeUpAttendance(AttendanceRecord record);

    // 查询学生考勤记录
    List<AttendanceRecord> getAttendanceByStudentId(String studentId);

    // 查询日期考勤记录
    List<AttendanceRecord> getAttendanceByDate(Date date);

    // 查询统计信息
    List<Map<String, Object>> getAttendanceStatistics(Date startDate, Date endDate);

    // 查询学生考勤统计
    Map<String, Object> getStudentAttendanceSummary(String studentId);

    // 检查是否已考勤
    boolean checkAttendanceExists(String studentId, Date date);
}