<!-- WEB-INF/jsp/login.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>登录 - 学生考勤系统</title>
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
  <style>
    body { font-family: Arial, sans-serif; margin: 50px; }
    .container { max-width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
    .form-group { margin-bottom: 15px; }
    label { display: block; margin-bottom: 5px; }
    input[type="text"], input[type="password"] { width: 100%; padding: 8px; box-sizing: border-box; }
    button { width: 100%; padding: 10px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }
    .error { color: red; margin-top: 10px; }
    .success { color: green; margin-top: 10px; }
  </style>
</head>
<body>
<div class="container">
  <h2>学生考勤系统登录</h2>
  <div class="form-group">
    <label>用户名:</label>
    <input type="text" id="username" placeholder="请输入用户名">
  </div>
  <div class="form-group">
    <label>密码:</label>
    <input type="password" id="password" placeholder="请输入密码">
  </div>
  <button onclick="login()">登录</button>
  <button onclick="goToRegister()" style="margin-top: 10px; background-color: #2196F3;">注册</button>
  <div id="message" class="error"></div>
</div>

<script>
  function login() {
    const username = $("#username").val();
    const password = $("#password").val();

    if (!username || !password) {
      $("#message").text("用户名和密码不能为空").addClass("error").removeClass("success");
      return;
    }

    $.ajax({
      url: "/campus_attendance_war/api/user/login",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({username: username, password: password}),
      success: function(response) {
        if (response.success) {
          $("#message").text("登录成功，正在跳转...").addClass("success").removeClass("error");
          // 根据角色跳转到不同页面
          if (response.role === "ADMIN") {
            window.location.href = "/campus_attendance_war/admin";
          } else if (response.role === "TEACHER") {
            window.location.href = "/campus_attendance_war/teacher";
          } else {
            window.location.href = "/campus_attendance_war/student";
          }
        } else {
          $("#message").text(response.message).addClass("error").removeClass("success");
        }
      },
      error: function() {
        $("#message").text("登录失败，请检查网络").addClass("error").removeClass("success");
      }
    });
  }

  function goToRegister() {
    // 使用上下文路径拼接正确的注册页面映射路径
    window.location.href = "${pageContext.request.contextPath}/register";
  }

  // 回车键登录
  $("#password").keypress(function(e) {
    if (e.which == 13) {
      login();
    }
  });
</script>
</body>
</html>