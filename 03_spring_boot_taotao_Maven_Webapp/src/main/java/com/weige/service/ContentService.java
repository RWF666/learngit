package com.weige.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.mapper.TbContentMapper;
import com.weige.model.TbContent;
import com.weige.model.TbContentExample;
import com.weige.model.TbContentExample.Criteria;

@Service
public class ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	
	@Transactional
	public void save(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insert(tbContent);
	}

	public List<TbContent> listContent(Long categoryId) {
		// TODO Auto-generated method stub
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		return tbContentMapper.selectByExampleWithBLOBs(example);
	}

	public void deleteContentInIds(List<Long> ids) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		tbContentMapper.deleteByExample(example);
	}

	public void update(TbContent content) {
		tbContentMapper.updateByPrimaryKeyWithBLOBs(content);
	}
	
}
