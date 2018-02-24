package com.weige.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.mapper.TbItemMapper;
import com.weige.mapper.TbItemParamItemMapper;
import com.weige.mapper.TbItemParamMapper;
import com.weige.model.TbItem;
import com.weige.model.TbItemDesc;
import com.weige.model.TbItemDescExample;
import com.weige.model.TbItemExample;
import com.weige.model.TbItemExample.Criteria;
import com.weige.model.TbItemParamItem;


@Service
@Transactional
public class ItemService extends BaseService{
	//注意事务的传播性
	@Autowired ItemDescService itemDescService;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Transactional
	public void save(TbItem item,String desc,String itemParams) {
		//新增商品
		tbItemMapper.insert(item);
		
		//新增商品描述
		TbItemDesc tbItemDesc = new TbItemDesc(new Date(),new Date());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(item.getId());
		itemDescService.save(tbItemDesc);
		
		//新增规格参数数据
		if(StringUtils.isNotEmpty(itemParams)){
			TbItemParamItem tbItemParamItem = new TbItemParamItem(new Date(),new Date());
			tbItemParamItem.setItemId(item.getId());
			tbItemParamItem.setParamData(itemParams);
			tbItemParamItemMapper.insert(tbItemParamItem);
		}
		
		sendMsg(item.getId(), "insert");
		
	}
	
	public List<TbItem> queryItemLsit() {
		TbItemExample tbItemExample = new TbItemExample();
		tbItemExample.setOrderByClause("created asc");
		Criteria criteria = tbItemExample.createCriteria();
		criteria.andStatusNotEqualTo(3);
		return tbItemMapper.selectByExample(tbItemExample);
	}
	
	@Transactional
	public void deleteInIds(List<Long> idList) {
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteria = tbItemExample.createCriteria();
		criteria.andIdIn(idList);
		TbItem tbItem = new TbItem();
		tbItem.setStatus(new Byte("3"));
		tbItemMapper.updateByExampleSelective(tbItem, tbItemExample);
		
		for(Long id:idList){
			sendMsg(id, "delete");
		}
	}
	
	@Transactional
	public void update(TbItem item,String desc, TbItemParamItem tbItemParamItem) {
		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(item.getId());
		tbItemMapper.updateByExampleSelective(item, example);
		
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(item.getId());
		tbItemDesc.setUpdated(new Date());
		itemDescService.update(tbItemDesc);
		
		//更新规格参数数据
		if(StringUtils.isNotEmpty(tbItemParamItem.getParamData())
				&&StringUtils.isNotEmpty(String.valueOf(tbItemParamItem.getId())))
			tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
		
		//发送更新的消息
		sendMsg(item.getId(), "update");
	}

	public TbItem findByItemId(Long itemId) {
		//从缓存中命中
		String key = "TAOTAO_MANAGE_ITEM_DETAIL_"+itemId;
		try {
			String jsonData = redisService.get(key);
			if(StringUtils.isNotEmpty(jsonData)){
					return MAPPER.readValue(jsonData, TbItem.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		try {
			redisService.set(key, MAPPER.writeValueAsString(item), 60*10);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	private void sendMsg(Long itemId,String type){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("itemId", itemId);
			map.put("type", type);
			map.put("created", System.currentTimeMillis());
			rabbitTemplate.convertAndSend("taotao_exchange", "item."+type, MAPPER.writeValueAsString(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
