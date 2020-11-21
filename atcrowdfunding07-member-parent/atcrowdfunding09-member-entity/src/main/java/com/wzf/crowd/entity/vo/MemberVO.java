package com.wzf.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专门用来封装表单提交的属性
 * @author wuzf
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {

	private String loginacct;

	private String userpswd;

	private String username;

	private String email;
	
	private String phoneNum;
	
	private String code;

}
