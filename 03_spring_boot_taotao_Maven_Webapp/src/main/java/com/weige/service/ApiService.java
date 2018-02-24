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
	 * ���в�����get����
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
        // ����http GET����
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            // ִ������
            response = httpclient.execute(httpGet);
            // �жϷ���״̬�Ƿ�Ϊ200
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
	 * ���в�����post����
	 * @param url
	 * @param parameters
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url, List<NameValuePair> parameters) throws ClientProtocolException, IOException{
        // ����http POST����
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        
        //αװ�����������
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,"UTF-8");
        
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        int code;
        try {
            // ִ������
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
	 * û�в�����post����
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url) throws ClientProtocolException, IOException{
		return doPost(url, null);
	}
}
