package com.weeds.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;

public abstract class RedisGeneratorDao<K extends Serializable,V extends Serializable> {

	@Autowired
	protected RedisTemplate<K, V> redisTemplate;
	
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
}
