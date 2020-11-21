<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="removeModal" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">尚筹网系统弹窗</h4>
			</div>
			<div class="modal-body">
				<h4>请确认是否删除以下角色：</h4>
				<div id="roleNameDiv"></div>
			</div>
			<div class="modal-footer">
				<button id="removeRoleBtn" type="button" class="btn btn-primary">
					确 认</button>
			</div>
		</div>
	</div>
</div>