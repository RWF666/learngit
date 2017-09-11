package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecCommonMsgContentDao;
import com.weige.elec.domain.ElecCommonMsgContent;



@Repository(IElecCommonMsgContentDao.SERVICE_NAME)
public class ElecCommonMsgContentDaoImpl  extends CommonDaoImpl<ElecCommonMsgContent> implements IElecCommonMsgContentDao {

}
