package com.weige.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weige.mapper.TbOrderItemMapper;
import com.weige.model.TbOrderItem;
import com.weige.model.TbOrderItemExample;
import com.weige.model.TbOrderItemExample.Criteria;

@Service
public class OrderItemService {
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	
	public void insert(TbOrderItem tbOrderItem) {
		tbOrderItemMapper.insert(tbOrderItem);
	}

	public List<TbOrderItem> findByOrderId(String orderId) {
		TbOrderItemExample example = new TbOrderItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		return tbOrderItemMapper.selectByExample(example);
	}
}
