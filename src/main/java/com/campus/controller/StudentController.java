package com.campus.controller;

import com.campus.entity.Student;
import com.campus.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// StudentController.java
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public List<Student> list() {
        return studentService.listAll();
    }

    @PostMapping("/add")
    public String add(@RequestBody Student student) {
        studentService.add(student);
        return "success";
    }
}