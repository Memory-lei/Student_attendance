package com.campus.service;

import com.campus.entity.Student;
import com.campus.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// StudentService.java
@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public List<Student> listAll() {
        return studentMapper.selectAll();
    }

    public void add(Student student) {
        studentMapper.insert(student);
    }
}