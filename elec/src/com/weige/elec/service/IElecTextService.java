package com.weige.elec.service;

import java.util.List;

import com.weige.elec.domain.ElecText;

public interface IElecTextService {
	public static final String SERVICE_NAME = "com.weige.elec.service.impl.ElecTextServiceImpl";
	void saveElecText(ElecText elecText);
	List<ElecText> findCollectionByConditionNoPage(ElecText elecText);
}
