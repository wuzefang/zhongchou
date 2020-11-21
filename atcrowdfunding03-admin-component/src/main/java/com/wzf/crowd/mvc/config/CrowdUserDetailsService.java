package com.wzf.crowd.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.wzf.crowd.entity.Admin;
import com.wzf.crowd.entity.Role;
import com.wzf.crowd.service.api.AdminService;
import com.wzf.crowd.service.api.AuthService;
import com.wzf.crowd.service.api.RoleService;

@Component
public class  CrowdUserDetailsService implements UserDetailsService{
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AuthService authService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 1、根据loginAcct查询出adminId
		Admin admin = adminService.getAdminByLoginAcct(username);
		Integer adminId = admin.getId();
		
		// 2、根据adminId查询对应的角色
		List<Role> roleList = roleService.getAssignedRole(adminId);
		
		// 3、根据adminId查询对应的权限
		List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
		
		// 4、创建List对象来保存GrantedAuthority
		List<GrantedAuthority> authorities =  new ArrayList<>();
		
		// 5、遍历角色集合，并保存到GrantedAuthority的集合中
		for (Role role : roleList) {
			// 拼接角色名：加"ROLE_"前缀
			String roleName = "ROLE_"+role.gettRole();
			
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
			
			authorities.add(simpleGrantedAuthority);
			
		}
		// 6、遍历所有权限的集合，并保存到GrantedAuthority的集合中
		for (String  authName : authNameList) {
			
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
			
			authorities.add(simpleGrantedAuthority);
			
		}
		// 7、封装SecurityAdmin对象
		SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
		
		
		return securityAdmin;
	}

}
