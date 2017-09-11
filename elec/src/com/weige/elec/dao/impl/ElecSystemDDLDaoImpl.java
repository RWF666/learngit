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
	* @Description: 查询数据字典，去掉重复值
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-1（创建日期）
	* @Parameters: 无
	* @Return: List<ElecSystemDDL>：存放数据类型的集合
	*/
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		/*List<ElecSystemDDL> systemList = new ArrayList<ElecSystemDDL>();
		String hql = "select distinct o.keyword from ElecSystemDDL o";
		List<Object> list = this.getHibernateTemplate().find(hql);
		//组织页面返回的结果
		if(list!=null&&list.size()>0){
			for(Object o:list){
				ElecSystemDDL elecSystemDDL = new ElecSystemDDL();
				elecSystemDDL.setKeyword(o.toString());
				systemList.add(elecSystemDDL);
			}
		}*/
		/**使用hql语句直接将投影的字段放置到对象中*/
		String hql = "select distinct new com.weige.elec.domain.ElecSystemDDL(o.keyword) from ElecSystemDDL o";
		List<ElecSystemDDL> systemList = this.getHibernateTemplate().find(hql);
		return systemList;
	}
	
	/**  
	* @Name: findDdlNameByKeywordAndDdlCode
	* @Description: 使用数据类型和数据项的编号，获取数据项的值
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-1（创建日期）
	* @Parameters: String keywrod,数据类型
	* 			   String ddlCode：数据项的编号
	* @Return: String：数据项的值
	*/
	public String findDdlNameByKeywordAndDdlCode(final String keyword, final String ddlCode) {
		final String hql = "select o.ddlName from ElecSystemDDL o where o.keyword=? and o.ddlCode=?";
		List<Object> list = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0, keyword);
				query.setParameter(1, Integer.parseInt(ddlCode));
				//启动二级缓存
				query.setCacheable(true);
				return query.list();
			}
			
		});
		//List<Object> list = this.getHibernateTemplate().find(hql, new Object[]{keyword,Integer.parseInt(ddlCode)});
		//返回数据项的值
		String ddlName = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlName = o.toString();
		}
		return ddlName;
	}

	/**  
	* @Name: findDdlCodeByKeywordAndDdlName
	* @Description: 使用数据类型和数据项的值，获取数据项的编号
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-14（创建日期）
	* @Parameters: String keyword, 数据类型
	* 				String ddlName，数据项的值
	* @Return: 数据项的编号
	*/
	public String findDdlCodeByKeywordAndDdlName(final String keyword, final String ddlName) {
		final String hql = "select o.ddlCode from ElecSystemDDL o where o.keyword=? and o.ddlName=?";
		List<Object> list = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0,keyword);
				query.setParameter(1,ddlName);
				//启动二级缓存
				query.setCacheable(true);
				return query.list();
			}
			
		});
		//List<Object> list = this.getHibernateTemplate().find(hql, new Object[]{keyword,Integer.parseInt(ddlCode)});
		//返回数据项的值
		String ddlCode = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlCode = o.toString();
		}
		return ddlCode;
	}
}
