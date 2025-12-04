<!-- WEB-INF/jsp/register.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>注册 - 学生考勤系统</title>
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
  <style>
    body { font-family: Arial, sans-serif; margin: 50px; }
    .container { max-width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
    .form-group { margin-bottom: 15px; }
    label { display: block; margin-bottom: 5px; }
    input[type="text"], input[type="password"], select { width: 100%; padding: 8px; box-sizing: border-box; }
    button { width: 100%; padding: 10px; background-color: #2196F3; color: white; border: none; cursor: pointer; }
    .error { color: red; margin-top: 10px; }
    .success { color: green; margin-top: 10px; }
  </style>
</head>
<body>
<div class="container">
  <h2>用户注册</h2>
  <div class="form-group">
    <label>用户名:</label>
    <input type="text" id="username" placeholder="请输入用户名">
  </div>
  <div class="form-group">
    <label>密码:</label>
    <input type="password" id="password" placeholder="请输入密码（至少6位）">
  </div>
  <div class="form-group">
    <label>确认密码:</label>
    <input type="password" id="confirmPassword" placeholder="请再次输入密码">
  </div>
  <div class="form-group">
    <label>角色:</label>
    <select id="role">
      <option value="STUDENT">学生</option>
      <option value="TEACHER">教师</option>
      <option value="ADMIN">管理员</option>
    </select>
  </div>
  <button onclick="register()">注册</button>
  <button onclick="goToLogin()" style="margin-top: 10px; background-color: #4CAF50;">返回登录</button>
  <div id="message" class="error"></div>
</div>

<script>
  function register() {
    const username = $("#username").val();
    const password = $("#password").val();
    const confirmPassword = $("#confirmPassword").val();
    const role = $("#role").val();

    // 简单验证
    if (!username || username.length < 3) {
      $("#message").text("用户名至少3位").addClass("error").removeClass("success");
      return;
    }

    if (!password || password.length < 6) {
      $("#message").text("密码至少6位").addClass("error").removeClass("success");
      return;
    }

    if (password !== confirmPassword) {
      $("#message").text("两次密码不一致").addClass("error").removeClass("success");
      return;
    }

    $.ajax({
      // 正确写法：拼接上下文路径
      url: "${pageContext.request.contextPath}/api/user/register",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        username: username,
        password: password,
        role: role
      }),
      success: function(response) {
        if (response.success) {
          $("#message").text("注册成功，请登录").addClass("success").removeClass("error");
          // 注册成功后跳转登录页（正确写法）
          setTimeout(function() {
            window.location.href = "${pageContext.request.contextPath}/login";
          }, 2000);

        } else {
          $("#message").text(response.message).addClass("error").removeClass("success");
        }
      },
      error: function() {
        $("#message").text("注册失败，请检查网络").addClass("error").removeClass("success");
      }
    });
  }

  function goToLogin() {
    window.location.href = "${pageContext.request.contextPath}/login";
  }
</script>
</body>
</html>