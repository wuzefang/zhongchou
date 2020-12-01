package com.wzf.crowd.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.entity.vo.PortalProjectVO;
import com.wzf.crowd.entity.vo.PortalTypeVO;
import com.wzf.crowd.mapper.MemberPOMapper;
import com.wzf.crowd.mapper.ProjectPOMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisTest {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private MemberPOMapper memberPOMapper;
	
	@Autowired
	ProjectPOMapper projectPOMapper;

	private Logger logger = LoggerFactory.getLogger(MybatisTest.class);

	@Test
	public void testProjectPOMapper(){
		List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
		for (PortalTypeVO portalTypeVO : portalTypeVOList) {
			int id = portalTypeVO.getId();
			String name = portalTypeVO.getName();
			String remark = portalTypeVO.getRemark();

			List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
			for (PortalProjectVO portalProjectVO : portalProjectVOList) {
				logger.info(name);
				logger.info(remark);
				logger.info(portalProjectVO.toString());
			}
		}
	}
	
	
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
