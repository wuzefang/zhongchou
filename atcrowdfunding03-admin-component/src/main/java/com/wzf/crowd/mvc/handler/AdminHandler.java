package com.wzf.crowd.mvc.handler;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.Constant;
import com.github.pagehelper.PageInfo;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.Admin;
import com.wzf.crowd.exception.LoginAcctAlreadyInUseException;
import com.wzf.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.wzf.crowd.service.api.AdminService;

@Controller
public class AdminHandler {

	@Autowired
	AdminService adminService;
	@RequestMapping("/admin/edit/{pageNum}.html")
	public String editAdmin(Admin admin,@RequestParam(value="pageNum",defaultValue="1") Integer pageNum){
		
		try {
			adminService.editAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof DuplicateKeyException){
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.LOGIN_ADMIN_ACCT_ALREADY_IN_USE);
			}
		}
		return  "redirect:/admin/get/page.html?pageNum="+pageNum;
	}
	
	/**
	 * 跳转回显页面
	 * @param adminId
	 * @param pageNum
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/admin/to/edit/page.html")
	public String toEditAdminPage(
			@RequestParam("adminId") Integer adminId,
			ModelMap modelMap){
		Admin admin = adminService.getAdminById(adminId);
		modelMap.addAttribute("admin", admin);
		return "admin-edit";
	}
	
	@RequestMapping("/admin/save.html")
	public String save(Admin admin){
		
		try {
			adminService.saveAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof DuplicateKeyException){
				throw new LoginAcctAlreadyInUseException(CrowdConstant.LOGIN_ADMIN_ACCT_ALREADY_IN_USE);
			}
		}
		return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
	}
	
	@RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
	public String removeAdmin(
			@PathVariable("adminId") Integer adminId,
			@PathVariable("pageNum") Integer pageNum,
			@PathVariable("keyword") String keyword
			){
			
		adminService.remove(adminId);
		// 删除后页面回显页面：重定向到查询的地址，并带上页码和关键字
		return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
	}
	
	@RequestMapping("/admin/get/page.html")
	public String getAdminPage(
			@RequestParam(value="keyword",defaultValue="")String keyword, 
			@RequestParam(value="pageNum",defaultValue="1")Integer pageNum, 
			@RequestParam(value="pageSize",defaultValue="5")Integer pageSize,
			ModelMap modelMap
			){
		PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
		modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
		return "admin-page";
	}
	
	@RequestMapping("/admin/do/logout.html")
	public String doLogout(HttpSession session){
		session.invalidate();// 让session失效
		return "redirect:/admin/login.html";
	}

	/**
	 * 管理员登陆
	 * @param loginAcct
	 * @param userPswd
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/do/login.html")
	public String doLogin(
			@RequestParam("loginAcct") String loginAcct,
			@RequestParam(value="userPswd" ) String userPswd,
			HttpSession session
			) {
		// 将从前端获取到的数数据进行后台校验
		Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
		
		// 将登陆成功返回的用户存入Session中
		session .setAttribute(CrowdConstant.LOGIN_ADMIN_ACCT, admin);
		return "redirect:/admin/main.html";

	}
}
