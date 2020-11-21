package com.wzf.crowd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzf.crowd.entity.Auth;
import com.wzf.crowd.entity.AuthExample;
import com.wzf.crowd.mapper.AuthMapper;
import com.wzf.crowd.service.api.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	AuthMapper authMapper;

	@Override
	public List<Auth> getAll() {
		List<Auth> selectByExample = authMapper.selectByExample(new AuthExample());
		return selectByExample;
	}

	@Override
	public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
		return authMapper.selectAssignedAuthIdByRoleId(roleId);
	}

	@Override
	public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
		// 1.获取 roleId 的值
		List<Integer> roleIdList = map.get("roleId");
		Integer roleId = roleIdList.get(0);
		// 2.删除旧关联关系数据
		authMapper.deleteOldRelationship(roleId);
		// 3.获取 authIdList
		List<Integer> authIdList = map.get("authIdArray");
		// 4.判断 authIdList 是否有效
		if (authIdList != null && authIdList.size() > 0) {
			authMapper.insertNewRelationship(roleId, authIdList);
		}
	}

	@Override
	public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
		
		return authMapper.selectAssignedAuthNameByAdminId(adminId);
	}
}
