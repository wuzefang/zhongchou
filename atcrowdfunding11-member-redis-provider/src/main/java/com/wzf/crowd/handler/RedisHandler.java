package com.wzf.crowd.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wzf.crowd.util.ResultEntity;

@RestController
public class RedisHandler {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@RequestMapping("/set/redis/value/remote")
	ResultEntity<String> setRedisValueRemote(@RequestParam("key") String key, @RequestParam("value") String value) {
		try {
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(key, value);

			return ResultEntity.successWithoutData();

		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}

	}

	@RequestMapping("/set/redis/value/remote/with/timeout")
	ResultEntity<String> setRedisValueRemoteWithTimeout(@RequestParam("key") String key,
			@RequestParam("value") String value, @RequestParam("time") long time,
			@RequestParam("timeUnit") TimeUnit timeUnit) {
		try {
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

			opsForValue.set(key, value, time, timeUnit);

			return ResultEntity.successWithoutData();

		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}
	}

	@RequestMapping("/get/redis/string/value/by/key")
	ResultEntity<String> getRedisStringValueByKey(@RequestParam("key") String key) {
		try {
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

			String value = opsForValue.get(key);

			return ResultEntity.successWithData(value);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}
	}

	@RequestMapping("/remove/redis/key/remote")
	ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key) {
		try {

			redisTemplate.delete(key);
			return ResultEntity.successWithoutData();

		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.error(e.getMessage());
		}
	}
}
