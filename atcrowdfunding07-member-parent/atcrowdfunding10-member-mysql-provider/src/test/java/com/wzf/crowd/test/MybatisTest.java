package com.wzf.crowd.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.mapper.MemberPOMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisTest {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private MemberPOMapper memberPOMapper;

	private Logger logger = LoggerFactory.getLogger(MybatisTest.class);

	@Test
	public void testMapper(){
		MemberPO memberPO = new MemberPO(1,"wuzefang","123456","吴泽方","zfwu5@qq.com",1,1,"吴泽方","123456",1);
		memberPOMapper.insert(memberPO);
	}
	
	@Test
	public void testConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		logger.info(connection.toString());
	}

}
