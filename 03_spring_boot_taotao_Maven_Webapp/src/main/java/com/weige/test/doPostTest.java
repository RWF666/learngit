package com.weige.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.sun.jndi.toolkit.url.Uri;

public class doPostTest {
	 public static void main(String[] args) throws Exception {

	        // 创建Httpclient对象
	        CloseableHttpClient httpclient = HttpClients.createDefault();
	       
	        // 创建http POST请求
	        HttpPost httpPost = new HttpPost("http://www.oschina.net/");
	        
	        //伪装成浏览器访问
	        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
	        
	        List<NameValuePair> parameters= new ArrayList<NameValuePair>();
	        parameters.add(new BasicNameValuePair("scope", "project"));
	        parameters.add(new BasicNameValuePair("q", "java"));
	        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
	        
	        httpPost.setEntity(formEntity);
	        CloseableHttpResponse response = null;
	        try {
	            // 执行请求
	            response = httpclient.execute(httpPost);
	            // 判断返回状态是否为200
	            if (response.getStatusLine().getStatusCode() == 200) {
	                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
	                System.out.println(content);
	            }
	        } finally {
	            if (response != null) {
	                response.close();
	            }
	            httpclient.close();
	        }

	    }

}
