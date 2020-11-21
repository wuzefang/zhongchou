<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include/include-head.jsp"%>
<%@include file="/WEB-INF/include/model-add-role.jsp"%>
<%@include file="/WEB-INF/include/model-edit-role.jsp"%>
<%@include file="/WEB-INF/include/model-remove-role.jsp"%>
<%@include file="/WEB-INF/include/model-assign-role-auth.jsp"%>

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
						<form class="form-inline" role="form" style="float: left;">
							<div class="form-group has-feedback">
								<div class="input-group">
									<div class="input-group-addon">查询条件</div>
									<input id="keywordInput" class="form-control has-success"
										type="text" placeholder="请输入查询条件">
								</div>
							</div>
							<button id="searchButton" type="button" class="btn btn-warning">
								<i class="glyphicon glyphicon-search"></i> 查询
							</button>
						</form>
						<button type="button" id="batchRemoveBtu" class="btn btn-danger"
							style="float: right; margin-left: 10px;">
							<i class=" glyphicon glyphicon-remove"></i> 删除
						</button>
						<button id="showAddModalBtn" type="button" class="btn btn-primary"
							style="float: right;">
							<i class="glyphicon glyphicon-plus"></i> 新增
						</button>
						<br>
						<hr style="clear: both;">
						<div class="table-responsive">
							<table class="table  table-bordered">
								<thead>
									<tr>
										<th width="30">#</th>
										<th width="30"><input id="sumCheckBox" type="checkbox"></th>
										<th>名称</th>
										<th width="100">操作</th>
									</tr>
								</thead>
								<tbody id="rolePageBody">
									<!-- js填充查询结果 -->
								</tbody>
								<tfoot>
									<tr>
										<td colspan="6" align="center">
											<div id="Pagination" class="pagination">
												<!-- 分页条信息 -->
											</div> <!-- <ul class="pagination">
												<li class="disabled"><a href="#">上一页</a></li>
												<li class="active"><a href="#">1 <span
														class="sr-only">(current)</span></a></li>
												<li><a href="#">2</a></li>
												<li><a href="#">3</a></li>
												<li><a href="#">4</a></li>
												<li><a href="#">5</a></li>
												<li><a href="#">下一页</a></li>
											</ul> -->
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
<script type="text/javascript" src="${PATH }/crowd/js/my-role.js"></script>
<script type="text/javascript">
	$(function() {
		//layer.msg(1);
		// 全局变量设置，默认值
		window.pageNum = 1;
		window.pageSice = 5;
		window.keyword = "";
		// 项目路径
		window.path = "${PATH}";
		//alert(path);
		generatePage();

		// 点击查询按钮，查询数据
		$("#searchButton").click(function() {
			// alert(1);
			// 更新全局的keyword值即可
			window.keyword = $("#keywordInput").val();
			//重新分页
			window.pageNum = 1;
			generatePage();
		});
		// 点击 新增 按钮弹出模态框
		$("#showAddModalBtn").click(function() {
			$("#addModal").modal("show");
		});

		// 在模态框点击 保存 按钮，更新页面
		$("#saveRoleBtn").click(function() {
			var roleName = $.trim($("#add_roleName_input").val());

			$.ajax({
				url : path + "/role/save.json",
				type : "POST",
				data : {
					"tRole" : roleName
				},
				dataType : "json",
				success : function(response) {
					if ("SUCCESS" == response.result) {
						layer.msg("更新成功！");
						// 重新分页
						window.pageNum = 99999;
						generatePage();
					}
					if ("ERROR" == response.result) {
						layer.msg("更新失败！" + response.message);
					}
				},
				error : function(response) {
					layer.msg("操作失败：" + resepon.status + resepon.statusText);
				}
			});
			// 关闭模态框
			$("#addModal").modal("hide");
			// 清理模态框
			$("#add_roleName_input").val("");

		});

		// 角色修改
		// 由于所有的分页数据都是用过ajax动态生成的，所以直接获取标签是获取不到的，所有要通过父标签调用on函数来进行动态获取
		$("#rolePageBody").on("click", ".pencilBtn", function() {
			// alert(111)
			// 打开模态框
			$("#editModal").modal("show");
			// 获取当前行的角色名称
			var roleName = $(this).parent().prev().text();
			// alert(roleName)
			// 数据进行回显
			window.roleID = this.id;
			// alert(roleID);
			$("#edit_roleName_input").val(roleName);
		});
		// 执行更新
		$("#updateRoleBtn").click(function() {
			var roleName = $("#edit_roleName_input").val();

			$.ajax({
				url : "${PATH}/role/update.json",
				type : "POST",
				data : {
					"id" : roleID,
					"tRole" : roleName
				},
				dataType : "json",
				success : function(response) {
					if ("SUCCESS" == response.result) {
						layer.msg("更新成功！");
						// 重新分页
						generatePage();
					}
					if ("ERROR" == response.result) {
						layer.msg("更新失败！" + response.message);
					}
				},
				error : function(response) {
					layer.msg("操作失败：" + resepon.status + resepon.statusText);
				}
			});

			// 关闭模态框
			$("#editModal").modal("hide");
		});
		// 删除员工
		$("#removeRoleBtn").click(function() {
			// 从全局变量中获取到数组，并转换成json字符串
			var requestBody = JSON.stringify(window.roleIdArray);
			// 发送ajax请求
			$.ajax({
				url : "${PATH}/role/remove/roleIdArray.json",
				type : "POST",
				data : requestBody,
				contentType : "application/json;charset=UTF-8",
				dataType : "JSON",
				success : function(response) {
					if ("SUCCESS" == response.result) {
						layer.msg("更新成功！");
						// 重新分页
						generatePage();
					}
					if ("ERROR" == response.result) {
						layer.msg("更新失败！" + response.message);
					}
				},
				error : function(response) {
					layer.msg("操作失败：" + resepon.status + resepon.statusText);
				}

			});
			// 关闭模态框
			$("#removeModal").modal("hide");
		});
		$("#rolePageBody").on("click", ".removeBtn", function() {

			var roleName = $(this).parent().prev().text();
			// 创建role对象，存入数组
			var roleArray = [ {
				id : this.id,
				tRole : roleName
			} ];
			//console.log(roleArray);
			showConfirmModal(roleArray)

		});

		// 全选按钮实现
		$("#sumCheckBox").click(function() {
			// 获取当前按钮的自身状态
			var curStatus = this.checked;

			$(".itemCheckBox").prop("checked", curStatus);
		});
		// 全部 ，全不选，反向操作
		$("#rolePageBody").on("click", ".itemCheckBox", function() {
			// 获取当前选已经选中的数量
			var checkedCount = $(".itemCheckBox:checked").length;
			// 获取选择按钮的总数
			var totalCount = $(".itemCheckBox").length;

			$("#sumCheckBox").prop("checked", checkedCount == totalCount);

		});
		// 批量删除的按钮
		$("#batchRemoveBtu").click(function() {

			// 创建要删除的数组
			var roleArray = [];
			// 遍历当前选中的
			$(".itemCheckBox:checked").each(function() {
				var roleId = this.id;
				var roleName = $(this).parent().next().text();
				roleArray.push({
					id : roleId,
					tRole : roleName
				});
			});
			// 检查选择的个数
			if (roleArray.length == 0) {
				layer.msg("请至少选择一个角色！");
				return null;
			}
			console.log(roleArray);
			showConfirmModal(roleArray);
		});
		// 13.给分配权限按钮绑定单击响应函数
		$("#rolePageBody").on("click", ".checkBtn", function() {
			window.roleID = this.id;
			// 打开模态框
			$("#assignModal").modal("show");
			// 在模态框中装载树 Auth 的形结构数据
			fillAuthTree();
		});
		// 14.给分配权限模态框中的“分配”按钮绑定单击响应函数
		$("#assignBtn").click(function() {
			// ①收集树形结构的各个节点中被勾选的节点
			// [1]声明一个专门的数组存放 id
			var authIdArray = [];
			// [2]获取 zTreeObj 对象
			var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
			// [3]获取全部被勾选的节点
			var checkedNodes = zTreeObj.getCheckedNodes();

			// [4]遍历 checkedNodes
			for (var i = 0; i < checkedNodes.length; i++) {
				var checkedNode = checkedNodes[i];
				var authId = checkedNode.id;
				authIdArray.push(authId);
			}
			// alert(authIdArray);
			// ②发送请求执行分配
			var requestBody = {
				"authIdArray" : authIdArray, // 为了服务器端 handler 方法能够统一使用 List<Integer>方式接收数据，roleId 也存入数组
				"roleId" : [ window.roleID ]
			}

			requestBody = JSON.stringify(requestBody);
			$.ajax({
				"url" : "${PATH}/assign/do/role/assign/auth.json",
				"type" : "post",
				"data" : requestBody,
				"contentType" : "application/json;charset=UTF-8",
				"dataType" : "json",
				"success" : function(response) {
					var result = response.result;
					if (result == "SUCCESS") {
						layer.msg("操作成功！");
					}
					if (result == "ERROR") {
						layer.msg("操作失败！" + response.message);
					}
				},
				"error" : function(response) {
					layer.msg(response.status + " " + response.statusText);
				}
			});
			$("#assignModal").modal("hide");

		});
	});
</script>

</html>
