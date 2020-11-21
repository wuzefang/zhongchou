package com.wzf.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//Spring整合junit

import com.wzf.crowd.entity.Admin;
import com.wzf.crowd.entity.Role;
import com.wzf.crowd.mapper.AdminMapper;
import com.wzf.crowd.mapper.RoleMapper;
import com.wzf.crowd.service.api.AdminService;
import com.wzf.crowd.service.api.RoleService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class TestDataSource {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AdminMapper adminMapper;
	
	@Autowired
	AdminService  adminService;
	
	@Autowired
	RoleService roleService ;
	
	@Autowired
	RoleMapper rolemapper;
	@Test
	public void testService(){
		for(int i =0  ; i< 168 ; i++){
			rolemapper.insertSelective(new Role(null, "role"+i));
			//adminService.saveAdmin(new Admin(null,"test"+i,"123456","test"+i,"cw@qq.com",null));
		}
	}
	
	@Test
	public void testLog(){
		Logger logger = LoggerFactory.getLogger(TestDataSource.class);
		logger.debug("hello");
		logger.info("wuzefang");
		logger.warn("jiayou");
		logger.error("aoligei");
	}
	
	//测试Mapper操作数据
	@Test
	public void testAdminInsert(){
		Admin admin = new Admin(null,"bizhenyuan","123456","毕儿子","bzy@qq.com",null);
		System.out.println(adminMapper.insert(admin));
	}
	
	//测试数据库连接
	@Test
	public void testDataSource() throws SQLException{
		System.out.println(dataSource.getConnection());
	}
}
