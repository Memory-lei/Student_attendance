package com.campus.mapper;

import com.campus.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    // 基础CRUD
    int insert(Student student);
    int update(Student student);
    int delete(@Param("id") Integer id);

    // 查询方法
    Student selectById(@Param("id") Integer id);
    Student selectByStudentId(@Param("studentId") Integer studentId);
    List<Student> selectAll();

    // 条件查询（新增）
    List<Student> selectByCondition(@Param("student") Student condition);
    List<Student> selectByIds(@Param("ids") List<Integer> ids);
}