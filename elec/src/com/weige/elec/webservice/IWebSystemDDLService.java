package com.weige.elec.webservice;

import java.util.List;

import com.weige.elec.domain.ElecSystemDDL;


public interface IWebSystemDDLService {

	/**
	 * 发布服务的方法
	 *   * 参数String：传递的数据类型
	 *   * 返回值List<ElecSystemDDL>：根据传递的数据类型，查询该数据类型对应的结果
	 * */
	public List<ElecSystemDDL> findSystemByKeyword(String keyword);

}
