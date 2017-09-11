package com.weige.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.taglibs.standard.lang.jstl.ELEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecText;
import com.weige.elec.service.IElecTextService;

/**
 * @Service 
 * 	�൱����spring������
 * 	<bean id="com.weige.elec.service.IElecTextService" class="com.weige.elec.service.IElecTextService">
 * @author RWF
 *
 */
@Service(IElecTextService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecTextServiceImpl implements IElecTextService{
	@Resource(name=IElecTextDao.SERVICE_NAME)
	IElecTextDao elecTextDao;
	
	/**������Ա�*/
	@Transactional(readOnly=false)
	public void saveElecText(ElecText elecText) {
		elecTextDao.save(elecText);
	}
	/**ָ����ѯ������ѯ�б�*/
	@Override
	public List<ElecText> findCollectionByConditionNoPage(ElecText elecText) {
		//��ѯ����
		String condition = "";
		//��ѯ��������Ĳ���
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(elecText.getTextName())){
			condition += "and o.textName like ? ";
			paramsList.add("%"+elecText.getTextName()+"%");
		}
		if(StringUtils.isNotBlank(elecText.getTextRemark())){
			condition += "and o.textRemark like ?";
			paramsList.add("%"+elecText.getTextRemark()+"%");
		}
		//���ݿɱ����
		Object[] params = paramsList.toArray();
		//����
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("o.textDate", "asc");
		map.put("o.textName", "desc");
		//��ѯ
		List<ElecText> list = elecTextDao.findCollectionByConditionNoPage(condition,params,map);
		return list;
	}

}
