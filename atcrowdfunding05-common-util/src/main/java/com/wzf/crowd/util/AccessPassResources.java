package com.wzf.crowd.util;

import java.util.HashSet;
import java.util.Set;

import com.wzf.crowd.constant.CrowdConstant;

public class AccessPassResources {
	
	// 需要放行的地址
	public static final Set<String> PASS_RES_SET = new HashSet<>();
	
	static{
		PASS_RES_SET.add("/");
		PASS_RES_SET.add("/auth/member/logout");
		PASS_RES_SET.add("/auth/member/do/login");
		PASS_RES_SET.add("/auth/do/member/register");
		PASS_RES_SET.add("/auth/member/to/login/page");
		PASS_RES_SET.add("/auth/member/send/short/message.json");
		PASS_RES_SET.add("/auth/member/to/reg/page");
	}
	
	// 需要放行的静态资源 
	public static final Set<String> STATIC_RES_SET = new HashSet<>();
	
	static{
		STATIC_RES_SET.add("bootstrap");
		STATIC_RES_SET.add("css");
		STATIC_RES_SET.add("fonts");
		STATIC_RES_SET.add("img");
		STATIC_RES_SET.add("jquery");
		STATIC_RES_SET.add("layer");
		STATIC_RES_SET.add("script");
		STATIC_RES_SET.add("ztree");
	}
	
	/**
	 * 判断请求的地址的第一层目录是不是静态资源目录
	 * @param servletPath
	 * @return
	 * 		true: 是静态资源目录
	 * 		false ：不是静态资源 目录
	 */
	public static boolean  judgeCurrentServletPathWeatherStaticResource(String servletPath){
		
		if(servletPath == null || servletPath.length() == 0){
			throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
		}
		String[] split = servletPath.split("/");
		
		String firstLevelPath = split[1];
		
		return STATIC_RES_SET.contains(firstLevelPath);
	}
	
}
