package com.wzf.crowd.api;

import java.util.concurrent.TimeUnit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wzf.crowd.util.ResultEntity;

@FeignClient("wzf-crowd-redis")
public interface RedisRemoteService {	
	
	@RequestMapping("/set/redis/value/remote")
	ResultEntity<String> setRedisValueRemote(
			@RequestParam("key") String key,
			@RequestParam("value") String value
			);
	
	@RequestMapping("/set/redis/value/remote/with/timeout")
	ResultEntity<String> setRedisValueRemoteWithTimeout(
			@RequestParam("key") String key,
			@RequestParam("value") String value,
			@RequestParam("time") long time,
			@RequestParam("timeUnit") TimeUnit timeUnit
			);
	
	
	@RequestMapping("/get/redis/string/value/by/key")
	ResultEntity<String> getRedisStringValueByKey(@RequestParam("key") String key);
	
	@RequestMapping("/remove/redis/key/remote")
	ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
} 
