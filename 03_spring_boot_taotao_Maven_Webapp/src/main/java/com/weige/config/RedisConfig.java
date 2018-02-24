package com.weige.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig{
	@Bean
	public JedisPool getJedisPoolConfig(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(50);
		JedisPool pool = new JedisPool(config,"127.0.0.1",6379);
		return pool;
	}
}
