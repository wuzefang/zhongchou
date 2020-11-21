package com.wzf.crowd.mvc.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wzf.crowd.entity.Admin;
import com.wzf.crowd.service.api.AdminService;
import com.wzf.crowd.util.ResultEntity;

@Controller
public class TestHandler {

	@Autowired
	private AdminService adminService;
	
	@RequestMapping("/test/ssm.html")
	public String getAll(ModelMap modelMap){
		List<Admin> list = adminService.getAll();
		modelMap.addAttribute("adminList",list);
		int i = 10 /0 ;
		return "testRequest";
	}
	
	@ResponseBody
	@RequestMapping("/test/ssm.json")
	public ResultEntity<Admin> getAllToJson(Admin admin){
		 List<Admin> list = adminService.getAll();
//		modelMap.addAttribute("adminList",list);
		return ResultEntity.successWithData(admin);
	}
}
