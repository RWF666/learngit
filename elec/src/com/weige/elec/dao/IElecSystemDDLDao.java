package com.weige.elec.dao;

import java.util.List;
import java.util.Map;

import com.weige.elec.domain.ElecSystemDDL;


public interface IElecSystemDDLDao extends ICommonDao<ElecSystemDDL> {
	public static final String SERVICE_NAME = "com.weige.elec.dao.impl.ElecSystemDDLDaoImpl";

	List<ElecSystemDDL> findSystemDDLListByDistinct();
	
	String findDdlNameByKeywordAndDdlCode(String keywrod, String ddlCode);

	String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName);


}
