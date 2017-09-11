package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecUserFileDao;
import com.weige.elec.domain.ElecUserFile;



@Repository(IElecUserFileDao.SERVICE_NAME)
public class ElecUserFileDaoImpl  extends CommonDaoImpl<ElecUserFile> implements IElecUserFileDao {

}
