<!-- student_list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生管理</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
</head>
<body>
<table border="1">
    <tr>
        <th>姓名</th>
        <th>学号</th>
        <th>班级</th>
        <th>专业</th>
    </tr>
    <tbody id="studentTable"></tbody>
</table>
<script>
    $(function() {
        $.get("/student/list", function(data) {
            let html = '';
            data.forEach(s => {
                html += `<tr>
                        <td>${s.name}</td>
                        <td>${s.studentId}</td>
                        <td>${s.class}</td>
                        <td>${s.major}</td>
                    </tr>`;
            });
            $("#studentTable").html(html);
        });
    });
</script>
</body>
</html>