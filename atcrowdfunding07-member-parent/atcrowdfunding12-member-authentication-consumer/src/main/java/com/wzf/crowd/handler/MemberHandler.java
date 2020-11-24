package com.wzf.crowd.handler;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Objects;
import com.wzf.crowd.api.MySQLRemoteService;
import com.wzf.crowd.api.RedisRemoteService;
import com.wzf.crowd.config.ShortMessageProperties;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.po.MemberPO;
import com.wzf.crowd.entity.vo.MemberLoginVO;
import com.wzf.crowd.entity.vo.MemberVO;
import com.wzf.crowd.util.CrowdUtil;
import com.wzf.crowd.util.ResultEntity;

@Controller
public class MemberHandler {
	// 发送短信属性的配置加载类
	@Autowired
	private ShortMessageProperties SMP;

	// Redis远程调用方法声明
	@Autowired
	private RedisRemoteService redisRemoteService;

	// Mysql远程调用方法声明
	@Autowired
	private MySQLRemoteService mySQLRemoteService;

	@RequestMapping("/auth/member/logout")
	public String logout(HttpSession session){
		// session失效
		session.invalidate();
		return "redirect:http://localhost/";
	}
	
	@RequestMapping("/auth/member/do/login")
	public String login(
			@RequestParam("loginacct") String loginacct, 
			@RequestParam("userpswd") String userpswd,
			ModelMap modelMap ,
			HttpSession session) {
		// 调用mysql远程服务查询数据库并返回结果
		ResultEntity<MemberPO> memberPOByLoginAcctRemote = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
		
		// 判断返回的结果的是否是失败，如果是失败返回登陆页
		if (ResultEntity.ERROR.equals(memberPOByLoginAcctRemote.getResult())) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, memberPOByLoginAcctRemote.getMessage());
			return "member-login";
		}
		// 如果调用数据库查询到的结果不是ERROR，获取查询到的数据 ：data
		MemberPO memberPO = memberPOByLoginAcctRemote.getData();
		
		// 判断对象是否是null
		if (memberPO == null) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
			return "member-login";
		}
		// 如果不是null ，判断密码是否一致
		// 取出数据库保存的密码(此时是已经加密的)
		 String mysqlPswd = memberPO.getUserpswd();
		// 盐值是随机的，所以不能再加密调用equals进行比较，需要使用加密对象的matches()
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		// String userPswdForm = bCryptPasswordEncoder.encode(userpswd);
		
		boolean matches = bCryptPasswordEncoder.matches(userpswd, mysqlPswd);
		// 如果不匹配这返回登陆页
		if(!matches){
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
			return "member-login";
		}
		// 创建MemberLoginVO对象存入Session
		MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());
		
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
		
		
		
		return "redirect:http://localhost/auth/member/to/center/page";
	}

	@RequestMapping("/auth/do/member/register")
	public String register(MemberVO memberVO, ModelMap modelMap) {
		// 1.获取用户输入的手机号
		String phoneNum = memberVO.getPhoneNum();

		// 2.拼接Redis中存储验证码的key
		String key = CrowdConstant.REDIS_CODE_PRIFIX + phoneNum;

		// 3.从Redis中读取key对应的value
		ResultEntity<String> redisStringValueByKey = redisRemoteService.getRedisStringValueByKey(key);

		// 4.检查查询操作是否有效
		String result = redisStringValueByKey.getResult();

		if (ResultEntity.ERROR.equals(result)) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, redisStringValueByKey.getMessage());
			return "member-reg";
		}
		String redisCode = redisStringValueByKey.getData();
		if (redisCode == null) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);

			return "member-reg";
		}

		// 获取表单中传送过来的验证码
		String formCode = memberVO.getCode();

		// 5.如果从Redis可以查询到value则比较表单的验证码和Redis验证码
		if (!Objects.equal(redisCode, formCode)) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_INVALID);
			return "member-reg";
		}
		// 6.如果验证码一致，则从Redis删除
		redisRemoteService.removeRedisKeyRemote(key);

		// 7.执行加密
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		// 获取密码
		String userpswd = memberVO.getUserpswd();
		// 加密
		String encode = bCryptPasswordEncoder.encode(userpswd);
		// 设置回对象
		memberVO.setUserpswd(encode);
		// 8.执行保存
		// 创建空的MemberPO对象
		MemberPO memberPO = new MemberPO();

		// 复制属性
		BeanUtils.copyProperties(memberVO, memberPO);

		// 调用保存方法
		ResultEntity<String> saveMember = mySQLRemoteService.saveMember(memberPO);

		if (ResultEntity.ERROR.equals(saveMember.getResult())) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMember.getMessage());

			return "member-reg";
		}

		return "redirect:http://localhost/auth/member/to/login/page";
	}

	@ResponseBody
	@RequestMapping("/auth/member/send/short/message.json")
	public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
		// 1.发送信息
		ResultEntity<String> sendCodeByShortMessage = CrowdUtil.sendCodeByShortMessage(SMP.getHost(), SMP.getPath(),
				SMP.getMethod(), SMP.getAppcode(), phoneNum, SMP.getTemplateId());

		// 2.判断短信发送的结果
		if (ResultEntity.SUCCESS.equals(sendCodeByShortMessage.getResult())) {
			// 3.如果发送成功，则将验证码存入redis
			String code = sendCodeByShortMessage.getData();
			// 保存的key的名称
			String key = CrowdConstant.REDIS_CODE_PRIFIX + phoneNum;
			// 设置一个key-value保存到redis，并设置保存时间
			ResultEntity<String> setRedisValueRemoteWithTimeout = redisRemoteService.setRedisValueRemoteWithTimeout(key,
					code, 15, TimeUnit.MINUTES);
			// 判断设置redis的key-value的结果，是否正确
			if (ResultEntity.SUCCESS.equals(setRedisValueRemoteWithTimeout.getResult())) {
				// 设置成功就返回
				return ResultEntity.successWithoutData();
			} else {
				// 设置失败就直接返回异常
				return setRedisValueRemoteWithTimeout;
			}
		}
		// 如果发送短信方法调用失败就直接返回结果
		return sendCodeByShortMessage;
	}
}
