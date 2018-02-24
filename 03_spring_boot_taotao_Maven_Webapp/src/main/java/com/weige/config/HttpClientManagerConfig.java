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
	 * ���ClientConnectionManager
	 * @param cm
	 * @return
	 */
	@Bean(destroyMethod="close")
	public PoolingHttpClientConnectionManager getClientConnectionManager(){
		 PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	     // �������������
	     cm.setMaxTotal(myProperties.getHttpClientMaxTotal());
	     // ����ÿ��������ַ�Ĳ�����
	     cm.setDefaultMaxPerRoute(myProperties.getHttpClientMaxPerRoute());
	     return cm;
	}
	
	/**
	 * ���HttpClient
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
	 * HttpClient����
	 * @param cm
	 * @return
	 */
	@Bean
	public RequestConfig getRequestConfig(PoolingHttpClientConnectionManager cm){
		 RequestConfig config = RequestConfig.custom().setConnectTimeout(myProperties.getHttpClientConnectTimeout()) // �������ӵ��ʱ��
	                .setConnectionRequestTimeout(myProperties.getHttpClientConnectionRequestTimeout()) // �����ӳ��л�ȡ�����ӵ��ʱ��
	                .setSocketTimeout(myProperties.getHttpClientSocketTimeout()) // ���ݴ�����ʱ��
	                .setStaleConnectionCheckEnabled(myProperties.isHttpClientStaleConnectionCheckEnabled()) // �ύ����ǰ���������Ƿ����
	                .build();
        return config;
	}
	
	/**
	 * ����������Ч����
	 * @param cm
	 * @return
	 */
	@Bean(destroyMethod="shutdown")
	public IdleConnectionEvictor getIdleConnectionEvictor(PoolingHttpClientConnectionManager cm){
        return new IdleConnectionEvictor(cm);
	}
}
