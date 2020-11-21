package com.wzf.crowd.mvc.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.wzf.crowd.constant.CrowdConstant;
import com.wzf.crowd.exception.AccessForbiddenException;
import com.wzf.crowd.exception.LoginAcctAlreadyInUseException;
import com.wzf.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.wzf.crowd.exception.LoginFailedException;
import com.wzf.crowd.util.CrowdUtil;
import com.wzf.crowd.util.ResultEntity;

/**
 * 基于注解的异常处理器类
 * 
 * @author Lenovo
 *
 */
// 表示当前类是一个处理异常的类
@ControllerAdvice
public class CrowdExceptionResolver {
	
	
	@ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
	public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseForUpdateException exception, HttpServletRequest request,
			HttpServletResponse response) throws IOException { 
		// 只是指定当前异常对应的页面即可
		String viewName = "system-error";
		return commonResolveException(exception, request, response, viewName);
	}
	
	
	@ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
	public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception, HttpServletRequest request,
			HttpServletResponse response) throws IOException { 
		// 只是指定当前异常对应的页面即可
		String viewName = "admin-add";
		return commonResolveException(exception, request, response, viewName);
	}
	
	@ExceptionHandler(value = AccessForbiddenException.class)
	public ModelAndView resolveLoginAccessForbiddenException(LoginFailedException exception, HttpServletRequest request,
			HttpServletResponse response) throws IOException { 
		// 只是指定当前异常对应的页面即可
		String viewName = "admin-login";
		return commonResolveException(exception, request, response, viewName);
	}

	@ExceptionHandler(value = LoginFailedException.class)
	public ModelAndView resolveLoginFailedException(LoginFailedException exception, HttpServletRequest request,
			HttpServletResponse response) throws IOException { 
		// 只是指定当前异常对应的页面即可
		String viewName = "admin-login";
		return commonResolveException(exception, request, response, viewName);
	}

	// 表示捕获到 Exception 类型的异常对象由当前方法处理
	@ExceptionHandler(value = Exception.class)
	public ModelAndView resolveException(Exception exception, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 只是指定当前异常对应的页面即可
		String viewName = "system-error";
		return commonResolveException(exception, request, response, viewName);
	}

	/**
	 * 核心异常处理方法
	 * 
	 * @param exception
	 *            SpringMVC 捕获到的异常对象
	 * @param request
	 *            为了判断当前请求是“普通请求”还是“Ajax 请求” 需要传入原生 request 对象
	 * @param response
	 *            为了能够将 JSON 字符串作为当前请求的响应数 据返回给浏览器
	 * @param viewName
	 *            指定要前往的视图名称
	 * @return ModelAndView
	 * @throws IOException
	 */
	private ModelAndView commonResolveException(Exception exception, HttpServletRequest request,
			HttpServletResponse response, String viewName) throws IOException {
		// 1.判断当前请求是“普通请求”还是“Ajax 请求”
		boolean judgeResult = CrowdUtil.judgeRequestType(request);
		// 2.如果是 Ajax 请求
		if (judgeResult) {
			// 3.从当前异常对象中获取异常信息
			String message = exception.getMessage();
			// 4.创建 ResultEntity

			ResultEntity<Object> resultEntity = ResultEntity.error(message);
			// 5.创建 Gson 对象
			Gson gson = new Gson();
			// 6.将 resultEntity 转化为 JSON 字符串
			String json = gson.toJson(resultEntity);
			// 7.把当前 JSON 字符串作为当前请求的响应体数据返回给浏览器
			// ①获取 Writer 对象
			PrintWriter writer = response.getWriter();
			// ②写入数据
			writer.write(json);
			// 8.返回 null，不给 SpringMVC 提供 ModelAndView 对象
			// 这样 SpringMVC 就知道不需要框架解析视图来提供响应，而是程序员自己提供了响应
			return null;
		}
		// 9.创建 ModelAndView 对象
		ModelAndView modelAndView = new ModelAndView();
		// 10.将 Exception 对象存入模型
		modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
		// 11.设置目标视图名称
		modelAndView.setViewName(viewName);
		// 12.返回 ModelAndView 对象
		return modelAndView;
	}
}
