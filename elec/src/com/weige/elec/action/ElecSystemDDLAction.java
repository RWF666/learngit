package com.weige.elec.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.service.IElecSystemDDLService;
import com.weige.elec.utils.AnnotationLimit;



@SuppressWarnings("serial")
@Controller("elecSystemDDLAction")
@Scope(value="prototype")
public class ElecSystemDDLAction extends BaseAction<ElecSystemDDL> {
	
	ElecSystemDDL elecSystemDDL = this.getModel();
	
	/**数据字典表Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: 数据字典的首页显示
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-1（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/dictionaryIndex.jsp
	*/
	@AnnotationLimit(mid="aq",pid="am")
	public String home(){
		// 1：查询数据库中已有的数据类型，返回List<ElecSystemDDL>集合，遍历到页面的下拉菜单中
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByDistinct();
		request.setAttribute("list", list);
		return "home";
	}
	/**  
	* @Name: edit
	* @Description: 跳转到数据字典编辑页面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-1（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/dictionaryEdit.jsp
	*/
	@AnnotationLimit(mid="eb",pid="ea")
	public String edit(){
		//获取数据类型
		String keyword = elecSystemDDL.getKeyword();
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(keyword);
		request.setAttribute("list", list);
		return "edit";
	}
	/**  
	* @Name: save
	* @Description: 保存数据字典
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-1（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到system/dictionaryEdit.jsp
	*/
	@AnnotationLimit(mid="ec",pid="ea")
	public String save(){
		String keyword = elecSystemDDL.getKeyword();
		elecSystemDDLService.saveSystemDDL(elecSystemDDL);
		return "save";
	}
}
