// com/campus/mapper/UserMapper.java
package com.campus.mapper;

import com.campus.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User selectByUsername(String username);

    // 新增用户(注册)
    void insert(User user);
}