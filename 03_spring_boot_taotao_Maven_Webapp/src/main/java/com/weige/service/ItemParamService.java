package com.weige.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.mapper.TbItemCatMapper;
import com.weige.mapper.TbItemMapper;
import com.weige.mapper.TbItemParamMapper;
import com.weige.model.TbItem;
import com.weige.model.TbItemCat;
import com.weige.model.TbItemExample;
import com.weige.model.TbItemParam;
import com.weige.model.TbItemParamExample;
import com.weige.model.TbItemParamExample.Criteria;


@Service
public class ItemParamService {
	@Autowired
	private TbItemParamMapper tbItemParamMapper;
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	public TbItemParam queryByItemCatId(Long itemCatId) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(itemCatId);
		List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExample(example);
		return tbItemParamList.size()>0?tbItemParamList.get(0):null;
	}
	
	@Transactional
	public void save(TbItemParam tbItemParam) {
		tbItemParamMapper.insert(tbItemParam);
	}

	public List<TbItemParam> listItemParam() {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		List<TbItemParam> selectList = tbItemParamMapper.selectByExample(example);
		for(int i=0;i<selectList.size();i++){
			TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(selectList.get(i).getId());
			selectList.get(i).setItemCatName(tbItemCat.getName());
		}
		return selectList;
	}

	public void deleteInIds(List<Long> ids) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		tbItemParamMapper.deleteByExample(example);
	}
}
