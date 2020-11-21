package com.wzf.crowd.service.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.wzf.crowd.entity.Admin;
public interface AdminService {

	void saveAdmin(Admin admin);

	List<Admin> getAll();

	Admin getAdminByLoginAcct(String loginAcct, String userPswd);
	
	PageInfo<Admin> getPageInfo(String keyword ,Integer pageNum, Integer pageSize);

	void remove(Integer adminId);

	Admin getAdminById(Integer adminId);

	void editAdmin(Admin admin);

	void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
	
	Admin getAdminByLoginAcct(String username);
}
