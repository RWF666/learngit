package com.weige.elec.dao;

import java.util.List;

import com.weige.elec.domain.ElecRolePopedom;


public interface IElecRolePopedomDao extends ICommonDao<ElecRolePopedom> {
	
	public static final String SERVICE_NAME = "com.weige.elec.dao.impl.ElecRolePopedomDaoImpl";

	List<Object> findPopedomByRoleIDs(String condition);

}
