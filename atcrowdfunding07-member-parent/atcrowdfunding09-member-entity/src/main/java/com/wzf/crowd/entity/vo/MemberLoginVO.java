package com.wzf.crowd.entity.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登陆成功后存入session的对象
 * 
 * @author wuzf
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginVO implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	private String email;

}
