package com.weige.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.weige.properties.MyProperties;
import com.weige.utils.IdleConnectionEvictor;

@Configuration
public class HttpClientManagerConfig {
	@Autowired
	private MyProperties myProperties;
	 
	/**
	 * 获得ClientConnectionManager
	 * @param cm
	 * @return
	 */
	@Bean(destroyMethod="close")
	public PoolingHttpClientConnectionManager getClientConnectionManager(){
		 PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	     // 设置最大连接数
	     cm.setMaxTotal(myProperties.getHttpClientMaxTotal());
	     // 设置每个主机地址的并发数
	     cm.setDefaultMaxPerRoute(myProperties.getHttpClientMaxPerRoute());
	     return cm;
	}
	
	/**
	 * 获得HttpClient
	 * @param cm
	 * @return
	 */
	@Bean
	@Scope("prototype")
	public CloseableHttpClient getHttpClient(PoolingHttpClientConnectionManager cm){
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
	}
	
	/**
	 * HttpClient配置
	 * @param cm
	 * @return
	 */
	@Bean
	public RequestConfig getRequestConfig(PoolingHttpClientConnectionManager cm){
		 RequestConfig config = RequestConfig.custom().setConnectTimeout(myProperties.getHttpClientConnectTimeout()) // 创建连接的最长时间
	                .setConnectionRequestTimeout(myProperties.getHttpClientConnectionRequestTimeout()) // 从连接池中获取到连接的最长时间
	                .setSocketTimeout(myProperties.getHttpClientSocketTimeout()) // 数据传输的最长时间
	                .setStaleConnectionCheckEnabled(myProperties.isHttpClientStaleConnectionCheckEnabled()) // 提交请求前测试连接是否可用
	                .build();
        return config;
	}
	
	/**
	 * 定期清理无效链接
	 * @param cm
	 * @return
	 */
	@Bean(destroyMethod="shutdown")
	public IdleConnectionEvictor getIdleConnectionEvictor(PoolingHttpClientConnectionManager cm){
        return new IdleConnectionEvictor(cm);
	}
}
