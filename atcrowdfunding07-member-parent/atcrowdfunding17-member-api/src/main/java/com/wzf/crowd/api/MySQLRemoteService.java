package com.wzf.crowd.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.entity.vo.DetailProjectVO;
import com.wzf.crowd.entity.vo.PortalTypeVO;
import com.wzf.crowd.entity.vo.ProjectVO;
import com.wzf.crowd.util.ResultEntity;

@FeignClient("wzf-crowd-mysql")
public interface MySQLRemoteService {

	@RequestMapping("/get/project/detail/remote/{projectId}")
	public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

	@RequestMapping("/get/portal/type/project/data")
	public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

	/**
	 * 处理保存会员请求的方法
	 * 
	 * @param memberPO
	 * @return
	 */
	@RequestMapping("/save/member/remote")
	ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

	/**
	 * 处理查询会员账号的请求
	 * 
	 * @param loginacct
	 * @return
	 */
	@RequestMapping("/get/memberpo/by/login/acct/remote")
	ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

	@RequestMapping("/save/project/vo/remote")
	ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
			@RequestParam("memberId") Integer memberId);
}
