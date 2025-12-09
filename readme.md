--用户表 (user)
CREATE TABLE user (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(100) NOT NULL,
role ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL,
created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
--老师表 (teacher)
CREATE TABLE teacher (
id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT UNIQUE NOT NULL,
name VARCHAR(50) NOT NULL,
teacher_id VARCHAR(20) UNIQUE NOT NULL,
department VARCHAR(100),
title VARCHAR(50),
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

--学生表 (student)
CREATE TABLE student (
id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT UNIQUE NOT NULL,
name VARCHAR(50) NOT NULL,
student_id VARCHAR(20) UNIQUE NOT NULL,
class_name VARCHAR(50),
major VARCHAR(100),
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

--课程表 (course)
CREATE TABLE course (
id INT PRIMARY KEY AUTO_INCREMENT,
course_name VARCHAR(100) NOT NULL,
course_code VARCHAR(20) UNIQUE NOT NULL,
teacher_id INT NOT NULL,
description TEXT,
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE CASCADE
);

--学生-课程关联表 (student_course)
CREATE TABLE student_course (
id INT PRIMARY KEY AUTO_INCREMENT,
student_id INT NOT NULL,
course_id INT NOT NULL,
enroll_date DATE NOT NULL,
status ENUM('ACTIVE', 'COMPLETED', 'DROPPED') DEFAULT 'ACTIVE',
UNIQUE KEY uk_student_course (student_id, course_id),
FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

--考勤记录表 (attendance_record)
CREATE TABLE attendance_record (
id INT PRIMARY KEY AUTO_INCREMENT,
student_id INT NOT NULL,
course_id INT NOT NULL,
attendance_date DATE NOT NULL,
attendance_time TIME NOT NULL,
status ENUM('PRESENT', 'ABSENT', 'LATE') NOT NULL,
remarks TEXT,
created_by INT NOT NULL, -- 记录创建者（老师ID）
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
UNIQUE KEY uk_student_course_date (student_id, course_id, attendance_date),
FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
FOREIGN KEY (created_by) REFERENCES teacher(id) ON DELETE CASCADE
);

老师-学生管理关系表 (teacher_student)
CREATE TABLE teacher_student (
id INT PRIMARY KEY AUTO_INCREMENT,
teacher_id INT NOT NULL,
student_id INT NOT NULL,
relationship_type ENUM('ADVISOR', 'INSTRUCTOR') NOT NULL,
start_date DATE NOT NULL,
end_date DATE,
UNIQUE KEY uk_teacher_student (teacher_id, student_id, relationship_type),
FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE CASCADE,
FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

--系统日志表 (system_log)
CREATE TABLE system_log (
id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT NOT NULL,
action VARCHAR(100) NOT NULL,
target_type VARCHAR(50) NOT NULL,
target_id INT,
description TEXT,
ip_address VARCHAR(45),
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 以下是插入的数据
INSERT INTO user (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('teacher1', 'teacher123', 'TEACHER'),
('teacher2', 'teacher456', 'TEACHER'),
('student1', 'student123', 'STUDENT'),
('student2', 'student456', 'STUDENT'),
('student3', 'student789', 'STUDENT'),
('student4', 'student000', 'STUDENT');


INSERT INTO teacher (user_id, name, teacher_id, department, title) VALUES
(2, '张老师', 'T001', '计算机科学系', '副教授'),
(3, '李老师', 'T002', '软件工程系', '讲师');

INSERT INTO student (user_id, name, student_id, class_name, major) VALUES
(4, '张三', 'S2024001', '计算机1班', '计算机科学与技术'),
(5, '李四', 'S2024002', '计算机1班', '计算机科学与技术'),
(6, '王五', 'S2024003', '软件工程1班', '软件工程'),
(7, '赵六', 'S2024004', '软件工程1班', '软件工程');

INSERT INTO course (course_name, course_code, teacher_id, description) VALUES
('Java程序设计', 'CS001', 1, 'Java语言基础与面向对象编程'),
('数据库原理', 'CS002', 1, '数据库系统原理与应用'),
('Web开发技术', 'SE001', 2, '前端与后端Web开发技术'),
('软件工程', 'SE002', 2, '软件开发流程与方法论');

INSERT INTO student_course (student_id, course_id, enroll_date, status) VALUES
-- 张三选课
(1, 1, '2024-09-01', 'ACTIVE'),
(1, 2, '2024-09-01', 'ACTIVE'),
-- 李四选课
(2, 1, '2024-09-01', 'ACTIVE'),
(2, 3, '2024-09-01', 'ACTIVE'),
-- 王五选课
(3, 3, '2024-09-01', 'ACTIVE'),
(3, 4, '2024-09-01', 'ACTIVE'),
-- 赵六选课
(4, 2, '2024-09-01', 'ACTIVE'),
(4, 4, '2024-09-01', 'ACTIVE');

INSERT INTO attendance_record (student_id, course_id, attendance_date, attendance_time, status, remarks, created_by) VALUES
-- Java程序设计考勤记录
(1, 1, '2024-10-01', '08:30:00', 'PRESENT', '按时到课', 1),
(2, 1, '2024-10-01', '08:45:00', 'LATE', '迟到15分钟', 1),
(1, 1, '2024-10-08', '08:30:00', 'PRESENT', '', 1),
(2, 1, '2024-10-08', '08:30:00', 'ABSENT', '请假', 1),

-- 数据库原理考勤记录
(1, 2, '2024-10-02', '10:00:00', 'PRESENT', '', 1),
(4, 2, '2024-10-02', '10:00:00', 'PRESENT', '', 1),

-- Web开发技术考勤记录
(2, 3, '2024-10-03', '14:00:00', 'PRESENT', '', 2),
(3, 3, '2024-10-03', '14:10:00', 'LATE', '交通堵塞', 2),

-- 软件工程考勤记录
(3, 4, '2024-10-04', '16:00:00', 'PRESENT', '', 2),
(4, 4, '2024-10-04', '16:00:00', 'PRESENT', '', 2);


INSERT INTO teacher_student (teacher_id, student_id, relationship_type, start_date) VALUES
-- 张老师管理计算机1班学生
(1, 1, 'ADVISOR', '2024-09-01'),
(1, 2, 'ADVISOR', '2024-09-01'),
-- 李老师管理软件工程1班学生
(2, 3, 'ADVISOR', '2024-09-01'),
(2, 4, 'ADVISOR', '2024-09-01'),
-- 授课关系
(1, 1, 'INSTRUCTOR', '2024-09-01'),
(1, 2, 'INSTRUCTOR', '2024-09-01'),
(1, 4, 'INSTRUCTOR', '2024-09-01'),
(2, 2, 'INSTRUCTOR', '2024-09-01'),
(2, 3, 'INSTRUCTOR', '2024-09-01'),
(2, 4, 'INSTRUCTOR', '2024-09-01');

INSERT INTO system_log (user_id, action, target_type, target_id, description, ip_address) VALUES
(1, 'LOGIN', 'SYSTEM', NULL, '管理员登录系统', '192.168.1.100'),
(2, 'CREATE', 'ATTENDANCE', 1, '创建考勤记录', '192.168.1.101'),
(2, 'UPDATE', 'ATTENDANCE', 2, '更新考勤状态', '192.168.1.101'),
(3, 'CREATE', 'COURSE', 3, '创建新课程', '192.168.1.102'),
(1, 'DELETE', 'USER', 5, '删除用户', '192.168.1.100');