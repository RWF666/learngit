package com.weige.elec.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.weige.elec.domain.ElecUser;
import com.weige.elec.utils.PageInfo;


public interface IElecUserDao extends ICommonDao<ElecUser> {
	
	public static final String SERVICE_NAME = "com.weige.elec.dao.impl.ElecUserDaoImpl";

	List<ElecUser> findCollectionByConditionNoPageWithSql(String condition,
			Object[] params, Map<String, String> orderby);

	List<Object[]> chartUser(String zName, String eName);
}
