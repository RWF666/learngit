package com.weige.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="my")
@PropertySource("classpath:my.properties")
@Data
public class MyProperties {
	private String repositoryPath;
	private String imageBseUrl;
	private int httpClientMaxTotal;
	private int httpClientMaxPerRoute;
	private int httpClientConnectTimeout;
	private int httpClientConnectionRequestTimeout;
	private int httpClientSocketTimeout;
	private boolean httpClientStaleConnectionCheckEnabled;
	private String taotaoUrl;
	private String adUrl1;
	private String solrUrl;
}
