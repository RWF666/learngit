package com.weige.test;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RequestConfigDemo {

	public static void main(String[] args) throws Exception {

        // ����Httpclient����
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // ����http GET����
        HttpGet httpGet = new HttpGet("http://www.baidu.com/");

        // ��������������Ϣ
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // �������ӵ��ʱ��
                .setConnectionRequestTimeout(500) // �����ӳ��л�ȡ�����ӵ��ʱ��
                .setSocketTimeout(10 * 1000) // ���ݴ�����ʱ��
                .setStaleConnectionCheckEnabled(true) // �ύ����ǰ���������Ƿ����
                .build();
        // ��������������Ϣ
        httpGet.setConfig(config);

        CloseableHttpResponse response = null;
        try {
            // ִ������
            response = httpclient.execute(httpGet);
            // �жϷ���״̬�Ƿ�Ϊ200
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
