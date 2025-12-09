<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员主页</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background-color: #333; color: white; padding: 10px; display: flex; justify-content: space-between; }
        .menu { margin: 20px 0; }
        .menu a { margin-right: 20px; text-decoration: none; color: #333; padding: 5px 10px; border: 1px solid #ccc; }
        .menu a:hover { background-color: #f0f0f0; }
        .content { margin-top: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f0f0f0; }
    </style>
</head>
<body>
<div class="header">
    <h2>学生考勤系统 - 管理员</h2>
    <div>
        欢迎，${user.username}（管理员） |
        <a href="javascript:void(0)" onclick="logout()">退出</a>
    </div>
</div>

<div class="menu">
    <a href="javascript:void(0)" onclick="loadContent('users')">用户管理</a>
    <a href="javascript:void(0)" onclick="loadContent('students')">学生管理</a>
    <a href="javascript:void(0)" onclick="loadContent('attendance')">考勤统计</a>
</div>

<div id="content" class="content">
    <h3>欢迎使用学生考勤管理系统</h3>
    <p>请选择左侧菜单进行操作</p>
</div>

<script>
    // 添加补零函数，兼容旧环境
    function padZero(num) {
        return num < 10 ? '0' + num : num;
    }

    function loadContent(type) {
        if (type === 'students') {
            $.get("/student/list", function(data) {
                let html = '<h3>学生列表</h3><table><tr><th>ID</th><th>学号</th><th>姓名</th><th>班级</th><th>专业</th></tr>';
                data.forEach(s => {
                    html += `<tr>
                            <td>${s.id}</td>
                            <td>${s.studentId}</td>
                            <td>${s.name}</td>
                            <td>${s.class_}</td>
                            <td>${s.major}</td>
                        </tr>`;
                });
                html += '</table>';
                $("#content").html(html);
            });
        } else if (type === 'attendance') {
            $.get("/api/attendance/statistics", function(response) {
                if (response.success) {
                    let html = '<h3>考勤统计</h3>';
                    if (response.statistics && response.statistics.length > 0) {
                        html += '<table><tr><th>日期</th><th>总人数</th><th>到课</th><th>缺勤</th><th>迟到</th><th>到课率</th></tr>';
                        response.statistics.forEach(stat => {
                            // 使用兼容的方式格式化日期
                            const date = new Date(stat.date);
                            const year = date.getFullYear();
                            const month = padZero(date.getMonth() + 1); // 月份从0开始，需要+1
                            const day = padZero(date.getDate());
                            const formattedDate = `${year}-${month}-${day}`;

                            const rate = (stat.present * 100 / stat.total).toFixed(2);
                            html += `<tr>
                                    <td>${formattedDate}</td>
                                    <td>${stat.total}</td>
                                    <td>${stat.present}</td>
                                    <td>${stat.absent}</td>
                                    <td>${stat.late}</td>
                                    <td>${rate}%</td>
                                </tr>`;
                        });
                        html += '</table>';
                    } else {
                        html += '<p>暂无考勤数据</p>';
                    }
                    $("#content").html(html);
                }
            });
        } else {
            $("#content").html("<h3>用户管理</h3><p>用户管理功能开发中...</p>");
        }
    }

    function logout() {
        $.post("/api/user/logout", function() {
            window.location.href = "/login";
        });
    }

    // 页面加载时检查登录状态
    $(function() {
        $.get("/api/user/current", function(response) {
            if (!response.success) {
                window.location.href = "/login";
            }
        });
    });
</script>
</body>
</html>