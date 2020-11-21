package com.wzf.crowd.mvc.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.wzf.crowd.constant.CrowdConstant;

@Configuration // 配置类
@EnableWebSecurity // 启用权限控制
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		// builder.inMemoryAuthentication().withUser("wuzefang").password("123456").roles("admin");
		builder.userDetailsService(userDetailsService) // 基于数据库的权限认证
				.passwordEncoder(passwordEncoder);

	}

	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security.authorizeRequests() // 队请求授权
				.antMatchers("/admin/login.html", "/bootstrap/**", "/crowd/**", "/css/**", "/fonts/**", "/img/**",
						"/jquery/**", "/layer/**", "/script/**", "/ztree/**") // 首页,静态文件地址
				.permitAll() // 让首页允许直接访问
				.antMatchers("/admin/get/page.html").hasRole("经理").anyRequest() // 其余请求
				.authenticated() // 需要认证
				.and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

					@Override
					public void handle(HttpServletRequest request, HttpServletResponse response,
							AccessDeniedException accessDeniedException) throws IOException, ServletException {
						request.setAttribute("exception", new
								Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
						request.getRequestDispatcher("/WEB-INF/pages/system-error.jsp").forward(request, response);
					}
					
				}).and().csrf() // csrf 跨站请求
				.disable() // 禁用csrf
				.formLogin().loginPage("/admin/login.html") // 登录页
				.loginProcessingUrl("/security/do/login.html") // 处理的登陆的请求
				.defaultSuccessUrl("/admin/main.html") // 验证成功后跳转地址
				.usernameParameter("loginAcct") // 表单的用户名的参数名
				.passwordParameter("userPswd") // 表单的密码的参数名

				.and().logout() // 开启退出
				.logoutUrl("/security/do/logout.html") // 退出的请求地址
				.logoutSuccessUrl("/admin/login.html") // 指定退出成功后跳转的地址
		;
	}

}
