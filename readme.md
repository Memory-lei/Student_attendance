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



-- 在已有的数据库基础上，添加考勤相关表

-- 考勤记录表
CREATE TABLE attendance_record (
id INT PRIMARY KEY AUTO_INCREMENT,
student_id VARCHAR(20) NOT NULL,
course_name VARCHAR(100) NOT NULL,
attendance_date DATE NOT NULL,
attendance_time TIME,
status VARCHAR(20) NOT NULL DEFAULT 'PRESENT', -- PRESENT/ABSENT/LATE
remarks VARCHAR(500),
created_by VARCHAR(50), -- 记录创建人（教师用户名）
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

-- 创建索引提高查询效率
CREATE INDEX idx_student_date ON attendance_record(student_id, attendance_date);
CREATE INDEX idx_date_status ON attendance_record(attendance_date, status);

-- 课程表（可选）
CREATE TABLE course (
id INT PRIMARY KEY AUTO_INCREMENT,
course_name VARCHAR(100) NOT NULL,
teacher_id INT, -- 关联user表的id
class_name VARCHAR(50),
schedule_time VARCHAR(100), -- 上课时间描述
status INT DEFAULT 1 -- 1启用，0停用
);




-- 添加更多测试数据到数据库

-- 添加更多学生
INSERT INTO student (name, student_id, class, major) VALUES
('王五', '2023003', '计算机1班', '计算机科学与技术'),
('赵六', '2023004', '软件1班', '软件工程'),
('钱七', '2023005', '网络1班', '网络工程'),
('孙八', '2023006', '计算机1班', '计算机科学与技术');

-- 添加更多用户（密码都是123456经过MD5加盐加密后的值）
-- 实际密码需要加密存储，这里先用明文，系统会自动加密
INSERT INTO user (username, password, role) VALUES
('2023001', '123456', 'STUDENT'),
('2023002', '123456', 'STUDENT'),
('2023003', '123456', 'STUDENT'),
('teacher2', '123456', 'TEACHER'),
('admin','123456','ADMIN'),
('student1', '123456', 'STUDENT');

-- 添加考勤测试数据
INSERT INTO attendance_record (student_id, course_name, attendance_date, attendance_time, status, remarks, created_by) VALUES
('2023001', '高等数学', DATE_SUB(CURDATE(), INTERVAL 3 DAY), '08:30:00', 'PRESENT', '', 'teacher1'),
('2023002', '高等数学', DATE_SUB(CURDATE(), INTERVAL 3 DAY), '08:35:00', 'LATE', '迟到5分钟', 'teacher1'),
('2023001', '大学英语', DATE_SUB(CURDATE(), INTERVAL 2 DAY), '10:00:00', 'PRESENT', '', 'teacher1'),
('2023003', '高等数学', DATE_SUB(CURDATE(), INTERVAL 3 DAY), '08:30:00', 'ABSENT', '请假', 'teacher1'),
('2023001', '数据结构', DATE_SUB(CURDATE(), INTERVAL 1 DAY), '14:00:00', 'PRESENT', '', 'teacher1'),
('2023002', '数据结构', DATE_SUB(CURDATE(), INTERVAL 1 DAY), '14:00:00', 'PRESENT', '', 'teacher1');