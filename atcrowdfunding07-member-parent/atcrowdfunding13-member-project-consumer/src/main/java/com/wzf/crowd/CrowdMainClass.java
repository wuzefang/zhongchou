package com.wzf.crowd;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients // 启用客户端
@EnableDiscoveryClient
@SpringBootApplication
public class CrowdMainClass {

	public static void main(String[] args) {
		SpringApplication.run(CrowdMainClass.class, args);
	}

}
