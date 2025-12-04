// com/campus/controller/UserController.java
package com.campus.controller;

import com.campus.entity.User;
import com.campus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录 - 使用POST方法
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String username = params.get("username");
        String password = params.get("password");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名和密码不能为空");
            return result;
        }

        User loginUser = userService.login(username, password);

        if (loginUser != null) {
            // 存储用户信息到session（移除密码）
            User sessionUser = new User();
            sessionUser.setId(loginUser.getId());
            sessionUser.setUsername(loginUser.getUsername());
            sessionUser.setRole(loginUser.getRole());
            session.setAttribute("currentUser", sessionUser);

            result.put("success", true);
            result.put("message", "登录成功");
            result.put("role", loginUser.getRole());
            result.put("userId", loginUser.getId());
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    /**
     * 注册 - 使用POST方法
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();

        String username = params.get("username");
        String password = params.get("password");
        String role = params.get("role");

        // 验证参数
        if (username == null || username.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }

        if (password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }

        if (role == null) {
            role = "STUDENT"; // 默认角色为学生
        }

        // 验证角色是否合法
        if (!"ADMIN".equals(role) && !"TEACHER".equals(role) && !"STUDENT".equals(role)) {
            result.put("success", false);
            result.put("message", "角色必须是ADMIN、TEACHER或STUDENT");
            return result;
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Service层会加密
        user.setRole(role);

        boolean success = userService.register(user);

        if (success) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "用户名已存在");
        }
        return result;
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        session.invalidate();
        result.put("success", true);
        result.put("message", "退出成功");
        return result;
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            result.put("success", true);
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "未登录");
        }
        return result;
    }
}