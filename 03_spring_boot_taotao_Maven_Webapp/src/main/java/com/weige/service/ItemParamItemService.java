package com.weige.service;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.weige.mapper.TbItemParamItemMapper;
import com.weige.model.TbItemParamItem;
import com.weige.model.TbItemParamItemExample;
import com.weige.model.TbItemParamItemExample.Criteria;



@Service
public class ItemParamItemService {
	
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public TbItemParamItem queryByItemId(Long itemId) {
		
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> tbItemParamItemlist = tbItemParamItemMapper.selectByExample(example);
		return tbItemParamItemlist.size()>0?tbItemParamItemlist.get(0):null;
	}
	
	/**
	 * 商品规格参数的html拼接
	 * @param itemId
	 * @return
	 */
	public String queryParamById(Long itemId) {
		TbItemParamItem itemParam = queryByItemId(itemId);
		if(itemParam!=null){
			String paramData = itemParam.getParamData();
			try {
				ArrayNode nodes = (ArrayNode) MAPPER.readTree(paramData);
				StringBuilder sb = new StringBuilder();
				sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
				for(JsonNode node:nodes){
					String group = node.get("group").asText();
					sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">"+group+"</th></tr>");
					ArrayNode params = (ArrayNode) node.get("params");
					for(JsonNode param:params){
						sb.append("<tr><td class=\"tdTitle\">"+param.get("k").asText()+"</td><td>"+param.get("v").asText()+"</td></tr>");
					}
					
				}
				sb.append("</tbody></table>");
				
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
