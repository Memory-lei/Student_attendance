package com.campus.mapper;

import com.campus.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> selectAll();
    void insert(Student student);
    void update(Student student);
    void delete(Integer id);
}