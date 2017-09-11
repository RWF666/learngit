package com.weige.elec.action;

import java.awt.Menu;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.dao.impl.CommonDaoImpl;
import com.weige.elec.dao.impl.ElecUserDaoImpl;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecPopedom;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecText;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.form.MenuForm;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecRoleService;
import com.weige.elec.service.IElecTextService;
import com.weige.elec.service.IElecUserService;
import com.weige.elec.utils.LogonUtils;
import com.weige.elec.utils.MD5keyBean;
import com.weige.elec.utils.ValueUtils;

/*	@Controller("elecTextAction")
 * 	相当于spring容器中
 * 	<bean id="elecTextAction" class="com.weige.elec.action.ElecTextAction" scope="prototype">
 */
@SuppressWarnings("serial")
@Controller("elecMenuAction")
@Scope(value="prototype")
public class ElecMenuAction extends BaseAction<MenuForm>{
	MenuForm menuForm = this.getModel();
	/**运行监控service*/
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	
	/**用户service*/
	@Resource(name=IElecUserService.SERVICE_NAME)
	IElecUserService elecUserService;
	
	/**权限控制service*/
	@Resource(name=IElecRoleService.SERVICE_NAME)
	IElecRoleService elecRoleService;
	
	/**  
	* @Name: menuHome
	* @Description: 跳转到系统登录的首页
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-30 （创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/home.jsp
	*/
	public String menuHome(){
		
		String name = menuForm.getName();
		String password = menuForm.getPassword();
		
		/**验证码*/
		boolean flag = LogonUtils.checkNumber(request);
		System.out.println(flag);
		if(flag==false){
			this.addActionError("验证码错误");
			return "logonError";
		}
		
		ElecUser elecUser =elecUserService.findUserByLogonName(name);
		if(elecUser==null){
			this.addActionError("登录名输入错误");
			return "logonError";
		}else{
			if(password.trim().equals("")){
				this.addActionError("密码不能未空");
				return "logonError";
			}else{
				if(!new MD5keyBean().getkeyBeanofStr(password).equals(elecUser.getLogonPwd())){
					this.addActionError("密码输入错误");
					return "logonError";
				}
			}
			
		}
		/**二：判断用户是否分配了角色，如果分配了角色，将角色的信息存放起来*/
		/**
		 * 存放角色信息
		 * key：角色ID
		 * value：角色名称
		 */
		Hashtable<String, String> ht = new Hashtable<String, String>();
		Set<ElecRole> elecRoles = elecUser.getElecRoles();
		//当前用户没有分配角色
		if(elecRoles==null || 0==elecRoles.size()){
			this.addActionError("当前用户没有分配角色，请与管理员联系！");
			return "logonError";//跳转到登录页面
		}
		//如果分配了角色，将角色的信息存放起来
		else{
			for(ElecRole elecRole:elecRoles){
				//一个用户可以对应多个角色
				ht.put(elecRole.getRoleID(), elecRole.getRoleName());
			}
		}
		/**三：判断用户对应的角色是否分配了权限，如果分配了权限，将权限的信息存放起来（aa）*/
		//将权限的信息你存放到字符串中，存放的权限的mid（字符串的格式：aa@ab@ac@ad@ae） -----jquery的ztree动态数据加载
		String popedom = elecRoleService.findPopedomByRoleIDs(ht);
		if(StringUtils.isBlank(popedom)){
			this.addActionError("当前用户具有的角色没有分配权限，请与管理员联系！");
			return "logonError";//跳转到登录页面
		}
		
		/**验证码*/
		LogonUtils.remeberMe(name,password,request,response);
		
		//将ElecUser对象放置到Session中
		request.getSession().setAttribute("globle_user", elecUser);
		//将Hashtable中存放的角色信息，放置到Session中
		request.getSession().setAttribute("globle_role", ht);
		//将权限的字符串（格式：aa@ab@ac@ad@ae）存放到Session中
		request.getSession().setAttribute("globle_popedom", popedom);
		return "menuHome";
	}
	/**标题*/
	public String title(){
		return "title";
	}
	/**菜单*/
	public String left(){
		return "left";
	}
	/**框架大小改变*/
	public String change(){
		return "change";
	}
	/**  
	* @Name: loading
	* @Description: 跳转到功能显示页面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-31（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/loading.jsp
	*/
	public String loading(){
		//查询设备运行情况，放置到浮动框中
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(elecCommonMsg);
		return "loading";
	}
	/**  
	* @Name: logout
	* @Description: 重新登陆
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-30 （创建日期）
	* @Parameters: 无
	* @Return: String：重定向到index.jsp
	*/
	public String logout(){
		//清空所有Session
		request.getSession().invalidate();
		return "logout";
	}
	/**  
	* @Name: alermStation
	* @Description: 显示设站点运行情况
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-31 （创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/alermStation.jsp
	*/
	/**站点运行情况*/
	public String alermStation(){
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(elecCommonMsg);
		return "alermStation";
	}
	/**  
	* @Name: alermDevice
	* @Description: 显示设备运行情况
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-31（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到menu/alermDevice.jsp
	*/
	/**设备运行情况*/
	public String alermDevice(){
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(elecCommonMsg);
		return "alermDevice";
	}
	
	/**  
	* @Name: showMenuAction
	* @Description: 使用ajax动态加载左侧的树型菜单
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-7（创建日期）
	* @Parameters: 无
	* @Return: String：showMenu 使用strut2的json插件包
	*/
	public String showMenu(){
		//获取当前用户的角色
		Hashtable<String, String> ht = (Hashtable<String, String>) request.getSession().getAttribute("globle_role");
		//获取当前用户
		ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
		
		//从Session获取当前用户具有的权限的字符串
		String popedom = (String) request.getSession().getAttribute("globle_popedom");
		//查询当前用户的功能权限List<ElecPopedom>,转化成json
		List<ElecPopedom> list = elecRoleService.findPopedomListByString(popedom);
		
		//使用角色控制系统的url
		if(!ht.containsKey("1")){
			for(ElecPopedom elecPopedom:list){
				String mid = elecPopedom.getMid();
				String pid = elecPopedom.getPid();
				//改变用户管理的url
				if("an".equals(mid) && "am".equals(pid)){
					elecPopedom.setUrl("../system/elecUserAction_edit.do?userID="+elecUser.getUserID()+"&roleflag=1");
				}
			}
		}
		//将list转化成json，放入栈顶
		ValueUtils.putValueStack(list);
		return "showMenu";
	}
}
