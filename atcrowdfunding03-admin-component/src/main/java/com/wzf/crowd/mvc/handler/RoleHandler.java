package com.wzf.crowd.mvc.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.wzf.crowd.entity.Role;
import com.wzf.crowd.service.api.RoleService;
import com.wzf.crowd.util.ResultEntity;

@Controller
public class RoleHandler {
	
	@ResponseBody
	@RequestMapping("/role/remove/roleIdArray.json")
	public ResultEntity<String> removeRole(@RequestBody List<Integer> roleList){
		roleService.removeRole(roleList);
		
		return ResultEntity.successWithoutData() ;
	}
 	
	@ResponseBody
	@RequestMapping("/role/update.json")
	public ResultEntity<String> updateRole(Role role){
		roleService.updateRole(role);
		return ResultEntity.successWithoutData(); 
	}
	
	@ResponseBody
	@RequestMapping("/role/save.json")
	public ResultEntity<String> saveRole(Role role){
		
		roleService.saveRole(role);
		return ResultEntity.successWithoutData();
	}
	
	@Autowired
	RoleService roleService;
	
	@ResponseBody
	@RequestMapping("/role/get/page/info.json")
	@PreAuthorize("hasRole('部长')")
	public ResultEntity<PageInfo<Role>> getPageInfo(
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
			@RequestParam(value = "keyword", defaultValue = "") String keyword) {
		// 调用查询方法，并且进行返回
		PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
		// 返回数据到页面
		return ResultEntity.successWithData(pageInfo);
	}
	
	
	
}
