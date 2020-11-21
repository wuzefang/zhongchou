package com.wzf.crowd.service.api;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.wzf.crowd.entity.Role;

public interface RoleService {
	
	PageInfo<Role> getPageInfo(Integer pageNum , Integer pageSize ,String keyword);

	void saveRole(Role role);

	void updateRole(Role role);
	void removeRole(List<Integer> roleIdList);

	List<Role> getAssignedRole(Integer adminId);

	List<Role> getUnAssignedRole(Integer adminId);


}
