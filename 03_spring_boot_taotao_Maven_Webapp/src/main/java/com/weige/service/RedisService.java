package com.weige.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
	@Autowired
	private JedisPool pool;
	
	private<T> T execute(Function<Jedis, T> fun){
		Jedis jedis = null;
		try {
			jedis= pool.getResource();
			return fun.Callback(jedis);
		}finally{
			if(jedis!=null){
				pool.returnResource(jedis);
			}
		}
	}
	/**
	 * 设置
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(final String key,final String value,final Integer seconds){
		return this.execute(new Function<Jedis, String>() {
			public String Callback(Jedis jedis) {
				String result = jedis.set(key, value);
				jedis.expire(key, seconds);
				return result;
			}
		});
	}
	/**
	 * 获取
	 * @param key
	 * @return
	 */
	public String get(final String key){
		return this.execute(new Function<Jedis, String>() {
			public String Callback(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	/**
	 * 删除
	 * @param key
	 * @return
	 */
	public Long del(final String key){
		return this.execute(new Function<Jedis, Long>() {
			public Long Callback(Jedis jedis) {
				return jedis.del(key);
			}
		});
	}
	
	/**
	 * 设置生存时间
	 * @param key
	 * @return
	 */
	public Long expire(final String key,final Integer seconds){
		return this.execute(new Function<Jedis, Long>() {
			public Long Callback(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
}
