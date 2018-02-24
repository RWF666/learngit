package com.weige.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weige.model.TbItem;
import com.weige.pojo.SolrResult;

@Service
public class SolrjService {
	
	public static final Integer ROWS = 24;

    // ����http��solr����
	@Autowired
    private HttpSolrServer httpSolrServer;

    /**
     * �������ݵ�solr����
     * 
     * @param foo
     * @throws Exception
     */
    public void add(TbItem tbItem) throws Exception {
        this.httpSolrServer.addBean(tbItem); //������ݵ�solr������
        this.httpSolrServer.commit(); //�ύ
    }

    public void deleteInIds(List<String> ids) throws Exception {
        this.httpSolrServer.deleteById(ids);
        this.httpSolrServer.commit(); //�ύ
    }
    
    public void deleteById(String id) throws Exception {
        this.httpSolrServer.deleteById(id);
        this.httpSolrServer.commit(); //�ύ
    }

    public SolrResult<TbItem> search(String keyword, Integer page) throws Exception {
        SolrQuery solrQuery = new SolrQuery(); //������������
        solrQuery.setQuery("title:"+keyword+" AND status:1");//�����ؼ���
        // ���÷�ҳ start=0���Ǵ�0��ʼ����rows=5��ǰ����5����¼���ڶ�ҳ���Ǳ仯start���ֵΪ5�Ϳ����ˡ�
        solrQuery.setStart((Math.max(page, 1) - 1) * ROWS);
        solrQuery.setRows(ROWS);

        //�Ƿ���Ҫ����
        boolean isHighlighting = !StringUtils.equals("*", keyword) && StringUtils.isNotEmpty(keyword);

        if (isHighlighting) {
            // ���ø���
            solrQuery.setHighlight(true); // �����������
            solrQuery.addHighlightField("title");// �����ֶ�
            solrQuery.setHighlightSimplePre("<em>");// ��ǣ������ؼ���ǰ׺
            solrQuery.setHighlightSimplePost("</em>");// ��׺
        }

        // ִ�в�ѯ
        QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);
        List<TbItem> items = queryResponse.getBeans(TbItem.class);
        if (isHighlighting) {
            // �������ı�������д�ص����ݶ�����
            Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
            for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
                for (TbItem item : items) {
                    if (!highlighting.getKey().equals(item.getId().toString())) {
                        continue;
                    }
                    item.setTitle(StringUtils.join(highlighting.getValue().get("title"), ""));
                    break;
                }
            }
        }

        return new SolrResult(queryResponse.getResults().getNumFound(), items);
    }

}
