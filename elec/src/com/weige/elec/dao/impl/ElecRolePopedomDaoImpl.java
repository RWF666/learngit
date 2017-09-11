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
	* @Description: 使用角色ID的Hashtable的集合，获取的当前角色ID条件，查询角色对应的权限并集
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2018-08-06（创建日期）
	* @Parameters: String：存放条件
	* @Return: List<Object>：表示权限的字符串集合：
	* 使用hql或者是sql语句：
	* SELECT DISTINCT o.mid FROM elec_role_popedom o WHERE 1=1 AND o.roleID IN ('1','2');
	*/
	public List<Object> findPopedomByRoleIDs(String condition) {
		//查询语句
		String hql = "SELECT DISTINCT o.mid FROM ElecRolePopedom o WHERE 1=1 AND o.roleID IN ("+condition+")";
		List<Object> list = this.getHibernateTemplate().find(hql);
		return list;
	}
}
