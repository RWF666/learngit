package com.weige.elec.dao;

import java.util.List;
import java.util.Map;

import com.weige.elec.domain.ElecFileUpload;


public interface IElecFileUploadDao extends ICommonDao<ElecFileUpload> {
	
	public static final String SERVICE_NAME = "com.weige.elec.dao.impl.ElecFileUploadDaoImpl";

	List<ElecFileUpload> findFileUploadListByCondition(String condition,
			Object[] params, Map<String, String> orderby);

	
	
}
