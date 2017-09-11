package com.weige.elec.service;

import java.util.List;

import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecText;

public interface IElecCommonMsgService {
	public static final String SERVICE_NAME = "com.weige.elec.service.impl.ElecCommonMsgServiceImpl";
	void saveCommonMsg(ElecCommonMsg elecCommonMsg);
	ElecCommonMsg findCommonMsg();
}
