<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include/include-head.jsp"%>

<body>
	<%@include file="/WEB-INF/include/include-nav.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/include/include-sidebar.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-th"></i> 数据列表
						</h3>
					</div>
					<div class="panel-body">
						<form action="${PATH }/admin/get/page.html" method="post"
							class="form-inline" role="form" style="float: left;">
							<div class="form-group has-feedback">
								<div class="input-group">
									<div class="input-group-addon">查询条件</div>
									<input name="keyword" class="form-control has-success"
										type="text" placeholder="请输入查询条件">
								</div>
							</div>
							<button type="submit" class="btn btn-warning">
								<i class="glyphicon glyphicon-search"></i> 查询
							</button>
						</form>


						<button type="button" class="btn btn-danger"
							style="float: right; margin-left: 10px;">
							<i class=" glyphicon glyphicon-remove"></i> 删除
						</button>
					
						<a  href="${PATH }/admin/add/page.html" class="btn btn-primary" style="float: right;" onclick="window.location.href='add.html'" >
							<i class="glyphicon glyphicon-plus"></i> 新增
						</a>
						<br>
						<hr style="clear: both;">
						<div class="table-responsive">
							<table class="table  table-bordered">
								<thead>
									<tr>
										<th width="30">#</th>
										<th width="30"><input type="checkbox"></th>
										<th>账号</th>
										<th>名称</th>
										<th>邮箱地址</th>
										<th width="100">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${empty requestScope.pageInfo.list }">
										<tr>
											<td colspan="6" align="center">抱歉！没有查询到您需要的数据！</td>
										</tr>
									</c:if>
									<c:if test="${!empty requestScope.pageInfo.list }">
										<c:forEach items="${requestScope.pageInfo.list }" var="admin"
											varStatus="myStatus">
											<tr>
												<td>${myStatus.count }</td>
												<td><input type="checkbox"></td>
												<td>${admin.loginAcct  }</td>
												<td>${admin.userName }</td>
												<td>${admin.email }</td>
												<td>
													<!-- <button type="button" class="btn btn-success btn-xs">
														<i class=" glyphicon glyphicon-check"></i>
													</button> -->
													<a href="${PATH }/assign/to/assign/role/page.html?adminId=${admin.id }&pageNum=${pageInfo.pageNum }&keyword=${param.keyword }
														 type="button" class="btn btn-success btn-xs">
														<i class="glyphicon glyphicon-check"></i>
													</a>
													<!-- <button type="button" class="btn btn-primary btn-xs">
														<i class=" glyphicon glyphicon-pencil"></i>
													</button> -->
													<a href="${PATH }/admin/to/edit/page.html?adminId=${admin.id }&pageNum=${pageInfo.pageNum }&keyword=${param.keyword }
														 type="button" class="btn btn-primary btn-xs">
														<i class=" glyphicon glyphicon-pencil"></i>
													</a>
													<a href="${PATH }/admin/remove/${admin.id }/${requestScope.pageInfo.pageNum }/${param.keyword }.html"
													class="btn btn-danger btn-xs"> <i
														class=" glyphicon glyphicon-remove"></i>
													</a>
												</td>
											</tr>
										</c:forEach>
									</c:if>


								</tbody>
								<tfoot>
									<tr>
										<td colspan="6" align="center">
											<ul class="pagination">
												<c:if test="${pageInfo.hasPreviousPage }">
													<li><a
														href="${PATH }/admin/get/page.html?pageNum=1&keyword=${param.keyword}">首页</a></li>
													<li><a
														href="${PATH }/admin/get/page.html?pageNum=${pageInfo.pageNum  - 1}&keyword=${param.keyword} "
														aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
													</a></li>
												</c:if>

												<c:forEach items="${pageInfo.navigatepageNums }"
													var="pageNumber">
													<c:if test="${pageNumber == pageInfo.pageNum }">
														<li class="active"><a href="#">${pageNumber }</a></li>
													</c:if>
													<c:if test="${pageNumber != pageInfo.pageNum }">
														<li><a
															href="${PATH }/admin/get/page.html?pageNum=${pageNumber }&keyword=${param.keyword}">${pageNumber }</a></li>
													</c:if>

												</c:forEach>
												<c:if test="${pageInfo.hasNextPage }">
													<li><a
														href="${PATH }/admin/get/page.html?pageNum=${pageInfo.pageNum + 1}&keyword=${param.keyword}"
														aria-label="Next"> <span aria-hidden="true">&raquo;</span>
													</a></li>
													<li><a
														href="${PATH }/admin/get/page.html?pageNum=${pageInfo.pages}&keyword=${param.keyword}">末页</a></li>
												</c:if>
											</ul>
										</td>
									</tr>

								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
