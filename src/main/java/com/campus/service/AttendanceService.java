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

    // 查询学生考勤记录 - 参数类型改为 Integer
    List<AttendanceRecord> getAttendanceByStudentId(Integer studentId);

    // 查询日期考勤记录
    List<AttendanceRecord> getAttendanceByDate(Date date);

    // 查询统计信息
    List<Map<String, Object>> getAttendanceStatistics(Date startDate, Date endDate);

    // 查询学生考勤统计 - 参数类型改为 Integer
    Map<String, Object> getStudentAttendanceSummary(Integer studentId);

    // 检查是否已考勤 - 参数类型改为 Integer
    boolean checkAttendanceExists(Integer studentId, Date date);
}