package com.weige.elec.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.weige.elec.domain.ElecPopedom;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.service.IElecRoleService;
import com.weige.elec.utils.AnnotationLimit;



@SuppressWarnings("serial")
@Controller("elecRoleAction")
@Scope(value="prototype")
public class ElecRoleAction extends BaseAction<ElecPopedom> {
	
	ElecPopedom elecPopedom = this.getModel();
	
	/**注入角色的Service*/
	@Resource(name=IElecRoleService.SERVICE_NAME)
	IElecRoleService elecRoleService;
	
	/**  
	* @Name: home
	* @Description: 权限管理的首页显示
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-05（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/roleIndex.jsp
	*/
	@AnnotationLimit(mid="ga",pid="0")
	public String home(){
		//1、查询系统中所有的角色
		List<ElecRole> roleList = elecRoleService.findAllRoleList();
		request.setAttribute("roleList", roleList);
		//2、查询系统中所有的权限
		//返回List<ElecPopedom>,存放所有的tr，也就是pid=0的集合，父集合
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomList();
		request.setAttribute("popedomList", popedomList);
		return "home";
	}
	
	/**  
	* @Name: edit
	* @Description: 跳转到编辑页面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-05（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/roleEdit.jsp
	*/
	@AnnotationLimit(mid="ga",pid="0")
	public String edit(){
		//角色ID
		String roleID = elecPopedom.getRoleID();
		//一：使用当前角色ID，查询系统中所有的权限，并显示（匹配） 
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomListByRoleID(roleID);
		request.setAttribute("popedomList", popedomList);
		//二：使用当前角色ID，查询系统中所有的用户，并显示（匹配）
		List<ElecUser> userList = elecRoleService.findAllUserListByRoleID(roleID);
		request.setAttribute("userList", userList);
		return "edit";
	}
	
	/**  
	* @Name: save
	* @Description: 保存角色权限，角色和用户的关联关系
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-06（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到system/roleIndex.jsp
	*/
	@AnnotationLimit(mid="gc",pid="ga")
	public String save(){
		elecRoleService.saveRole(elecPopedom);
		return "save";
	}
	
}
