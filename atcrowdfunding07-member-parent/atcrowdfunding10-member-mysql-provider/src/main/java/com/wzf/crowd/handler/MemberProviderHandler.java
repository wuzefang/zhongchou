package com.wzf.crowd.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.service.api.MemberService;
import com.wzf.crowd.util.ResultEntity;

@RestController
public class MemberProviderHandler {
	@Autowired
	private MemberService memberService;

	@RequestMapping("/get/memberpo/by/login/acct/remote")
	ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct) {

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
