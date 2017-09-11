package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecExportFieldsDao;
import com.weige.elec.domain.ElecExportFields;



@Repository(IElecExportFieldsDao.SERVICE_NAME)
public class ElecExportFieldsDaoImpl  extends CommonDaoImpl<ElecExportFields> implements IElecExportFieldsDao {

}
