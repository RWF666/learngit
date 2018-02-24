package com.weige.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weige.mapper.TbOrderShippingMapper;
import com.weige.model.TbOrderShipping;
import com.weige.model.TbOrderShippingExample;
import com.weige.model.TbOrderShippingExample.Criteria;

@Service
public class OrderShippingService {
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;

	public void insert(TbOrderShipping orderShipping) {
		tbOrderShippingMapper.insert(orderShipping);
		
	}

	public TbOrderShipping findByOrderId(String orderId) {
		TbOrderShippingExample example = new TbOrderShippingExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<TbOrderShipping> orderShippings = tbOrderShippingMapper.selectByExample(example);
		return orderShippings.size()>0?orderShippings.get(0):null;
	}
}
