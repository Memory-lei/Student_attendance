<!-- WEB-INF/jsp/student.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>学生主页</title>
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/dayjs@1.10.7/dayjs.min.js"></script>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .header { background-color: #4CAF50; color: white; padding: 10px; display: flex; justify-content: space-between; }
    .content { margin-top: 20px; }
    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    th { background-color: #f0f0f0; }
    .status-present { color: green; }
    .status-absent { color: red; }
    .status-late { color: orange; }
    .summary { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
    .summary-item { display: inline-block; margin-right: 30px; }
  </style>
</head>
<body>
<div class="header">
  <h2>学生考勤系统 - 学生</h2>
  <div>
    欢迎，${user.username}（学生） |
    <a href="javascript:void(0)" onclick="logout()">退出</a>
  </div>
</div>

<div class="content">
  <div class="summary">
    <h3>我的考勤统计</h3>
    <div id="summaryStats">
      <p>加载中...</p>
    </div>
  </div>

  <h3>我的考勤记录</h3>
  <div id="attendanceRecords">
    <p>加载中...</p>
  </div>
</div>

<script>
  $(function() {
    loadMyAttendance();
    loadStatistics();
  });

  function loadMyAttendance() {
    $.get("api/attendance/my", function(response) {
      if (response.success) {
        let html = '';
        if (response.records && response.records.length > 0) {
          html += '<table><tr><th>日期</th><th>课程</th><th>时间</th><th>状态</th><th>备注</th><th>记录人</th></tr>';
          response.records.forEach(record => {
            const statusClass = 'status-' + record.status.toLowerCase();
            const date = new Date(record.attendanceDate);
            const time = record.attendanceTime ? new Date(record.attendanceTime).toLocaleTimeString() : '';

            html += `<tr>
                            <td>${date.toLocaleDateString()}</td>
                            <td>${record.courseName}</td>
                            <td>${time}</td>
                            <td class="${statusClass}">${record.status}</td>
                            <td>${record.remarks || ''}</td>
                            <td>${record.createdBy}</td>
                        </tr>`;
          });
          html += '</table>';
        } else {
          html = '<p>暂无考勤记录</p>';
        }
        $("#attendanceRecords").html(html);
      } else {
        $("#attendanceRecords").html(`<p class="error">${response.message}</p>`);
      }
    });
  }

  function loadStatistics() {
    // 注意：这里需要知道当前学生的学号
    $.get("api/user/current", function(userResponse) {
      if (userResponse.success) {
        const username = userResponse.user.username;

        // 请求学生考勤统计
        $.get(`api/attendance/statistics?studentId=${username}`, function(response) {
          if (response.success && response.summary) {
            const summary = response.summary;

            // 修复：使用正确的属性名（根据你的AttendanceMapper.xml中的别名）
            // 注意：这里使用的是你SQL查询中定义的别名
            const totalCount = summary.totalCount || summary.totalcount || 0;
            const presentCount = summary.presentCount || summary.presentcount || 0;
            const absentCount = summary.absentCount || summary.absentcount || 0;
            const lateCount = summary.lateCount || summary.latecount || 0;
            const attendanceRate = summary.attendanceRate || summary.attendancerate || 0;

            let html = `
                            <div class="summary-item">
                                <h4>总次数</h4>
                                <p>${totalCount}</p>
                            </div>
                            <div class="summary-item">
                                <h4>到课</h4>
                                <p class="status-present">${presentCount}</p>
                            </div>
                            <div class="summary-item">
                                <h4>缺勤</h4>
                                <p class="status-absent">${absentCount}</p>
                            </div>
                            <div class="summary-item">
                                <h4>迟到</h4>
                                <p class="status-late">${lateCount}</p>
                            </div>
                            <div class="summary-item">
                                <h4>到课率</h4>
                                <p>${attendanceRate}%</p>
                            </div>`;

            $("#summaryStats").html(html);
          } else {
            $("#summaryStats").html('<p>暂无统计信息</p>');
          }
        });
      }
    });
  }

  function logout() {
    $.post("api/user/logout", function() {
      window.location.href = "login";
    });
  }
</script>
</body>
</html>