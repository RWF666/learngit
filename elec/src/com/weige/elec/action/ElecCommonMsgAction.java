package com.weige.elec.action;

import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecText;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecTextService;
import com.weige.elec.utils.AnnotationLimit;
import com.weige.elec.utils.ValueUtils;

@SuppressWarnings("serial")
@Controller("elecCommonMsgAction")
@Scope(value="prototype")
public class ElecCommonMsgAction extends BaseAction<ElecCommonMsg>{
	ElecCommonMsg elecCommonMsg = this.getModel();
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	
	/**  
	* @Name: home
	* @Description: 运行监控的首页
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-31（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/actingIndex.jsp
	*/
	@AnnotationLimit(mid="ap",pid="am")
	public String home(){
		//1、查询数据库运行监控表的数据进行回显
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		//2、将elecCommonMsg压如栈顶,进行表单回显
		ValueUtils.putValueStack(elecCommonMsg);
		return "home";
	}
	
	/**  
	* @Name: save
	* @Description:保存运行监控数据
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-31（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到system/actingIndex.jsp
	*/
	@AnnotationLimit(mid="cb",pid="ca")
	public String save(){
		//模拟保存150次，方便计算百分比
		for(int i=1;i<=100;i++){
			elecCommonMsgService.saveCommonMsg(elecCommonMsg);
			request.getSession().setAttribute("percent", (double)i/100*100);//存放计算的百分比
		}
		//线程结束时，清空当前session
		request.getSession().removeAttribute("percent");
		return "save";
	}
	/**  
	* @Name: actingView
	* @Description: 使用highsliderJS完成查询设备运行情况的详细信息
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-7-31（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/actingView.jsp
	*/
	@AnnotationLimit(mid="ap",pid="am")
	public String actingView(){
		//查询运行监控的数据
		//1：查询数据库运行监控表的数据，返回惟一ElecCommonMsg
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		//2：将ElecCommonMsg对象压入栈顶，支持表单回显
		ValueUtils.putValueStack(commonMsg);
		return "actingView";
	}
	
	/**  
	* @throws Exception 
	* @Name: progressBar
	* @Description: 在页面执行保存后，使用ajax，计算执行的百分比情况，将结果显示到页面上
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-19（创建日期）
	* @Parameters: 无
	* @Return: ajax如果不需要跳转页面，返回null或者NONE
	*/
	@AnnotationLimit(mid="ap",pid="am")
	public String progressBar() throws Exception{
		//从session中获取操作方法中计算的百分比
		Double percent = (Double) request.getSession().getAttribute("percent");
		String res = "";
		//此时说明操作的业务方法仍然继续在执行
		if(percent!=null){
			//计算的小数，四舍五入取整
			int percentInt = (int) Math.rint(percent); 
			res = "<percent>" + percentInt + "</percent>";
		}
		//此时说明操作的业务方法已经执行完毕，session中的值已经被清空
		else{
			//存放百分比
			res = "<percent>" + 100 + "</percent>";
		}
		//定义ajax的返回结果是XML的形式
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		//存放结果数据，例如：<response><percent>88</percent></response>
		out.println("<response>");
		out.println(res);
		out.println("</response>");
		out.close();
		return null;
	}
}
