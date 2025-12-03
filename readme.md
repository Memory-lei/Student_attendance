MySQL中创建数据库和表：

sql
-- 创建数据库
CREATE DATABASE campus_attendance DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_attendance;

-- 创建学生表
CREATE TABLE student (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(50) NOT NULL,
student_id VARCHAR(20) NOT NULL UNIQUE,
class VARCHAR(50),
major VARCHAR(100)
);

-- 创建用户表
CREATE TABLE user (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL,
role VARCHAR(20) NOT NULL
);

-- 插入测试数据
INSERT INTO student (name, student_id, class, major) VALUES
('张三', '2023001', '计算机1班', '计算机科学与技术'),
('李四', '2023002', '软件1班', '软件工程');

INSERT INTO user (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('teacher1', 'teacher123', 'TEACHER');


![img.png](img.png)