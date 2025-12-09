// com/campus/entity/User.java
package com.campus.entity;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String role; // ADMIN/TEACHER/STUDENT
    private Integer studentId;  // 如果是学生角色，关联学生ID
    private Integer teacherId;  // 如果是教师角色，关联教师ID
    // 新增构造方法
    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 已有的getter和setter方法...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}