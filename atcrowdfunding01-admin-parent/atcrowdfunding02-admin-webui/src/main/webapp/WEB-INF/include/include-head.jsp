<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<%
	pageContext.setAttribute("PATH", request.getContextPath());
%>
<link rel="stylesheet" href="${PATH }/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${PATH }/css/font-awesome.min.css">
<link rel="stylesheet" href="${PATH }/css/main.css">
<link rel="stylesheet" href="${PATH }/css/pagination.css">
<link rel="stylesheet" href="${PATH }/ztree/zTreeStyle.css">

<style>
.tree li {
	list-style-type: none;
	cursor: pointer;
}

.tree-closed {
	height: 40px;
}

.tree-expanded {
	height: auto;
}
</style>
</head>

<script src="${PATH }/jquery/jquery-3.4.1.min.js"></script>
<script src="${PATH }/bootstrap/js/bootstrap.min.js"></script>
<script src="${PATH }/script/docs.min.js"></script>
<script src="${PATH }/layer/layer.js"></script>
<script src="${PATH }/jquery/jquery.pagination.js"></script>
<script src="${PATH }/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
	$(function() {
		$(".list-group-item").click(function() {
			if ($(this).find("ul")) {
				$(this).toggleClass("tree-closed");
				if ($(this).hasClass("tree-closed")) {
					$("ul", this).hide("fast");
				} else {
					$("ul", this).show("fast");
				}
			}
		});
	});
</script>