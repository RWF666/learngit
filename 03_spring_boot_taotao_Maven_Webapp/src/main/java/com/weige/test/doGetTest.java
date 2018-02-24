package com.weige.test;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.sun.jndi.toolkit.url.Uri;

public class doGetTest {
	 public static void main(String[] args) throws Exception {
	        // ����Httpclient����
	        CloseableHttpClient httpclient = HttpClients.createDefault();
	        
	        URI uri = new URIBuilder("http://www.baidu.com/s").setParameter("wd", "java").build();
	        // ����http GET����
	        HttpGet httpGet = new HttpGet(uri);

	        CloseableHttpResponse response = null;
	        try {
	            // ִ������
	            response = httpclient.execute(httpGet);
	            // �жϷ���״̬�Ƿ�Ϊ200
	            if (response.getStatusLine().getStatusCode() == 200) {
	                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
	                System.out.println("���ݳ��ȣ�"+content);
	            }
	        } finally {
	            if (response != null) {
	                response.close();
	            }
	            httpclient.close();
	        }

	    }
}
