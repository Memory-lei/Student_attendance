package com.campus.mapper;

import com.campus.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 基础操作
    int insert(User user);
    int update(User user);
    int delete(@Param("id") Long id);

    // 查询方法
    User selectById(@Param("id") Long id);
    User selectByUsername(@Param("username") String username);

    // 安全检查（新增）
    User selectByUsernameForAuth(@Param("username") String username);
}