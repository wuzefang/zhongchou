package com.wzf.crowd.service.api;

import java.util.List;
import java.util.Map;

import com.wzf.crowd.entity.Auth;

public interface AuthService {

	List<Auth> getAll();

	List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

	void saveRoleAuthRelathinship(Map<String, List<Integer>> map);
	
	List<String> getAssignedAuthNameByAdminId(Integer adminId);

}
