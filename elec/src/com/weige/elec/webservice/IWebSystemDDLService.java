package com.weige.elec.webservice;

import java.util.List;

import com.weige.elec.domain.ElecSystemDDL;


public interface IWebSystemDDLService {

	/**
	 * ��������ķ���
	 *   * ����String�����ݵ���������
	 *   * ����ֵList<ElecSystemDDL>�����ݴ��ݵ��������ͣ���ѯ���������Ͷ�Ӧ�Ľ��
	 * */
	public List<ElecSystemDDL> findSystemByKeyword(String keyword);

}
