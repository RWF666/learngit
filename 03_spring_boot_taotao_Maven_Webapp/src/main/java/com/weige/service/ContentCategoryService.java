package com.weige.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.mapper.TbContentCategoryMapper;
import com.weige.model.TbContentCategory;
import com.weige.model.TbContentCategoryExample;
import com.weige.model.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	

	public List<TbContentCategory> queryContentCategoryList(Long id) {
		TbContentCategoryExample example =new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		return tbContentCategoryMapper.selectByExample(example);
	}
	
	@Transactional
	public void addContentCateGory(TbContentCategory tbContentCategory) {
		tbContentCategoryMapper.insert(tbContentCategory);
		
	}

	public TbContentCategory findParentByParentId(Long parentId) {
		return tbContentCategoryMapper.selectByPrimaryKey(parentId);
	}

	@Transactional
	public void rename(TbContentCategory tbContentCategory) {
		tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
	}
	
	@Transactional
	public void update(TbContentCategory parent) {
		tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
		
	}

	public List<TbContentCategory> findAllSonByParentId(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		return tbContentCategoryMapper.selectByExample(example);
	}
	
	@Transactional
	public void deleteInIds(List<Long> ids) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		tbContentCategoryMapper.deleteByExample(example);
	}

}
