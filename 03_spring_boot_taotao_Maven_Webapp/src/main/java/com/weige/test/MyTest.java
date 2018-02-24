package com.weige.test;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weige.App;
@RunWith(SpringRunner.class)
@SpringBootTest(classes={App.class})
public class MyTest {
	@Autowired 
	private PoolingHttpClientConnectionManager cm;
	@Test
	public void testCm(){
	}
}
