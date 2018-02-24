package com.weige.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.mapper.TbItemCatMapper;
import com.weige.model.TbItemCat;
import com.weige.model.TbItemCatExample;
import com.weige.model.TbItemCatExample.Criteria;
import com.weige.pojo.ItemCatData;
import com.weige.pojo.ItemCatResult;


@Service
public class ItemCatService {
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	@Autowired
	private RedisService redisService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public List<TbItemCat> queryItemCatList(Long parentId){
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		Criteria criteria = tbItemCatExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		return tbItemCatMapper.selectByExample(tbItemCatExample);
	}

	public TbItemCat queryItemCatById(Long id) {
		return tbItemCatMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * ȫ����ѯ������������״�ṹ
	 * @return
	 */
	public ItemCatResult queryAllToTree() {
		ItemCatResult result = new ItemCatResult();
		// ȫ��������������ڴ����������νṹ
		//EasyUIResult<TbItemCat> easyUIResult = queryList(1, 99999, Order.formString("sort_order.asc"));
		//List<TbItemCat> cats = easyUIResult.getRows();
		
		//�ӻ���������
		String key = "TAOTAO_MANAGE_ITEMCAT_ALL_TREE";
		
		try {
			String cacheData = redisService.get(key);
				if(StringUtils.isNotEmpty(cacheData)){
				return MAPPER.readValue(cacheData, ItemCatResult.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		
		List<TbItemCat> cats = tbItemCatMapper.selectByExample(tbItemCatExample);
		
		// תΪmap�洢��keyΪ���ڵ�ID��valueΪ���ݼ���
		Map<Long, List<TbItemCat>> itemCatMap = new HashMap<Long, List<TbItemCat>>();
		for (TbItemCat itemCat : cats) {
			if(!itemCatMap.containsKey(itemCat.getParentId())){
				itemCatMap.put(itemCat.getParentId(), new ArrayList<TbItemCat>());
			}
			itemCatMap.get(itemCat.getParentId()).add(itemCat);
		}
		
		// ��װһ������
		List<TbItemCat> itemCatList1 = itemCatMap.get(0L);
		for (TbItemCat itemCat : itemCatList1) {
			ItemCatData itemCatData = new ItemCatData();
			itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
			itemCatData.setName("<a href='"+itemCatData.getUrl()+"'>"+itemCat.getName()+"</a>");
			result.getItemCats().add(itemCatData);
			if(!itemCat.getIsParent()){
				continue;
			}
			
			// ��װ��������
			List<TbItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
			List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
			itemCatData.setItems(itemCatData2);
			for (TbItemCat itemCat2 : itemCatList2) {
				ItemCatData id2 = new ItemCatData();
				id2.setName(itemCat2.getName());
				id2.setUrl("/products/" + itemCat2.getId() + ".html");
				itemCatData2.add(id2);
				if(itemCat2.getIsParent()){
					// ��װ��������
					List<TbItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
					List<String> itemCatData3 = new ArrayList<String>();
					id2.setItems(itemCatData3);
					for (TbItemCat itemCat3 : itemCatList3) {
						itemCatData3.add("/products/" + itemCat3.getId() + ".html|"+itemCat3.getName());
					}
				}
			}
			if(result.getItemCats().size() >= 14){
				break;
			}
		}
		try {
			//��������滺����
			redisService.set(key,MAPPER.writeValueAsString(result),60 * 60 * 24 * 30);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
