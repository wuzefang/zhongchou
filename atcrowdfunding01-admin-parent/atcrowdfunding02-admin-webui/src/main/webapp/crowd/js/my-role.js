// 声明专门的函数用来在分配 Auth 的模态框中显示 Auth 的树形结构数据
function fillAuthTree() {
	// 1.发送 Ajax 请求查询 Auth 数据
	var ajaxReturn = $.ajax({
		"url" : window.path + "/assign/get/all/auth.json",
		"type" : "post",
		"dataType" : "json",
		"async" : false
	});
	if (ajaxReturn.status != 200) {
		layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： " + ajaxReturn.status
				+ " 说 明 是 ：" + ajaxReturn.statusText);
		return;
	}
	// 2.从响应结果中获取 Auth 的 JSON 数据

	// 从服务器端查询到的 list 不需要组装成树形结构，这里我们交给 zTree 去组装
	var authList = ajaxReturn.responseJSON.data;
	console.log(ajaxReturn);
	// 3.准备对 zTree 进行设置的 JSON 对象
	var setting = {
		"data" : {
			"simpleData" : {
				// 开启简单 JSON 功能
				"enable" : true, // 使用 categoryId 属性关联父节点，不用默认的 pId 了
				"pIdKey" : "categoryId"
			},
			"key" : {
				// 使用 title 属性显示节点名称，不用默认的 name 作为属性名了
				"name" : "title"
			}
		},
		"check" : {
			"enable" : true
		}
	};
	// 4.生成树形结构
	// <ul id="authTreeDemo" class="ztree"></ul>
	$.fn.zTree.init($("#authTreeDemo"), setting, authList);
	// 获取 zTreeObj 对象
	var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
	
	// 调用 zTreeObj 对象的方法，把节点展开
	zTreeObj.expandAll(true);
	// 5.查询已分配的 Auth 的 id 组成的数组
	ajaxReturn = $.ajax({
		"url" : window.path + "/assign/get/assigned/auth/id/by/role/id.json",
		"type" : "post",
		"data" : {
			"roleId" : window.roleID
		},
		"dataType" : "json",
		"async" : false
	});
	if (ajaxReturn.status != 200) {
		layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： " + ajaxReturn.status
				+ " 说 明 是 ：" + ajaxReturn.statusText);
		return;
	}
	// 从响应结果中获取 authIdArray
	var authIdArray = ajaxReturn.responseJSON.data;
	// 6.根据 authIdArray 把树形结构中对应的节点勾选上
	// ①遍历 authIdArray
	for (var i = 0; i < authIdArray.length; i++) {
		var authId = authIdArray[i];
		// ②根据 id 查询树形结构中对应的节点
		var treeNode = zTreeObj.getNodeByParam("id", authId);
		// ③将 treeNode 设置为被勾选
		// checked 设置为 true 表示节点勾选
		var checked = true;
		// checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
		var checkTypeFlag = false;
		// 执行
		zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
	}

}

function showConfirmModal(roleArray) {
	// 打开模态框
	$("#removeModal").modal("show");
	// 清空显示框
	$("#roleNameDiv").empty();
	window.roleIdArray = [];

	for (var i = 0; i < roleArray.length; i++) {
		var role = roleArray[i];
		var roleName = role.tRole;
		$("#roleNameDiv").append(roleName + "<br/>")

		var roleId = role.id;
		// alert(roleId)
		roleIdArray.push(roleId);
	}
}

// 执行分页，生成页面效果，任何时候代用这个函数都会重新加载页面
function generatePage() {

	$("#sumCheckBox").prop("checked", false);

	// 1、获取分页数据
	var pageInfo = getPageInfoRemote();

	// 2、填充表格
	// fillTableBody(pageInfo);
}
// 远程获取pageInfo数据
function getPageInfoRemote() {
	// 发送ajax请求
	$.ajax({
		url : window.path + "/role/get/page/info.json",

		type : "POST",
		data : {
			pageNum : window.pageNum,
			pageSize : window.pageSize,
			keyword : window.keyword
		},
		dataType : "JSON",
		success : function(resepon) {
			console.log(resepon)
			// alert(resepon);
			if ("SUCCESS" == resepon.result) {
				//alert(1);
			//	layer.msg("成功："+result.status+result.statusText)
				var pageInfo = resepon.data;
				console.log(pageInfo);
				// 调用填充方法
				fillTableBody(pageInfo);
			}
			if ("ERROR" == resepon.result) {
				layer.msg( resepon.message);
				
				//alert(resepon.message);
			}
		},
		error : function(resepon) {
			// console.log(result)
			layer.msg("失败：" + resepon.status + resepon.statusText)
		}
	});
}

// 填充表格
function fillTableBody(pageInfo) {
	// 请求之前先清除 tbody的旧内容
	$("#rolePageBody").empty();
	$("#Pagination").empty();
	// 判断pageInfo是否有效,无效就返回信息时
	/*
	 * console.log("pageInfo == null"+pageInfo == null ) console.log( "pageInfo ==
	 * undefined"+pageInfo == undefined) console.log( "pageInfo.list ==
	 * null"+pageInfo.list == null ) //alert("111");
	 * console.log("pageIndfo.list.length == 0"+pageInfo.list.length == 0)
	 * console.log(pageInfo == null || pageInfo == undefined || pageInfo.list ==
	 * null || pageInfo.list.length == 0);
	 */

	if (pageInfo == null || pageInfo == undefined || pageInfo.list == null
			|| pageInfo.list.length == 0) {
		$("#rolePageBody").append(
				"<tr><td colspan='4'  align='center'>抱歉！没查询到您搜索的数据！</td></tr>");
		return;
	}

	for (var i = 0; i < pageInfo.list.length; i++) {
		// 获取role信息
		var role = pageInfo.list[i];
		var roleId = role.id;
		var roleName = role.tRole

		// 生成页面信息
		var numberId = "<td>" + (i + 1) + "</td>";
		var checkboxTd = "<td  ><input id='" + roleId
				+ "' class='itemCheckBox' type='checkbox'></td>";
		var rolename = "<td  >" + roleName + "</td>";

		var checkBtn = "<button id='"
				+ roleId
				+ "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>"
		var pencilBtn = "<button id='"
				+ roleId
				+ "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>"

		var removeBtn = "<button id='"
				+ roleId
				+ "'  type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>"
		var button = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn
				+ "</td>";

		var tr = "<tr>" + numberId + checkboxTd + rolename + button + "</tr>"
		// var tr =
		// "<tr></tr>".append(numberId).append(checkboxTd).append(rolename).append(button)
		$("#rolePageBody").append(tr);
	}
	// 生成导航条
	generateNavigator(pageInfo);

}
// 生成分页页码导航条
function generateNavigator(pageInfo) {
	// 获取总记录数
	var totalRecord = pageInfo.total;

	// 声明属性
	var properties = {
		"num_edeg_entries" : 3,
		"num_display_entries" : 5,
		"callback" : paginationCallBack,
		"items_per_page" : pageInfo.pageSize,
		"current_page" : pageInfo.pageNum - 1,
		"prev_text" : "上一页",
		"next_text" : "下一页"
	}
	// 调用pagination()函数
	$("#Pagination").pagination(totalRecord, properties);
}
// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
	// 修改Window对象的pageNum属性
	window.pageNum = pageIndex + 1;
	// 调用分页函数
	generatePage();

	// 取消页面超链接的默认行为
	return false;

}