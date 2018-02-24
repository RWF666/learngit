package com.weige.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import lombok.Data;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.weige.pojo.HttpResult;

@Service
@Data
public class ApiService {
	@Autowired
	private CloseableHttpClient httpclient;
	
	@Autowired
	private RequestConfig requestConfig;
	
	/**
	 * 带有参数的get请求
	 * @param url
	 * @param nvps
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String doGet(String url,List<NameValuePair> nvps) 
			throws ClientProtocolException,IOException, URISyntaxException{
		URI uri = new URIBuilder(url).setParameters(nvps).build();
		return doGet(uri.toString());
	}
	
	public String doGet(String url) 
			throws ClientProtocolException,IOException, URISyntaxException{
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
        
        return null;
	}
	
	
	/**
	 * 带有参数的post请求
	 * @param url
	 * @param parameters
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url, List<NameValuePair> parameters) throws ClientProtocolException, IOException{
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        
        //伪装成浏览器访问
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,"UTF-8");
        
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        int code;
        try {
            // 执行请求
             response = httpclient.execute(httpPost);
             code = response.getStatusLine().getStatusCode();
             String content = EntityUtils.toString(response.getEntity(), "UTF-8");
             return new HttpResult(code,content);
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }
	
	/**
	 * 没有参数的post请求
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url) throws ClientProtocolException, IOException{
		return doPost(url, null);
	}
}
