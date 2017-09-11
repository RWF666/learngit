package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecCommonMsgDao;
import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecText;


@Repository(IElecCommonMsgDao.SERVICE_NAME)
public class ElecCommonMsgImpl extends CommonDaoImpl<ElecCommonMsg> implements IElecCommonMsgDao{

}
