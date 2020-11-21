package com.wzf.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.Admin;
import com.wzf.crowd.entity.AdminExample;
import com.wzf.crowd.entity.AdminExample.Criteria;
import com.wzf.crowd.exception.LoginFailedException;
import com.wzf.crowd.mapper.AdminMapper;
import com.wzf.crowd.service.api.AdminService;
import com.wzf.crowd.util.CrowdUtil;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminMapper adminMapper;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public void saveAdmin(Admin admin) {
		// 密码加密
	//	String encode = CrowdUtil.md5(admin.getUserPswd()); //原始的自定义的加密
		String encode = passwordEncoder.encode(admin.getUserPswd());  // SpringSecurity盐值加密
		admin.setUserPswd(encode);

		// 生成时间
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = simpleDateFormat.format(date);
		admin.setCreateTime(createTime);
		adminMapper.insert(admin);
	}

	@Override
	public List<Admin> getAll() {

		return adminMapper.selectByExample(null);
	}

	@Override
	public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
		// 1、根据登陆账号查询admin对象

		// 先创建AdminExample对象
		AdminExample adminExample = new AdminExample();
		// 通过AdminExample对象来创建条件对象
		Criteria criteria = adminExample.createCriteria();
		// 在Criteria对象中添加匹配条件
		criteria.andLoginAcctEqualTo(loginAcct);
		// 查询admin对象
		List<Admin> adminList = adminMapper.selectByExample(adminExample);

		// 2、判断查询到的结果是不是为空
		if (adminList == null || adminList.size() == 0) {
			// 3、如果查询到结果为空，抛出异常
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		// 4、如果list对象不为空，取出对象，并判断
		// 如果传入多个对象，抛出异常
		if (adminList.size() > 1) {
			throw new RuntimeException(CrowdConstant.LOGIN_ADMIN_ACCT_NOT_UNIQUE);

		}
		// 只有一个对象，开始获取到对象
		Admin admin = adminList.get(0);
		if (admin == null) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		// 5、对提交过来的明文进行加密
		String encodePawd = CrowdUtil.md5(userPswd);
		// 6、对密码进行比较
		if (!Objects.equals(encodePawd, admin.getUserPswd())) {
			// 密码不下相等，抛出异常
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		// 密码正常 返回对象
		return admin;
	}

	/**
	 * 管理员查询分页
	 */
	@Override
	public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
		// 1、调用PageHelper的静态方法开启分页；本行代码的下一行自动分页
		PageHelper.startPage(pageNum, pageSize);

		// 2、执行查询方法
		List<Admin> selectAdminByKeyword = adminMapper.selectAdminByKeyword(keyword);

		// 3、封装到PageInfo中
		return new PageInfo<>(selectAdminByKeyword);
	}

	@Override
	public void remove(Integer adminId) {
		adminMapper.deleteByPrimaryKey(adminId);
	}

	@Override
	public Admin getAdminById(Integer adminId) {
		Admin admin = adminMapper.selectByPrimaryKey(adminId);
		return admin;

	}

	@Override
	public void editAdmin(Admin admin) {
		adminMapper.updateByPrimaryKeySelective(admin);
	}

	@Override
	public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
		// 为了简化操作：先根据 adminId 删除旧的数据，再根据 roleIdList 保存全部新的数据
		// 1.根据 adminId 删除旧的关联关系数据
		adminMapper.deleteOLdRelationship(adminId);
		// 2.根据 roleIdList 和 adminId 保存新的关联关系
		if (roleIdList != null && roleIdList.size() > 0) {
			adminMapper.insertNewRelationship(adminId, roleIdList);
		}
	}

	@Override
	public Admin getAdminByLoginAcct(String username) {
		AdminExample example = new AdminExample();
		Criteria criteria = example.createCriteria();
		criteria.andLoginAcctEqualTo(username);
		List<Admin> list = adminMapper.selectByExample(example);
		Admin admin = list.get(0);
		return admin;
	}

}
