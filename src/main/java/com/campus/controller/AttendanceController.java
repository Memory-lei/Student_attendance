// com/campus/controller/AttendanceController.java
package com.campus.controller;

import com.campus.entity.AttendanceRecord;
import com.campus.entity.User;
import com.campus.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 记录考勤（教师使用）
     */
    @PostMapping("/record")
    public Map<String, Object> recordAttendance(@RequestBody Map<String, String> params,
                                                HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户是否登录且为教师
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }

        if (!"TEACHER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            result.put("success", false);
            result.put("message", "只有教师和管理员可以记录考勤");
            return result;
        }

        try {
            String studentId = params.get("studentId");
            String courseName = params.get("courseName");
            String status = params.get("status");

            if (studentId == null || courseName == null || status == null) {
                result.put("success", false);
                result.put("message", "参数不完整");
                return result;
            }

            AttendanceRecord record = new AttendanceRecord();
            record.setStudentId(studentId);
            record.setCourseName(courseName);
            record.setAttendanceDate(new Date());
            record.setStatus(status);
            record.setRemarks(params.get("remarks"));
            record.setCreatedBy(user.getUsername());

            boolean success = attendanceService.recordAttendance(record);

            if (success) {
                result.put("success", true);
                result.put("message", "考勤记录成功");
            } else {
                result.put("success", false);
                result.put("message", "考勤记录失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 补签考勤（教师使用）
     */
    @PostMapping("/makeup")
    public Map<String, Object> makeupAttendance(@RequestBody Map<String, String> params,
                                                HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"TEACHER".equals(user.getRole())) {
            result.put("success", false);
            result.put("message", "只有教师可以进行补签");
            return result;
        }

        try {
            AttendanceRecord record = new AttendanceRecord();
            record.setStudentId(params.get("studentId"));
            record.setCourseName(params.get("courseName"));
            record.setStatus(params.get("status"));
            record.setRemarks(params.get("remarks"));
            record.setCreatedBy(user.getUsername());

            // 解析日期
            String dateStr = params.get("attendanceDate");
            if (dateStr != null) {
                record.setAttendanceDate(new Date(Long.parseLong(dateStr)));
            } else {
                record.setAttendanceDate(new Date());
            }

            boolean success = attendanceService.makeUpAttendance(record);

            if (success) {
                result.put("success", true);
                result.put("message", "补签成功");
            } else {
                result.put("success", false);
                result.put("message", "补签失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 获取我的考勤记录（学生使用）
     */
    @GetMapping("/my")
    public Map<String, Object> getMyAttendance(HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }

        // 学生只能查看自己的考勤记录
        // 注意：这里需要关联学生ID和用户ID，暂时简化处理
        // 实际应用中，应该在User表中存储student_id字段
        if ("STUDENT".equals(user.getRole())) {
            // 假设学生用户名为学号（简化处理）
            List<AttendanceRecord> records = attendanceService.getAttendanceByStudentId(user.getUsername());
            result.put("success", true);
            result.put("records", records);
        } else {
            result.put("success", false);
            result.put("message", "只有学生可以查看个人考勤");
        }

        return result;
    }

    /**
     * 查询考勤记录（教师/管理员使用）
     */
    @GetMapping("/query")
    public Map<String, Object> queryAttendance(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        User user = (User) session.getAttribute("currentUser");
        if (user == null || (!"TEACHER".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            result.put("success", false);
            result.put("message", "权限不足");
            return result;
        }

        List<AttendanceRecord> records;

        if (studentId != null && !studentId.trim().isEmpty()) {
            records = attendanceService.getAttendanceByStudentId(studentId);
        } else if (date != null) {
            records = attendanceService.getAttendanceByDate(date);
        } else {
            // 默认查询今天的考勤
            records = attendanceService.getAttendanceByDate(new Date());
        }

        result.put("success", true);
        result.put("records", records);
        return result;
    }

    /**
     * 获取考勤统计
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String studentId,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        User user = (User) session.getAttribute("currentUser");
        if (user == null || (!"TEACHER".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            result.put("success", false);
            result.put("message", "权限不足");
            return result;
        }

        if (studentId != null && !studentId.trim().isEmpty()) {
            // 学生考勤统计
            Map<String, Object> summary = attendanceService.getStudentAttendanceSummary(studentId);
            result.put("success", true);
            result.put("summary", summary);
        } else {
            // 时间段统计
            if (startDate == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                startDate = calendar.getTime();
            }

            if (endDate == null) {
                endDate = new Date();
            }

            List<Map<String, Object>> statistics = attendanceService.getAttendanceStatistics(startDate, endDate);
            result.put("success", true);
            result.put("statistics", statistics);
        }

        return result;
    }
}