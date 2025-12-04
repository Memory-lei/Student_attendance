// com/campus/service/impl/AttendanceServiceImpl.java
package com.campus.service.impl;

import com.campus.entity.AttendanceRecord;
import com.campus.entity.Student;
import com.campus.mapper.AttendanceMapper;
import com.campus.mapper.StudentMapper;
import com.campus.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private StudentMapper studentMapper;  // 使用你定义的StudentMapper

    @Override
    @Transactional
    public boolean recordAttendance(AttendanceRecord record) {
        // 验证学生是否存在 - 使用 selectByStudentId 方法
        Student student = studentMapper.selectByStudentId(record.getStudentId());

        if (student == null) {
            throw new RuntimeException("学生不存在，学号：" + record.getStudentId());
        }

        // 检查是否已考勤
        if (checkAttendanceExists(record.getStudentId(), record.getAttendanceDate())) {
            throw new RuntimeException("该学生今天已考勤，学号：" + record.getStudentId());
        }

        // 设置创建时间
        if (record.getAttendanceTime() == null) {
            record.setAttendanceTime(new Date());
        }

        return attendanceMapper.insert(record) > 0;
    }

    @Override
    @Transactional
    public boolean makeUpAttendance(AttendanceRecord record) {
        // 验证学生是否存在
        Student student = studentMapper.selectByStudentId(record.getStudentId());
        if (student == null) {
            throw new RuntimeException("学生不存在，学号：" + record.getStudentId());
        }

        // 检查是否已有记录
        AttendanceRecord existing = attendanceMapper.selectByStudentAndDate(
                record.getStudentId(), record.getAttendanceDate());

        if (existing != null) {
            // 更新现有记录
            existing.setStatus(record.getStatus());
            existing.setRemarks(record.getRemarks());
            existing.setAttendanceTime(new Date());
            existing.setCreatedBy(record.getCreatedBy());
            return attendanceMapper.update(existing) > 0;
        } else {
            // 创建新记录
            return recordAttendance(record);
        }
    }

    @Override
    public List<AttendanceRecord> getAttendanceByStudentId(String studentId) {
        return attendanceMapper.selectByStudentId(studentId);
    }

    @Override
    public List<AttendanceRecord> getAttendanceByDate(Date date) {
        return attendanceMapper.selectByDate(date);
    }

    @Override
    public List<Map<String, Object>> getAttendanceStatistics(Date startDate, Date endDate) {
        return attendanceMapper.getAttendanceStatistics(startDate, endDate);
    }

    @Override
    public Map<String, Object> getStudentAttendanceSummary(String studentId) {
        return attendanceMapper.getStudentAttendanceSummary(studentId);
    }

    @Override
    public boolean checkAttendanceExists(String studentId, Date date) {
        AttendanceRecord record = attendanceMapper.selectByStudentAndDate(studentId, date);
        return record != null;
    }
}