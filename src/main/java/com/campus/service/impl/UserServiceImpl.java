// com/campus/service/impl/UserServiceImpl.java
package com.campus.service.impl;

import com.campus.entity.User;
import com.campus.mapper.UserMapper;
import com.campus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        // 1. 根据用户名查询用户
        User user = userMapper.selectByUsername(username);
        // 2. 直接比对明文密码（作业简化版）
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (checkUsernameExists(user.getUsername())) {
            return false;
        }
        // 密码直接存明文（作业简化版）
        // 注意：实际生产环境绝不允许这样做！
        userMapper.insert(user);
        return true;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    // 完全删除原来的 encryptPassword 方法
}