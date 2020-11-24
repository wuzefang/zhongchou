package com.wzf.crowd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.entity.po.MemberPOExample;
import com.wzf.crowd.entity.po.MemberPOExample.Criteria;
import com.wzf.crowd.mapper.MemberPOMapper;
import com.wzf.crowd.service.api.MemberService;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberPOMapper memberPOMapper;

	@Override
	public MemberPO getMemberPOByLoginAcct(String loginacct) {
		// 创建MemberPOExample
		MemberPOExample example = new MemberPOExample();
		// 创建条件
		Criteria criteria = example.createCriteria();
		// 设置条件
		criteria.andLoginacctEqualTo(loginacct);
		// 查询出集合
		List<MemberPO> memberPOList = memberPOMapper.selectByExample(example);
		
		if (memberPOList == null || memberPOList.size() == 0) {
			return null; 
		}
		return memberPOList.get(0);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW , rollbackFor = Exception.class,readOnly = false)
	@Override
	public void saveMember(MemberPO memberPO) {
		memberPOMapper.insertSelective(memberPO);
	}

}
