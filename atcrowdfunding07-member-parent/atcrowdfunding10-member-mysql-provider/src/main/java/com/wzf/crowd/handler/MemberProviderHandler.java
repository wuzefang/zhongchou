package com.wzf.crowd.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.service.api.MemberService;
import com.wzf.crowd.util.ResultEntity;

@RestController
public class MemberProviderHandler {
	@Autowired
	private MemberService memberService;
	
	/**
	 * 处理保存会员请求的方法
	 * @param memberPO
	 * @return
	 */
	@RequestMapping("/save/member/remote")
	public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {
		try {
			memberService.saveMember(memberPO);
			
			return ResultEntity.successWithoutData();
			
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				
				return ResultEntity.error(CrowdConstant.LOGIN_ADMIN_ACCT_ALREADY_IN_USE);
				
			}
			return ResultEntity.error(e.getMessage());
		}
	}
	
	/**
	 * 处理查询会员账号的请求
	 * @param loginacct
	 * @return
	 */
	@RequestMapping("/get/memberpo/by/login/acct/remote")
	public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct) {

		MemberPO memberPO;
		try {
			memberPO = memberService.getMemberPOByLoginAcct(loginacct);
			return ResultEntity.successWithData(memberPO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}

	}
}
