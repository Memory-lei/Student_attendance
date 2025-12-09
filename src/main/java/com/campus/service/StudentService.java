package com.campus.service;

import com.campus.entity.Student;
import com.campus.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // 参数类型改为 Integer
    public Student getByStudentId(Integer studentId) {
        return studentMapper.selectByStudentId(studentId);
    }

    public void update(Student student) {
        studentMapper.update(student);
    }

    public void delete(Integer id) {
        studentMapper.delete(id);
    }
}