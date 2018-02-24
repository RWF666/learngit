package com.weige.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpConnectManager {

	public static void main(String[] args) throws Exception {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // �������������
        cm.setMaxTotal(200);
        // ����ÿ��������ַ�Ĳ�����
        cm.setDefaultMaxPerRoute(20);

        doGet(cm);
        doGet(cm);
    }

    public static void doGet(HttpClientConnectionManager cm) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        // ����http GET����
        HttpGet httpGet = new HttpGet("http://www.baidu.com/");

        CloseableHttpResponse response = null;
        try {
            // ִ������
            response = httpClient.execute(httpGet);
            // �жϷ���״̬�Ƿ�Ϊ200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("���ݳ��ȣ�" + content.length());
            }
        } finally {
            if (response != null) {
                response.close();
            }
            // �˴����ܹر�httpClient������ر�httpClient�����ӳ�Ҳ������
            // httpClient.close();
        }
    }


}
