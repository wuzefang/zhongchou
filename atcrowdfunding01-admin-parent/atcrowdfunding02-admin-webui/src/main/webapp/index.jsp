<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
<%
	pageContext.setAttribute("PATH", request.getContextPath());
%>
</head>

<body>
	<a href="${PATH }/test/ssm.html">testRequest</a>
	<a href="${PATH }/test/ssm.json">testJson</a>
	
	<button id="btn1" onclick="layer">layer弹框</button>
	
</body>
<script type="text/javascript" src="${PATH }/jquery/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="${PATH }/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		$("#btn1").click(function() {
			//alert(1);
			layer.msg(1)
		})
		
	});
	
</script>
</html>