package com.wzf.crowd.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.util.AccessPassResources;

@Component
public class CrowdAccessFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		// 1.获取请求地址
		RequestContext requestContext = RequestContext.getCurrentContext(); // 获取RequestContext 对象
		
		// 2.通过RequestContext对象获取当前请求对象，（框架底层是借助ThreadLocal从当前线程上获取实现绑定的Request对象）
		HttpServletRequest request = requestContext.getRequest();  // 获取当前请求的HttpServletRequest对象
		
		// 3.获取servletPath值
		String servletPath = request.getServletPath();
		
		// 4.判断请求地址是否是可以直接放行的url
		boolean contains = AccessPassResources.PASS_RES_SET.contains(servletPath);
		
		// 5.如果是直接放行的url，不需要进行处理，返回false
		if(contains){
			return false;
		}
		// 6.如果不是需要放行的url，再判断是否是静态资源
		boolean judgePath = AccessPassResources.judgeCurrentServletPathWeatherStaticResource(servletPath);
		
		// 7.是静态资源就返回false，否则返回true
		return !judgePath;
	}

	@Override
	public Object run() throws ZuulException {
		// 1.获取RequestContext对象
		RequestContext currentContext = RequestContext.getCurrentContext();
		
		// 2.获取HttpServletRequest对象
		HttpServletRequest request = currentContext.getRequest();
		
		// 3.获取当前HttpSession对象
		HttpSession session = request.getSession();
		
		// 4.获取session中的loginMember对象
		Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
		
		// 5.判断对象是否存在
		if (loginMember == null) {
			// 6.如果对象不存在，返回页面一个信息，并重定向到登录页
			session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
			
			// 获取响应对象
			HttpServletResponse response = currentContext.getResponse();
			try {
				response.sendRedirect("/auth/member/to/login/page");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return null;
	}

	@Override
	public String filterType() {
		// 返回pre表示在目标微服务前执行过滤器
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
