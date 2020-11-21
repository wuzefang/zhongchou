<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="keys" content="">
<meta name="author" content="">
<%
	pageContext.setAttribute("PATH", request.getContextPath());
%>
<link rel="stylesheet" href="${PATH }/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${PATH }/css/font-awesome.min.css">
<link rel="stylesheet" href="${PATH }/css/login.css">
<style>
</style>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<div>
					<a class="navbar-brand" href="index.html" style="font-size: 32px;">尚筹网-创意产品众筹平台</a>
				</div>
			</div>
		</div>
	</nav>

	<div class="container">

		<form action="${PATH }/security/do/login.html" method="post"
			class="form-signin" role="form">
			<h2 class="form-signin-heading">
				<i class="glyphicon glyphicon-log-in"></i> 管理员登录 <br>
				<p style="font-size: 5px; color: red; text-align: center">${requestScope.exception.message }</p> <!-- 以前使用的 -->
				<p style="font-size: 5px; color: red; text-align: center">${SPRING_SECURITY_LAST_EXCEPTION.message }</p> <!-- SpringSecurity使用的 -->
			</h2>
			<div class="form-group has-success has-feedback">
				<input name="loginAcct" type="text" class="form-control"
					id="inputSuccess4" placeholder="请输入登录账号" autofocus> <span
					class="glyphicon glyphicon-user form-control-feedback"></span>
			</div>
			<div class="form-group has-success has-feedback">
				<input type="text" name="userPswd" class="form-control"
					id="inputSuccess4" placeholder="请输入登录密码" style="margin-top: 10px;">
				<span class="glyphicon glyphicon-lock form-control-feedback"></span>
			</div>
			<button type="submit" class="btn btn-lg btn-success btn-block"
				href="main.html">登录</button>
		</form>
	</div>

</body>
</html>