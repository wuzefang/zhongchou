package com.wzf.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 用户访问的源地址
		String urlPath = "/auth/member/to/reg/page";
		
		// 根据配置配置文件自动添加thymeleaf的prefix 和 suffix，拼接成要跳转的地址
		String viewName = "member-reg";
		// 添加view-controller，实现跳转
		registry.addViewController(urlPath).setViewName(viewName);
		registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
		registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
		registry.addViewController("/member/my/corwd").setViewName("member-crowd");
		
	}

	
}
