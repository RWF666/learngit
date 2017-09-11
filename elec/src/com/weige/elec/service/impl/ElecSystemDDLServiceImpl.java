package com.weige.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecSystemDDLDao;
import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.service.IElecSystemDDLService;
import com.weige.elec.utils.AnnotationLimit;



@Service(IElecSystemDDLService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecSystemDDLServiceImpl implements IElecSystemDDLService {
	/**�����ֵ��Dao*/
	@Resource(name=IElecSystemDDLDao.SERVICE_NAME)
	IElecSystemDDLDao elecSystemDDLDao;

	/**  
	* @Name: findSystemDDLListByDistinct
	* @Description: ��ѯ�����ֵ䣬ȥ���ظ�ֵ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-1���������ڣ�
	* @Parameters: ��
	* @Return: List<ElecSystemDDL>������������͵ļ���
	*/
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		List<ElecSystemDDL> list = elecSystemDDLDao.findSystemDDLListByDistinct();
		return list;
	}

	/**  
	* @Name: findSystemDDLListByKeyword
	* @Description: ������������Ϊ��������ѯ�����ֵ�
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-1���������ڣ�
	* @Parameters: String ��������
	* @Return: List<ElecSystemDDL>������������͵ļ���
	*/
	public List<ElecSystemDDL> findSystemDDLListByKeyword(String keyword) {
		String condition="";
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(keyword)){
			condition +=" and o.keyword=?";
			paramsList.add(keyword);
		}
		Object[] params = paramsList.toArray();
		//����
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.ddlCode", "asc");
		//���ö�������
		List<ElecSystemDDL> list = elecSystemDDLDao.findCollectionByConditionNoPageWithCache(condition, params, orderby);
		return list;
	}

	/**  
	* @Name: saveSystemDDL
	* @Description: ���������ֵ�
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-1���������ڣ�
	* @Parameters: ElecSystemDDL vo����
	* @Return: ��
	*/
	@Transactional(readOnly=false)
	public void saveSystemDDL(ElecSystemDDL elecSystemDDL) {
		//1����ȡҳ������ϵĲ���
		//��������
		String keyword = elecSystemDDL.getKeywordname();
		//ҵ���ʾ
		String typeflag = elecSystemDDL.getTypeflag();
		//�������ֵ�����飩
		String[] itemnames = elecSystemDDL.getItemname();
		//2����ȡ�ж�ҵ���߼��ı�ʾ���ж�
		if(typeflag!=null && typeflag.equals("new")){
			//* ����ҳ�洫�ݹ���������������ƣ���֯PO����ִ�б���
			this.saveDDL(keyword,itemnames);
		}
		// ���typeflag==add�������е��������ͻ����Ͻ��б༭���޸�
		else{
			//* ʹ���������ͣ���ѯ���������Ͷ�Ӧ��list��ɾ��list
			List<ElecSystemDDL> list = this.findSystemDDLListByKeyword(keyword);
			elecSystemDDLDao.deleteObjectByCollection(list);
		    //* ����ҳ�洫�ݹ���������������ƣ���֯PO����ִ�б���
			this.saveDDL(keyword, itemnames);
		}
	}
	
	//* ����ҳ�洫�ݹ���������������ƣ���֯PO����ִ�б���
	private void saveDDL(String keyword, String[] itemnames) {
		if(itemnames!=null && itemnames.length>0){
			for(int i=0;i<itemnames.length;i++){
				//��֯PO����ִ�б���
				ElecSystemDDL systemDDL = new ElecSystemDDL();
				systemDDL.setKeyword(keyword);
				systemDDL.setDdlCode(i+1);
				systemDDL.setDdlName(itemnames[i]);
				elecSystemDDLDao.save(systemDDL);
			}
		}
		
	}
	
	/**  
	* @Name: findDdlNameByKeywordAndDdlCode
	* @Description: ʹ���������ͺ�������ı�ţ���ȡ�������ֵ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-3���������ڣ�
	* @Parameters: String keyword, ��������
	* 				String ddlCode��������ı��
	* @Return: �������ֵ
	*/
	public String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode) {
		return elecSystemDDLDao.findDdlNameByKeywordAndDdlCode(keyword, ddlCode);
	}

	/**  
	* @Name: findDdlCodeByKeywordAndDdlName
	* @Description: ʹ���������ͺ��������ֵ����ȡ������ı��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-3���������ڣ�
	* @Parameters: String keyword, ��������
	* 				String ddlName���������ֵ
	* @Return: ������ı��
	*/
	public String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName) {
		return elecSystemDDLDao.findDdlCodeByKeywordAndDdlName(keyword, ddlName);
	}
	
}
