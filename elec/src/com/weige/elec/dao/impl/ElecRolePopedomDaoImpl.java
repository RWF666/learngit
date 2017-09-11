package com.weige.elec.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecRolePopedomDao;
import com.weige.elec.domain.ElecRolePopedom;



@Repository(IElecRolePopedomDao.SERVICE_NAME)
public class ElecRolePopedomDaoImpl  extends CommonDaoImpl<ElecRolePopedom> implements IElecRolePopedomDao {

	/**  
	* @Name: findPopedomByRoleIDs
	* @Description: ʹ�ý�ɫID��Hashtable�ļ��ϣ���ȡ�ĵ�ǰ��ɫID��������ѯ��ɫ��Ӧ��Ȩ�޲���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2018-08-06���������ڣ�
	* @Parameters: String���������
	* @Return: List<Object>����ʾȨ�޵��ַ������ϣ�
	* ʹ��hql������sql��䣺
	* SELECT DISTINCT o.mid FROM elec_role_popedom o WHERE 1=1 AND o.roleID IN ('1','2');
	*/
	public List<Object> findPopedomByRoleIDs(String condition) {
		//��ѯ���
		String hql = "SELECT DISTINCT o.mid FROM ElecRolePopedom o WHERE 1=1 AND o.roleID IN ("+condition+")";
		List<Object> list = this.getHibernateTemplate().find(hql);
		return list;
	}
}
