package com.weige.elec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecExportFieldsDao;
import com.weige.elec.domain.ElecExportFields;
import com.weige.elec.service.IElecExportFieldsService;



@Service(IElecExportFieldsService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecExportFieldsServiceImpl implements IElecExportFieldsService {

	/**�������ñ�Dao*/
	@Resource(name=IElecExportFieldsDao.SERVICE_NAME)
	IElecExportFieldsDao elecExportFieldsDao;
	

	/**  
	* @Name: findExportFieldsByID
	* @Description: ʹ������ID����ѯ��Ӧ�ĵ������ö���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-17���������ڣ�
	* @Parameters: String������ID
	* @Return: ElecExportFields��PO����
	*/
	public ElecExportFields findExportFieldsByID(String belongTo) {
		return elecExportFieldsDao.findObjectByID(belongTo);
	}
	
	/**  
	* @Name: saveSetExportExcel
	* @Description: ���±��浼�����õĲ���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-17���������ڣ�
	* @Parameters: ElecExportFields��VO����
	* @Return: ��
	*/
	@Transactional(readOnly=false)
	public void saveSetExportExcel(ElecExportFields elecExportFields) {
		elecExportFieldsDao.update(elecExportFields);
	}
}
