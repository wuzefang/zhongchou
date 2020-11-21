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


	<div class="container" style="text-align: center;">
		<h3>系统信息页面</h3>
		<h4>${requestScope.exception.message }</h4>
		<button style="width: 300px; margin: 0px auto 0px auto;"
			class="btn btn-lg btn-success btn-block">返回上一层</button>
	</div>
</body>
<script src="${PATH }/jquery/jquery-3.4.1.min.js"></script>
<script src="${PATH }/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("button").click(function() {
			// 调用 back()方法类似于点击浏览器的后退按钮
			window.history.back();
		});
	});
</script>
</html>