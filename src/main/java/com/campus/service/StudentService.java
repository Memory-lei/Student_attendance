// com/campus/service/StudentService.java
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

    // 添加新方法：根据学号查询学生
    public Student getByStudentId(String studentId) {
        return studentMapper.selectByStudentId(studentId);
    }

    // 添加更新方法
    public void update(Student student) {
        studentMapper.update(student);
    }

    // 添加删除方法
    public void delete(Integer id) {
        studentMapper.delete(id);
    }
}