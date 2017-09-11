package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecRoleDao;
import com.weige.elec.domain.ElecRole;



@Repository(IElecRoleDao.SERVICE_NAME)
public class ElecRoleDaoImpl  extends CommonDaoImpl<ElecRole> implements IElecRoleDao {

}
