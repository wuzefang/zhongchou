package com.wzf.crowd.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.pagehelper.Constant;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.entity.Admin;
import com.wzf.crowd.exception.AccessForbiddenException;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 1、从request对象中获取Session对象
		HttpSession session = request.getSession();
		// 2、尝试从session中获取Admin对象
		Admin admin = (Admin) session.getAttribute(CrowdConstant.LOGIN_ADMIN_ACCT);
		// 3、没有获取到用户对象 ，抛出异常
		if (admin == null) {
			// 4、抛出异常
			throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
		}
		// 5、获取到对象，放行请求
		return true;
	}
	

}
