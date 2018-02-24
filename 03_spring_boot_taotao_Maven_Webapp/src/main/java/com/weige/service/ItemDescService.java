package com.weige.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.mapper.TbItemDescMapper;
import com.weige.model.TbItemDesc;
import com.weige.model.TbItemDescExample;
import com.weige.model.TbItemDescExample.Criteria;
import com.weige.model.TbItemExample;


@Service
public class ItemDescService {
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	@Transactional
	public void save(TbItemDesc tbItemDesc) {
		tbItemDescMapper.insert(tbItemDesc);
	}

	public TbItemDesc queryDescById(Long itemId) {
		return tbItemDescMapper.selectByPrimaryKey(itemId);
	}
	
	@Transactional
	public void update(TbItemDesc tbItemDesc) {
		tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
	}
}
