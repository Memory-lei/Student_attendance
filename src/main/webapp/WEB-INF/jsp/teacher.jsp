<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>教师主页 - 学生考勤系统</title>
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .header { background-color: #2196F3; color: white; padding: 10px; display: flex; justify-content: space-between; }
    .menu { margin: 20px 0; }
    .menu a { margin-right: 20px; text-decoration: none; color: #333; padding: 5px 10px; border: 1px solid #ccc; }
    .menu a:hover { background-color: #f0f0f0; }
    .content { margin-top: 20px; }
    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    th { background-color: #f0f0f0; }
    .form-group { margin-bottom: 10px; }
    .form-group label { display: inline-block; width: 100px; }
    .status-present { color: green; }
    .status-absent { color: red; }
    .status-late { color: orange; }
    .btn { padding: 8px 15px; background-color: #4CAF50; color: white; border: none; cursor: pointer; margin: 5px; }
    .btn-blue { background-color: #2196F3; }
    .btn-red { background-color: #f44336; }
    .message { padding: 10px; margin: 10px 0; border-radius: 4px; }
    .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    .info { background-color: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }
  </style>
</head>
<body>
<div class="header">
  <h2>学生考勤系统 - 教师</h2>
  <div>
    欢迎，${user.username}（教师） |
    <a href="javascript:void(0)" onclick="logout()" style="color:white; text-decoration:underline;">退出</a>
  </div>
</div>

<div class="menu">
  <a href="javascript:void(0)" onclick="loadContent('record')">📝 记录考勤</a>
  <a href="javascript:void(0)" onclick="loadContent('query')">🔍 查询考勤</a>
  <a href="javascript:void(0)" onclick="loadContent('makeup')">✏️ 补签管理</a>
  <a href="javascript:void(0)" onclick="loadContent('statistics')">📊 考勤统计</a>
  <a href="javascript:void(0)" onclick="loadContent('students')">👨‍🎓 学生列表</a>
</div>

<div id="content" class="content">
  <h3>欢迎使用学生考勤系统</h3>
  <p>请选择上方菜单进行操作</p>
  <div id="messageArea"></div>
</div>

<script>
  // ==================== 辅助函数 ====================

  // 获取今天的日期字符串 (YYYY-MM-DD)
  function getTodayDateString() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  // 格式化后端返回的日期
  function formatDate(dateValue) {
    if (!dateValue) return '未知日期';
    try {
      const date = new Date(dateValue);
      return date.toLocaleDateString('zh-CN');
    } catch (e) {
      return dateValue;
    }
  }

  // 显示消息
  function showMessage(message, type = 'info') {
    const messageArea = $("#messageArea");
    messageArea.html(`<div class="message ${type}">${message}</div>`);
    if (type !== 'info') {
      setTimeout(() => messageArea.empty(), 5000);
    }
  }

  // ==================== 主功能函数 ====================

  function loadContent(type) {
    $("#messageArea").empty(); // 清空消息区域

    if (type === 'record') {
      loadRecordAttendance();
    } else if (type === 'query') {
      loadQueryAttendance();
    } else if (type === 'makeup') {
      loadMakeupAttendance();
    } else if (type === 'statistics') {
      loadStatistics();
    } else if (type === 'students') {
      loadStudentList();
    }
  }

  // 1. 记录考勤
  function loadRecordAttendance() {
    $.get("/campus_attendance_war/student/list", function(students) {
      if (!students || students.length === 0) {
        showMessage("没有找到学生数据", "error");
        return;
      }

      let html = `
                <h3>📝 记录考勤</h3>
                <div class="form-group">
                    <label>课程名称:</label>
                    <input type="text" id="courseName" value="高等数学" style="width:200px;">
                </div>
                <div class="form-group">
                    <label>考勤日期:</label>
                    <input type="date" id="attendanceDate" style="width:200px;">
                </div>
                <div class="form-group">
                    <button class="btn" onclick="selectAllStudents()">全选</button>
                    <button class="btn" onclick="deselectAllStudents()">取消全选</button>
                    <button class="btn btn-blue" onclick="setAllStatus('PRESENT')">全部到课</button>
                    <button class="btn btn-red" onclick="setAllStatus('ABSENT')">全部缺勤</button>
                </div>
                <table>
                    <tr>
                        <th>选择</th>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>班级</th>
                        <th>状态</th>
                        <th>备注</th>
                    </tr>`;

      students.forEach(s => {
        html += `<tr>
                    <td><input type="checkbox" class="student-check" data-id="${s.studentId}"></td>
                    <td>${s.studentId}</td>
                    <td>${s.name}</td>
                    <td>${s.class_}</td>
                    <td>
                        <select class="status-select">
                            <option value="PRESENT">到课</option>
                            <option value="LATE">迟到</option>
                            <option value="ABSENT">缺勤</option>
                        </select>
                    </td>
                    <td><input type="text" class="remarks" placeholder="备注" style="width:150px;"></td>
                </tr>`;
      });

      html += `</table>
                <div class="form-group">
                    <button class="btn" onclick="submitAttendance()">提交考勤</button>
                    <button class="btn" onclick="clearForm()">清空</button>
                </div>
                <div id="submitResult"></div>`;

      $("#content").html(html);
      $("#attendanceDate").val(getTodayDateString());
      showMessage("请选择学生并设置考勤状态", "info");
    }).fail(function() {
      showMessage("加载学生列表失败", "error");
    });
  }

  // 记录考勤辅助函数
  function selectAllStudents() {
    $(".student-check").prop('checked', true);
  }

  function deselectAllStudents() {
    $(".student-check").prop('checked', false);
  }

  function setAllStatus(status) {
    $(".status-select").val(status);
  }

  function clearForm() {
    $(".student-check").prop('checked', false);
    $(".status-select").val('PRESENT');
    $(".remarks").val('');
    $("#courseName").val('高等数学');
    $("#attendanceDate").val(getTodayDateString());
  }

  function submitAttendance() {
    const courseName = $("#courseName").val().trim();
    const attendanceDate = $("#attendanceDate").val();

    if (!courseName) {
      showMessage("请输入课程名称", "error");
      return;
    }

    if (!attendanceDate) {
      showMessage("请选择考勤日期", "error");
      return;
    }

    const records = [];
    $(".student-check:checked").each(function() {
      const row = $(this).closest('tr');
      const studentId = row.find('td').eq(1).text();
      const status = row.find('.status-select').val();
      const remarks = row.find('.remarks').val().trim();

      records.push({
        studentId: studentId,
        courseName: courseName,
        attendanceDate: new Date(attendanceDate).getTime(),
        status: status,
        remarks: remarks || ''
      });
    });

    if (records.length === 0) {
      showMessage("请至少选择一名学生", "error");
      return;
    }

    showMessage(`正在提交 ${records.length} 条考勤记录...`, "info");

    // 逐条提交（更稳定）
    let successCount = 0;
    let failCount = 0;
    let processed = 0;

    function submitNext() {
      if (processed >= records.length) {
        const result = `
                    <div class="message success">
                        <h4>提交完成！</h4>
                        <p>成功: ${successCount}条 | 失败: ${failCount}条</p>
                    </div>`;
        $("#submitResult").html(result);
        return;
      }

      const record = records[processed];
      $.ajax({
        url: '/campus_attendance_war/api/attendance/record',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(record),
        success: function(response) {
          if (response.success) {
            successCount++;
          } else {
            failCount++;
          }
          processed++;
          submitNext();
        },
        error: function() {
          failCount++;
          processed++;
          submitNext();
        }
      });
    }

    submitNext();
  }

  // 2. 查询考勤
  function loadQueryAttendance() {
    let html = `
            <h3>🔍 查询考勤</h3>
            <div class="form-group">
                <label>查询方式:</label>
                <select id="queryType" onchange="changeQueryType()">
                    <option value="date">按日期查询</option>
                    <option value="student">按学生查询</option>
                    <option value="course">按课程查询</option>
                </select>
            </div>
            <div id="queryParams"></div>
            <div class="form-group">
                <button class="btn" onclick="queryAttendance()">查询</button>
                <button class="btn" onclick="clearQuery()">清空</button>
            </div>
            <div id="queryResult"></div>`;

    $("#content").html(html);
    changeQueryType();
  }

  function changeQueryType() {
    const type = $("#queryType").val();
    let html = '';

    if (type === 'date') {
      html = `
                <div class="form-group">
                    <label>查询日期:</label>
                    <input type="date" id="queryDate">
                </div>`;
    } else if (type === 'student') {
      html = `
                <div class="form-group">
                    <label>学生学号:</label>
                    <input type="text" id="queryStudentId" placeholder="输入学号">
                </div>`;
    } else if (type === 'course') {
      html = `
                <div class="form-group">
                    <label>课程名称:</label>
                    <input type="text" id="queryCourseName" placeholder="输入课程名称">
                </div>`;
    }

    $("#queryParams").html(html);
    if (type === 'date') {
      $("#queryDate").val(getTodayDateString());
    }
  }

  function clearQuery() {
    $("#queryParams input").val('');
    $("#queryResult").empty();
  }

  function queryAttendance() {
    const type = $("#queryType").val();
    let url = '/campus_attendance_war/api/attendance/query?';

    if (type === 'date') {
      const date = $("#queryDate").val();
      if (!date) {
        showMessage("请选择查询日期", "error");
        return;
      }
      url += `date=${date}`;
    } else if (type === 'student') {
      const studentId = $("#queryStudentId").val().trim();
      if (!studentId) {
        showMessage("请输入学号", "error");
        return;
      }
      url += `studentId=${studentId}`;
    } else if (type === 'course') {
      const courseName = $("#queryCourseName").val().trim();
      if (!courseName) {
        showMessage("请输入课程名称", "error");
        return;
      }
      // 注意：这里需要后端支持按课程查询，暂时用日期查询代替
      url += `date=${getTodayDateString()}`;
    }

    showMessage("正在查询...", "info");

    $.get(url, function(response) {
      if (response.success) {
        let html = '<h4>查询结果</h4>';
        if (response.records && response.records.length > 0) {
          html += `<p>共找到 ${response.records.length} 条记录</p>`;
          html += '<table><tr><th>学号</th><th>课程</th><th>日期</th><th>时间</th><th>状态</th><th>备注</th><th>记录人</th></tr>';

          response.records.forEach(record => {
            const statusClass = 'status-' + record.status.toLowerCase();
            const dateStr = formatDate(record.attendanceDate);
            const timeStr = record.attendanceTime ? formatDate(record.attendanceTime).split(' ')[1] || '' : '';

            html += `<tr>
                            <td>${record.studentId}</td>
                            <td>${record.courseName}</td>
                            <td>${dateStr}</td>
                            <td>${timeStr}</td>
                            <td class="${statusClass}">${record.status}</td>
                            <td>${record.remarks || ''}</td>
                            <td>${record.createdBy || '系统'}</td>
                        </tr>`;
          });
          html += '</table>';
        } else {
          html += '<div class="message info">暂无考勤记录</div>';
        }
        $("#queryResult").html(html);
      } else {
        $("#queryResult").html(`<div class="message error">${response.message || "查询失败"}</div>`);
      }
    }).fail(function() {
      $("#queryResult").html('<div class="message error">查询失败，请检查网络连接</div>');
    });
  }

  // 3. 补签管理
  function loadMakeupAttendance() {
    let html = `
            <h3>✏️ 补签管理</h3>
            <div class="form-group">
                <label>学生学号:</label>
                <input type="text" id="makeupStudentId" placeholder="输入学号" style="width:200px;">
            </div>
            <div class="form-group">
                <label>课程名称:</label>
                <input type="text" id="makeupCourse" placeholder="输入课程名称" style="width:200px;">
            </div>
            <div class="form-group">
                <label>补签日期:</label>
                <input type="date" id="makeupDate" style="width:200px;">
            </div>
            <div class="form-group">
                <label>考勤状态:</label>
                <select id="makeupStatus" style="width:200px;">
                    <option value="PRESENT">到课</option>
                    <option value="LATE">迟到</option>
                    <option value="ABSENT">缺勤</option>
                </select>
            </div>
            <div class="form-group">
                <label>备注:</label>
                <input type="text" id="makeupRemarks" placeholder="补签原因" style="width:300px;">
            </div>
            <div class="form-group">
                <button class="btn" onclick="submitMakeup()">提交补签</button>
                <button class="btn" onclick="clearMakeupForm()">清空</button>
            </div>
            <div id="makeupResult"></div>`;

    $("#content").html(html);
    $("#makeupDate").val(getTodayDateString());
    showMessage("请填写补签信息", "info");
  }

  function clearMakeupForm() {
    $("#makeupStudentId").val('');
    $("#makeupCourse").val('');
    $("#makeupRemarks").val('');
    $("#makeupDate").val(getTodayDateString());
    $("#makeupStatus").val('PRESENT');
    $("#makeupResult").empty();
  }

  function submitMakeup() {
    const studentId = $("#makeupStudentId").val().trim();
    const courseName = $("#makeupCourse").val().trim();
    const date = $("#makeupDate").val();
    const status = $("#makeupStatus").val();
    const remarks = $("#makeupRemarks").val().trim();

    if (!studentId || !courseName || !date) {
      showMessage("请填写完整信息（学号、课程、日期必填）", "error");
      return;
    }

    showMessage("正在提交补签...", "info");

    $.ajax({
      url: '/campus_attendance_war/api/attendance/makeup',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({
        studentId: studentId,
        courseName: courseName,
        attendanceDate: new Date(date).getTime(),
        status: status,
        remarks: remarks || '教师补签'
      }),
      success: function(response) {
        if (response.success) {
          $("#makeupResult").html('<div class="message success">补签成功！</div>');
          clearMakeupForm();
        } else {
          $("#makeupResult").html(`<div class="message error">${response.message || "补签失败"}</div>`);
        }
      },
      error: function() {
        $("#makeupResult").html('<div class="message error">提交失败，请检查网络连接</div>');
      }
    });
  }

  // 4. 考勤统计
  function loadStatistics() {
    showMessage("正在加载统计信息...", "info");

    // 获取最近7天的统计
    const endDate = getTodayDateString();
    const startDate = new Date();
    startDate.setDate(startDate.getDate() - 7);
    const startDateStr = startDate.toISOString().split('T')[0];

    $.get(`/campus_attendance_war/api/attendance/statistics?startDate=${startDateStr}&endDate=${endDate}`, function(response) {
      let html = '<h3>📊 考勤统计（最近7天）</h3>';

      if (response.success && response.statistics && response.statistics.length > 0) {
        // 计算总计
        let totalCount = 0, presentCount = 0, absentCount = 0, lateCount = 0;

        html += '<table><tr><th>日期</th><th>总人数</th><th>到课</th><th>缺勤</th><th>迟到</th><th>到课率</th></tr>';

        response.statistics.forEach(stat => {
          const total = stat.total || stat.totalcount || 0;
          const present = stat.present || stat.presentcount || 0;
          const absent = stat.absent || stat.absentcount || 0;
          const late = stat.late || stat.latecount || 0;
          const rate = total > 0 ? ((present * 100) / total).toFixed(2) : 0;

          totalCount += total;
          presentCount += present;
          absentCount += absent;
          lateCount += late;

          const dateStr = formatDate(stat.date);

          html += `<tr>
                        <td>${dateStr}</td>
                        <td>${total}</td>
                        <td class="status-present">${present}</td>
                        <td class="status-absent">${absent}</td>
                        <td class="status-late">${late}</td>
                        <td>${rate}%</td>
                    </tr>`;
        });

        // 总计行
        const totalRate = totalCount > 0 ? ((presentCount * 100) / totalCount).toFixed(2) : 0;
        html += `<tr style="font-weight:bold; background-color:#f0f0f0;">
                    <td>总计</td>
                    <td>${totalCount}</td>
                    <td class="status-present">${presentCount}</td>
                    <td class="status-absent">${absentCount}</td>
                    <td class="status-late">${lateCount}</td>
                    <td>${totalRate}%</td>
                </tr>`;

        html += '</table>';

        // 统计摘要
        html += `
                    <div class="message info" style="margin-top:20px;">
                        <h4>统计摘要</h4>
                        <p>总考勤人次: ${totalCount} | 到课率: ${totalRate}%</p>
                        <p>到课: ${presentCount} (${totalCount > 0 ? ((presentCount*100)/totalCount).toFixed(1) : 0}%)</p>
                        <p>缺勤: ${absentCount} (${totalCount > 0 ? ((absentCount*100)/totalCount).toFixed(1) : 0}%)</p>
                        <p>迟到: ${lateCount} (${totalCount > 0 ? ((lateCount*100)/totalCount).toFixed(1) : 0}%)</p>
                    </div>`;
      } else {
        html += '<div class="message info">暂无考勤统计数据</div>';
      }

      $("#content").html(html);
    }).fail(function() {
      $("#content").html('<div class="message error">加载统计信息失败</div>');
    });
  }

  // 5. 学生列表
  function loadStudentList() {
    $.get("/campus_attendance_war/student/list", function(students) {
      let html = '<h3>👨‍🎓 学生列表</h3>';

      if (students && students.length > 0) {
        html += `<p>共 ${students.length} 名学生</p>`;
        html += '<table><tr><th>学号</th><th>姓名</th><th>班级</th><th>专业</th></tr>';

        students.forEach(s => {
          html += `<tr>
                        <td>${s.studentId}</td>
                        <td>${s.name}</td>
                        <td>${s.class_}</td>
                        <td>${s.major}</td>
                    </tr>`;
        });

        html += '</table>';
      } else {
        html += '<div class="message info">暂无学生数据</div>';
      }

      $("#content").html(html);
    }).fail(function() {
      $("#content").html('<div class="message error">加载学生列表失败</div>');
    });
  }

  // ==================== 系统函数 ====================

  function logout() {
    if (confirm("确定要退出登录吗？")) {
      $.post("/campus_attendance_war/api/user/logout", function() {
        window.location.href = "/campus_attendance_war/login";
      });
    }
  }

  // 页面加载时检查登录状态
  $(function() {
    // 修正登录状态检查（纯EL表达式，不在JS中混用）
    <c:if test="${user == null}">
    window.location.href = "/campus_attendance_war/login";
    return;
    </c:if>

    // 修正欢迎信息提取（直接使用EL表达式传递用户名）
    showMessage(`欢迎 ${'${user.username}'} 老师！`, "success");

    // 自动加载学生列表
    loadStudentList();
  });
</script>
</body>
</html>