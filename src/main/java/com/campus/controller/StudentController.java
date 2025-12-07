// com/campus/controller/StudentController.java
package com.campus.controller;

import com.campus.entity.Student;
import com.campus.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public List<Student> list() {
        return studentService.listAll();
    }

    @GetMapping("/{studentId}")
    public Map<String, Object> getByStudentId(@PathVariable Integer studentId) {
        Map<String, Object> result = new HashMap<>();
        Student student = studentService.getByStudentId(studentId);

        if (student != null) {
            result.put("success", true);
            result.put("student", student);
        } else {
            result.put("success", false);
            result.put("message", "学生不存在");
        }

        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Student student) {
        Map<String, Object> result = new HashMap<>();

        try {
            studentService.add(student);
            result.put("success", true);
            result.put("message", "添加成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }

        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Student student) {
        Map<String, Object> result = new HashMap<>();

        try {
            studentService.update(student);
            result.put("success", true);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }

        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();

        try {
            studentService.delete(id);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }

        return result;
    }
}