// com/campus/mapper/StudentMapper.java
package com.campus.mapper;

import com.campus.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> selectAll();
    void insert(Student student);
    void update(Student student);
    void delete(Integer id);

    // 新增：根据学号查询学生
    Student selectByStudentId(@Param("studentId") String studentId);
}