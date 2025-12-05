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
    public Map<String, Object> recordAttendance(@RequestBody Map<String, Object> params,  // 改为Object类型
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
            // 类型转换处理
            Integer studentId = null;
            Integer courseId = null;

            try {
                studentId = Integer.parseInt(params.get("studentId").toString());
                courseId = Integer.parseInt(params.get("courseId").toString());
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "学生ID或课程ID格式错误");
                return result;
            }

            String status = (String) params.get("status");

            if (studentId == null || courseId == null || status == null) {
                result.put("success", false);
                result.put("message", "参数不完整");
                return result;
            }

            AttendanceRecord record = new AttendanceRecord();
            record.setStudentId(studentId);  // 使用Integer类型
            record.setCourseId(courseId);    // 使用courseId
            record.setAttendanceDate(new Date());
            record.setStatus(status);
            record.setRemarks((String) params.get("remarks"));
            record.setCreatedBy(user.getId());  // 使用用户ID而不是用户名

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
    public Map<String, Object> makeupAttendance(@RequestBody Map<String, Object> params,  // 改为Object类型
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

            // 类型转换处理
            Integer studentId = Integer.parseInt(params.get("studentId").toString());
            Integer courseId = Integer.parseInt(params.get("courseId").toString());

            record.setStudentId(studentId);
            record.setCourseId(courseId);
            record.setStatus((String) params.get("status"));
            record.setRemarks((String) params.get("remarks"));
            record.setCreatedBy(user.getId());  // 使用用户ID

            // 解析日期
            String dateStr = (String) params.get("attendanceDate");
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

        if ("STUDENT".equals(user.getRole())) {
            // 需要建立User和Student的关联，这里假设可以通过用户ID查询学生ID
            // 实际实现需要在service层处理
            List<AttendanceRecord> records = attendanceService.getAttendanceByStudentId(user.getId());
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
            @RequestParam(required = false) Integer studentId,  // 改为Integer类型
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

        if (studentId != null) {
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

    // 其他方法保持不变...
}