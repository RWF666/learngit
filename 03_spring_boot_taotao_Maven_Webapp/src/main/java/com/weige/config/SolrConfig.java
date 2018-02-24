package com.weige.config;


import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.weige.properties.MyProperties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class SolrConfig{
	@Autowired
	private MyProperties myProperties;
	
	@Bean
	public HttpSolrServer getHttpSolrServer(){
		String url = myProperties.getSolrUrl();
		HttpSolrServer httpSolrServer = new HttpSolrServer(url); //����solr��server
		httpSolrServer.setParser(new XMLResponseParser()); // ������Ӧ������
		httpSolrServer.setMaxRetries(1); // �������Դ������Ƽ�����Ϊ1
		httpSolrServer.setConnectionTimeout(500); // �������ӵ��ʱ��
		return httpSolrServer;
	}
}
