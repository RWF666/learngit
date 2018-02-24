package com.weige.receive;

import java.io.IOException;

import net.sf.jsqlparser.expression.StringValue;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.weige.model.TbItem;
import com.weige.pojo.message;
import com.weige.service.BaseService;
import com.weige.service.ItemService;
import com.weige.service.RedisService;
import com.weige.service.SolrjService;

@Component
public class RabbitReceive extends BaseService{
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private SolrjService solrjService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@RabbitListener(queues="frontQueue")    //¼àÌýÆ÷¼àÌýÖ¸¶¨µÄQueue
    public void processA(String msg) {
        try {
			JsonNode jsonNode = MAPPER.readTree(msg);
			long itemId = jsonNode.get("itemId").asLong();
			String key = TAOTAO_MANAGE_ITEM_DETAIL_+itemId;
			//É¾³ý»º´æ
			if(redisService.get(key)!=null){
				redisService.del(key);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	@RabbitListener(queues="backQueue")    //¼àÌýÆ÷¼àÌýÖ¸¶¨µÄQueue
    public void processB(String msg) {
		try {
			JsonNode jsonNode = MAPPER.readTree(msg);
			long itemId = jsonNode.get("itemId").asLong();
			String type = jsonNode.get("type").asText();
			if(StringUtils.equals(type, "insert") || StringUtils.equals(type, "update")){
				TbItem tbItem = itemService.findByItemId(itemId);
				httpSolrServer.addBean(tbItem);
				httpSolrServer.commit();
			}else if (StringUtils.equals(type, "delete")) {
				solrjService.deleteById(String.valueOf(itemId));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
