// com/campus/service/UserService.java
package com.campus.service;

import com.campus.entity.User;

public interface UserService {
    // 登录
    User login(String username, String password);

    // 注册
    boolean register(User user);

    // 检查用户名是否已存在
    boolean checkUsernameExists(String username);
}