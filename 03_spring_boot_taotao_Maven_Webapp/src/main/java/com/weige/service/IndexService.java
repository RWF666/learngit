package com.weige.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.pagehelper.PageHelper;
import com.weige.model.TbContent;
import com.weige.pojo.EasyUIResult;
import com.weige.properties.MyProperties;

@Service
public class IndexService {
	/*@Autowired
	MyProperties myProperties;
	
	@Autowired
	ApiService apiService;*/
	
	@Autowired
	private ContentService contentService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public String getIndexAd1(Long categoryId) {
		//String url = myProperties.getTaotaoUrl()+myProperties.getAdUrl1();
		try {
			PageHelper.startPage(1, 20);
			List<TbContent> contentList = contentService.listContent(categoryId);
			//String jsonData = apiService.doGet(url);
			//JsonNode jsonNode = MAPPER.readTree(jsonData);
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			
			/*for(JsonNode node :jsonNode){
				Map<String,Object> map = new LinkedHashMap<String, Object>();
				map.put("srcB", node.get("pic").asText());
				map.put("height", 240);
				map.put("alt", node.get("title").asText());
				map.put("width", 670);
				map.put("src", node.get("pic").asText());
				map.put("widthB", 550);
				map.put("href", node.get("url").asText());
				map.put("heightB", 240);
				
				result.add(map);
			}*/
			for(TbContent tbContent:contentList){
				Map<String,Object> map = new LinkedHashMap<String, Object>();
				map.put("srcB", tbContent.getPic());
				map.put("height", 240);
				map.put("alt",  tbContent.getTitle());
				map.put("width", 670);
				map.put("src",  tbContent.getPic());
				map.put("widthB", 550);
				map.put("href",  tbContent.getUrl());
				map.put("heightB", 240);
				result.add(map);
			}
			//将java对象序列化成json对象
			return MAPPER.writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
