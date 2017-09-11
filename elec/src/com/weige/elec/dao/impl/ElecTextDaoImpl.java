package com.weige.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecText;

/**
 * @Repository
 * 	�൱���������ж���
 * 	<bean id="com.weige.elec.dao.impl.ElecTextDaoImpl" class="com.weige.elec.dao.impl.ElecTextDaoImpl">
 * @author RWF
 *
 */
@Repository(IElecTextDao.SERVICE_NAME)
public class ElecTextDaoImpl extends CommonDaoImpl<ElecText> implements IElecTextDao{

}
