package com.wzf.crowd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzf.crowd.entity.Role;
import com.wzf.crowd.entity.RoleExample;
import com.wzf.crowd.entity.RoleExample.Criteria;
import com.wzf.crowd.mapper.RoleMapper;
import com.wzf.crowd.service.api.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleMapper rolemapper;

	@Override
	public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
		// 1、开启分页
		PageHelper.startPage(pageNum, pageSize);

		// 2、执行查询
		List<Role> roleList = rolemapper.selectByKeyWord(keyword);

		return new PageInfo<>(roleList);
	}

	@Override
	public void saveRole(Role role) {
		rolemapper.insert(role);
	}

	@Override
	public void updateRole(Role role) {
		rolemapper.updateByPrimaryKey(role);
	}

	@Override
	public void removeRole(List<Integer> roleIdList) {
		RoleExample example = new RoleExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andIdIn(roleIdList);
		rolemapper.deleteByExample(example);
	}

	@Override
	public List<Role> getAssignedRole(Integer adminId) {
		
		return  rolemapper.selectAssignedRole(adminId);
	}

	@Override
	public List<Role> getUnAssignedRole(Integer adminId) {
		return  rolemapper.selectUnAssignedRole(adminId);
	}
}
