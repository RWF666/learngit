package com.weige.test;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageHelper;
import com.mysql.fabric.xmlrpc.base.Array;
import com.weige.App;
import com.weige.model.TbItem;
import com.weige.service.ItemService;
import com.weige.service.SolrjService;
@RunWith(SpringRunner.class)
@SpringBootTest(classes={App.class})
public class ItemDataImportTest {
	@Autowired
	private ItemService itemService;
	@Autowired
	private SolrjService solrjService;
	    
    private HttpSolrServer httpSolrServer;
	
	@Test
	public void start(){
		int page = 1;
		List<TbItem> queryItemLsit=new ArrayList<TbItem>();;
		do {
			PageHelper.startPage(page,100);
			queryItemLsit = itemService.queryItemLsit();
			
			try {
				httpSolrServer.addBeans(queryItemLsit);
				httpSolrServer.commit();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			page++;
		} while (queryItemLsit.size()==100);
	}
}
