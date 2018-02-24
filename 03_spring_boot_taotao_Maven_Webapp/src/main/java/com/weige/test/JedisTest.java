package com.weige.test;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.weige.App;
@RunWith(SpringRunner.class)
@SpringBootTest(classes={App.class})
public class JedisTest {
	@Test
	public void testJedisPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(50);
		JedisPool pool = new JedisPool(config,"127.0.0.1",6379);
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("id"));
		
		pool.returnResource(jedis);
		pool.close();
	}
}
