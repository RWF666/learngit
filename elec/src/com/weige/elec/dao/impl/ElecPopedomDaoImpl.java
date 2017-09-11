package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecPopedomDao;
import com.weige.elec.domain.ElecPopedom;



@Repository(IElecPopedomDao.SERVICE_NAME)
public class ElecPopedomDaoImpl  extends CommonDaoImpl<ElecPopedom> implements IElecPopedomDao {

}
