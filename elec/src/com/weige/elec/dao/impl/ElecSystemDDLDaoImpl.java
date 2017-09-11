package com.weige.elec.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.New;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecSystemDDLDao;
import com.weige.elec.domain.ElecSystemDDL;



@Repository(IElecSystemDDLDao.SERVICE_NAME)
public class ElecSystemDDLDaoImpl  extends CommonDaoImpl<ElecSystemDDL> implements IElecSystemDDLDao {

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
		/*List<ElecSystemDDL> systemList = new ArrayList<ElecSystemDDL>();
		String hql = "select distinct o.keyword from ElecSystemDDL o";
		List<Object> list = this.getHibernateTemplate().find(hql);
		//��֯ҳ�淵�صĽ��
		if(list!=null&&list.size()>0){
			for(Object o:list){
				ElecSystemDDL elecSystemDDL = new ElecSystemDDL();
				elecSystemDDL.setKeyword(o.toString());
				systemList.add(elecSystemDDL);
			}
		}*/
		/**ʹ��hql���ֱ�ӽ�ͶӰ���ֶη��õ�������*/
		String hql = "select distinct new com.weige.elec.domain.ElecSystemDDL(o.keyword) from ElecSystemDDL o";
		List<ElecSystemDDL> systemList = this.getHibernateTemplate().find(hql);
		return systemList;
	}
	
	/**  
	* @Name: findDdlNameByKeywordAndDdlCode
	* @Description: ʹ���������ͺ�������ı�ţ���ȡ�������ֵ
	* @Author: �������ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2014-12-1���������ڣ�
	* @Parameters: String keywrod,��������
	* 			   String ddlCode��������ı��
	* @Return: String���������ֵ
	*/
	public String findDdlNameByKeywordAndDdlCode(final String keyword, final String ddlCode) {
		final String hql = "select o.ddlName from ElecSystemDDL o where o.keyword=? and o.ddlCode=?";
		List<Object> list = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0, keyword);
				query.setParameter(1, Integer.parseInt(ddlCode));
				//������������
				query.setCacheable(true);
				return query.list();
			}
			
		});
		//List<Object> list = this.getHibernateTemplate().find(hql, new Object[]{keyword,Integer.parseInt(ddlCode)});
		//�����������ֵ
		String ddlName = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlName = o.toString();
		}
		return ddlName;
	}

	/**  
	* @Name: findDdlCodeByKeywordAndDdlName
	* @Description: ʹ���������ͺ��������ֵ����ȡ������ı��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-14���������ڣ�
	* @Parameters: String keyword, ��������
	* 				String ddlName���������ֵ
	* @Return: ������ı��
	*/
	public String findDdlCodeByKeywordAndDdlName(final String keyword, final String ddlName) {
		final String hql = "select o.ddlCode from ElecSystemDDL o where o.keyword=? and o.ddlName=?";
		List<Object> list = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0,keyword);
				query.setParameter(1,ddlName);
				//������������
				query.setCacheable(true);
				return query.list();
			}
			
		});
		//List<Object> list = this.getHibernateTemplate().find(hql, new Object[]{keyword,Integer.parseInt(ddlCode)});
		//�����������ֵ
		String ddlCode = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlCode = o.toString();
		}
		return ddlCode;
	}
}
