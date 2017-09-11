package com.weige.elec.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.domain.ElecUserFile;
import com.weige.elec.service.IElecSystemDDLService;
import com.weige.elec.service.IElecUserService;
import com.weige.elec.utils.AnnotationLimit;
import com.weige.elec.utils.ChartUtils;
import com.weige.elec.utils.DateUtils;
import com.weige.elec.utils.ExcelFileGenerator;
import com.weige.elec.utils.GenerateSqlFromExcel;
import com.weige.elec.utils.MD5keyBean;
import com.weige.elec.utils.PageInfo;
import com.weige.elec.utils.ValueUtils;

@SuppressWarnings("serial")
@Controller("elecUserAction")
@Scope(value="prototype")
public class ElecUserAction extends BaseAction<ElecUser> {
	
	ElecUser elecUser = this.getModel();
	
	/**注入用户Service*/
	@Resource(name=IElecUserService.SERVICE_NAME)
	IElecUserService elecUserService;
	
	/**注入数据字典Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: 用户管理的首页显示
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/userIndex.jsp
	*/
	@AnnotationLimit(mid="an",pid="am")
	public String home(){
		System.out.println(request.getParameter("pageNO"));
		//1：加载数据类型是所属单位的数据字典的集合，遍历在页面的下拉菜单中
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		//2：组织页面中传递的查询条件，查询用户表，返回List<ElecUser>
		List<ElecUser> userList = elecUserService.findUserListByCondition(elecUser);
		request.setAttribute("userList", userList);
		
		/**2017-08-21 添加分类  begin*/
		String initpage = request.getParameter("initpage");
		if(initpage!=null && initpage.equals("1")){
			return "list";
		}
		/**2017-08-21 添加分类  end*/
		/*try {
			int a = 1/0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("运行时异常");
		}*/
		return "home";
	}
	
	/**  
	* @Name: add
	* @Description: 跳转到用户管理的新增显示
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/userAdd.jsp
	*/
	@AnnotationLimit(mid="fb",pid="fa")
	public String add(){
		this.initSystemDDL();
		return "add";
	}
	
	/**  
	* @Name: home
	* @Description: 使用jquery的ajax完成二级联动，是所属对象，关联对象名称
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: 无
	* @Return: String：使用struts2的插件包
	*/
	@AnnotationLimit(mid="fd",pid="fa")
	public String findJctUnit(){
		// 1：获取所属单位下的数据项的值
		String jctID = elecUser.getJctID();
		// 2：使用该值作为数据类型，查询对应数据字典的值，返回List<ElecSystemDDL>
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(jctID);
		// 3：将List<ElecSystemDDL>转换成json的数组，将List集合放置到栈顶
		ValueUtils.putValueStack(list);
		return "findJctUnit";
	}
	
	/**  
	* @Name: checkUser
	* @Description: 使用jquery的ajax完成登陆名的后台验证，判断数据库中是否存在,保证数据的唯一性
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: 无
	* @Return: String：使用struts2的插件包
	*/
	@AnnotationLimit(mid="ff",pid="fa")
	public String checkUser(){
		// 1：获取所属单位下的数据项的值
		String logonName = elecUser.getLogonName();
		// 2：使用该值作为数据类型，查询对应数据字典的值，返回List<ElecSystemDDL>
		// 3：将List<ElecSystemDDL>转换成json的数组，将List集合放置到栈顶
		String message = elecUserService.checkUser(logonName);
		elecUser.setMessage(message);
		/*ValueUtils.putValueStack(message);*/
		return "checkUser";
	}
	
	/**  
	* @Name: save
	* @Description: 保存用户信息
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: 无
	* @Return: String 跳转到close.jsp
	*/
	@AnnotationLimit(mid="fc",pid="fa")
	public String save(){
		elecUserService.saveUser(elecUser);
		/**
		 * 用来判断
		 * roleflag==null：系统管理员操作编辑页面，此时保存：close.jsp
		 * roleflag==1：如果不是系统管理员操作编辑页面，此时保存：重定向到编辑页面
	 */
		String roleflag = elecUser.getRoleflag();
		//如果不是系统管理员操作编辑页面，此时保存：重定向到编辑页面
		if(roleflag!=null && roleflag.equals("1")){
			return "redirectEdit";
		}
		return "close";
	}
	
	/**  
	* @Name: edit
	* @Description: 跳转到编辑界面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-3（创建日期）
	* @Parameters: 无
	* @Return: String 跳转到system/userEdit.jsp
	*/
	@AnnotationLimit(mid="fd",pid="fa")
	public String edit(){
		String userID = elecUser.getUserID();
		ElecUser user = elecUserService.findUserByUserID(userID);
		user.setViewflag(elecUser.getViewflag());
		user.setRoleflag(elecUser.getRoleflag());
		this.initSystemDDL();
		//4：二级联动的表单回显
		//(1)获取到所属单位的编号
		String ddlCode = user.getJctID();
		//(2)使用所属单位和数据项的编号，获取数据项的值
		String ddlName = elecSystemDDLService.findDdlNameByKeywordAndDdlCode("所属单位",ddlCode);
		//(3)使用查询的数据项的值，作为数据类型，查询该数据类型的对应的集合，返回List<ElecSystemDDL>
		List<ElecSystemDDL> jctUnitList = elecSystemDDLService.findSystemDDLListByKeyword(ddlName);
		request.setAttribute("jctUnitList", jctUnitList);
		ValueUtils.putValueStack(user);
		
		return "edit";
	}
	
	/**1：加载数据字典，用来遍历性别，职位，所属单位，是否在职*/
	@AnnotationLimit(mid="fd",pid="fa")
	private void initSystemDDL() {
		List<ElecSystemDDL> sexList = elecSystemDDLService.findSystemDDLListByKeyword("性别");
		request.setAttribute("sexList", sexList);
		List<ElecSystemDDL> postList = elecSystemDDLService.findSystemDDLListByKeyword("职位");
		request.setAttribute("postList", postList);
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		List<ElecSystemDDL> isDutyList = elecSystemDDLService.findSystemDDLListByKeyword("是否在职");
		request.setAttribute("isDutyList", isDutyList);
	}
	
	/**  
	* @throws Exception 
	* @Name: download
	* @Description: 文件下载(普通方式)
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-4（创建日期）
	* @Parameters: 无
	* @Return: 无
	*//*
	public String download() throws Exception{
		//获取文件ID
		String fileID = elecUser.getFileID();
		
		//1、使用文件ID,查询用户文件表，获取到路径path
		ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
		
		//路径path
		String path = ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
		//文件名称
		String filename =  elecUserFile.getFileName();
		//可以出现中文
		filename = new String(filename.getBytes("gbk"),"iso8859-1");
		//填写下载文件的头部格式
//		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition","attachment;filename="+filename);//inline：内联,直接打开  attachment:附件,下载形式打开
		
		//2、使用路径path，查找到对应的文件，装换成InputStream
		InputStream in = new FileInputStream(new File(path));
		
		//3、从响应对象Response中获取输出流的outputStream
		OutputStream out = response.getOutputStream();
		
		//4、将输入流的信息，写入到输出流中
		IOUtils.copy(in, out);
		for(int b= -1;(b=in.read())!=-1;){
			out.write(b);
		}
		out.close();
		in.close();
		return NONE;
		
	}*/
	
	/**  
	* @throws Exception 
	* @Name: download
	* @Description: 文件下载(struts2方式)
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-4（创建日期）
	* @Parameters: 无
	* @Return: 无
	*/
	@AnnotationLimit(mid="fd",pid="fa")
	public String download() throws Exception{
		//获取文件ID
		String fileID = elecUser.getFileID();
		
		//1、使用文件ID,查询用户文件表，获取到路径path
		ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
		
		//路径path
		String path = ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
		//文件名称
		String filename =  elecUserFile.getFileName();
		//可以出现中文
		filename = new String(filename.getBytes("gbk"),"iso8859-1");
		request.setAttribute("filename", filename);
		//2、使用路径path，查找到对应的文件，装换成InputStream
		InputStream in = new FileInputStream(new File(path));
		
		//与栈顶的InputStream对象关联
		elecUser.setInputStream(in);
		
		return "download";
	}
	
	/**  
	* @Name: delete
	* @Description: 批量删除用户信息
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-4（创建日期）
	* @Parameters: 无
	* @Return: String 重定向到system/userIndex.jsp
	*/
	@AnnotationLimit(mid="fe",pid="fa")
	public String delete(){
		elecUserService.deleteUserByID(elecUser);
		/**添加执行删除,这样做可以定向到刚才的页数*/
		request.setAttribute("pageNO", request.getParameter("pageNO"));
		return "delete";
	}
	
	/**  
	* @throws UnsupportedEncodingException 
	* @Name: exportExcel
	* @Description: 将数据通过查询条件，导出对应数据的excel报表
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-21（创建日期）
	* @Parameters: 无
	* @Return: 无  不适用struts2进行导出
	*/
	/*@AnnotationLimit(mid="fg",pid="fa")
	public String exportExcel() throws UnsupportedEncodingException{
		//初始化数据
		//excel的标题数据（一条）
		ArrayList<String> fieldName = elecUserService.findFieldNameWithExcel();
		ArrayList<ArrayList<String>> fieldDate = elecUserService.findFieldDataWithExcel(elecUser);
		//调用封装的POI报表的导出类,完成excel报表的导出
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDate);
		String filename = "用户报表（"+DateUtils.dateToStringWithExcel(new Date())+"）.xls";
		filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		*//**response中进行设置，总结下载，导出，需要io流和头*//*
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+filename);
		response.setBufferSize(1024);
		try {
			//获取输出流
			OutputStream os = response.getOutputStream();
			excelFileGenerator.expordExcel(os);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	/**  
	* @throws Exception 
	 * @Name: exportExcel
	* @Description: 将数据通过查询条件，导出对应数据的excel报表
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-24（创建日期）
	* @Parameters: 无
	* @Return: 无  使用struts2进行导出
	*/
	@AnnotationLimit(mid="fg",pid="fa")
	public String exportExcel() throws Exception{
		//初始化数据
		//excel的标题数据（一条）
		ArrayList<String> fieldName = elecUserService.findFieldNameWithExcel();
		ArrayList<ArrayList<String>> fieldDate = elecUserService.findFieldDataWithExcel(elecUser);
		//调用封装的POI报表的导出类,完成excel报表的导出
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDate);
		String filename = "用户报表（"+DateUtils.dateToStringWithExcel(new Date())+"）.xls";
		filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		request.setAttribute("filename", filename);
		//获取输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		excelFileGenerator.expordExcel(os);
		
		byte[] buf = os.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		//将文件转换成输入流
		elecUser.setInputStream(in);
		return "exportExcel";
	}
	
	/**  
	* @Name: exportPage
	* @Description: 跳转到导入界面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-24（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	@AnnotationLimit(mid="fh",pid="fa")
	public String exportPage(){
		return "exportPage";
	}
	

	/**  
	* @throws Exception 
	 * @Name: exportPage
	* @Description: 跳转到导入界面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-24（创建日期）
	* @Parameters: 无
	* @Return: 
	*/
	@AnnotationLimit(mid="fh",pid="fa")
	public String importData() throws Exception{
		//获取上传的文件
		File fromFile = elecUser.getFile();
		//读取excel的所有数据，放在集合当中
		GenerateSqlFromExcel formExcel = new GenerateSqlFromExcel();
		ArrayList<String[]> arrayList = formExcel.generateUserSql(fromFile);
		/**
		 * List<String> errorList:用来添加错误信息的集合
		 *   * 如果没有错误：errorList==null
		 *   * 如果存在错误：errorList有值
		 */
		List<String> errorList = new ArrayList<String>();
		//将ArrayList<String[]>转换成List<ElecUser>，同时完成校验
		List<ElecUser> userList = this.fromExcelListToUserList(arrayList,errorList);
		//执行保存
		//如果存在错误：errorList有值
		if(errorList!=null && errorList.size()>0){
			request.setAttribute("errorList", errorList);
		}
		//如果没有错误：errorList==null
		else{//执行保存
			elecUserService.saveUserList(userList);
		}
		return "exportPage";
	}

	private List<ElecUser> fromExcelListToUserList(
			ArrayList<String[]> arrayList,List<String> errorList) {
		List<ElecUser> userList = new ArrayList<ElecUser>();
		if(arrayList!=null && arrayList.size()>0){
			for(int i = 0;i<arrayList.size();i++){
				//将每一行的数据封装到po对象中
				String[] arrays = arrayList.get(i); 
				ElecUser elecUser = new ElecUser();
				//将值从数组中读取出来，放置到PO对象中
				//登录名
				if(StringUtils.isNotBlank(arrays[0])){
					//校验登录名，在数据库中是否出现重复
					String message = elecUserService.checkUser(arrays[0]);
					//登录名不存在重复，此时可以保存
					if(message!=null && message.equals("3")){
						elecUser.setLogonName(arrays[0]);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(0+1)+"列，登录名在数据库中已经存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(0+1)+"列，登录名不能为空！");
				}
				//密码
				if(StringUtils.isNotBlank(arrays[1])){
					//md5的密码加密
					MD5keyBean bean = new MD5keyBean();
					String logonPwd = bean.getkeyBeanofStr(arrays[1]);
					elecUser.setLogonPwd(logonPwd);
				}
				//用户姓名
				if(StringUtils.isNotBlank(arrays[2])){
					elecUser.setUserName(arrays[2]);
				}
				//性别
				if(StringUtils.isNotBlank(arrays[3])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("性别",arrays[3]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setSexID(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(3+1)+"列，性别在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(3+1)+"列，性别不能为空！");
				}
				//所属单位
				if(StringUtils.isNotBlank(arrays[4])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("所属单位",arrays[4]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setJctID(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(4+1)+"列，所属单位在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(4+1)+"列，所属单位不能为空！");
				}
				//联系地址
				if(StringUtils.isNotBlank(arrays[5])){
					elecUser.setAddress(arrays[5]);
				}
				//是否在职
				if(StringUtils.isNotBlank(arrays[6])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("是否在职",arrays[6]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setIsDuty(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(6+1)+"列，是否在职在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(6+1)+"列，是否在职不能为空！");
				}
				//出生日期
				if(StringUtils.isNotBlank(arrays[7])){
					Date birthday = DateUtils.stringToDate(arrays[7]);
					elecUser.setBirthday(birthday);
				}
				//职位
				if(StringUtils.isNotBlank(arrays[8])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("职位",arrays[8]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setPostID(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(8+1)+"列，职位在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(8+1)+"列，职位不能为空！");
				}
				userList.add(elecUser);
			}
		}
		return userList;
	}
	
	/**  
	* @throws 用户 统计
	 * @Name: exportPage
	* @Description: 跳转到用户统计(按照所属单位)界面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-29（创建日期）
	* @Parameters: 无
	* @Return: string 跳转到system/userReport.jsp
	*/
	@AnnotationLimit(mid="fg",pid="fa")
	public String chartUser(){
		//查询数据库，构造对应的数据集合
		List<Object[]> list = elecUserService.chartUser("所属单位","jctID");
		String filename = ChartUtils.createBarChart(list);
		request.setAttribute("filename", filename);
		//1、使用jfreechart生成图片，把生成的图片放到chart文件夹下，返回文件名
		return "chartUser";
	}
	
	/**  
	* @throws 用户 统计
	 * @Name: exportPage
	* @Description: 跳转到用户统计(按照性别)界面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-29（创建日期）
	* @Parameters: 无
	* @Return: string 跳转到system/userReport.jsp
	*/
	@AnnotationLimit(mid="fg",pid="fa")
	public String chartSexFCF(){
		//查询数据库，构造对应的数据集合
		List<Object[]> list = elecUserService.chartUser("性别","sexID");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
				/**b.keyword,b.ddlName,COUNT(b.ddlCode)*/
				Object[] objects = (Object[])list.get(i);
				if(i==0){//组织第一个值
					String x = "男女比例统计";
					String y = "unit";//存在FusionChart中的一个问题，Y轴的显示不支持中文，所以我们用英文代替
					builder.append("<graph caption='用户统计报表("+objects[0].toString()+")' xAxisName='"+x+"' bgColor='FFFFDD' yAxisName='"+y+"' showValues='1'  decimals='0' baseFontSize='18'  maxColWidth='60' showNames='1' decimalPrecision='0'> ");
					builder.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='AFD8F8'/>");
				}
			    
			    if(i==list.size()-1){//组织最后一个值
			    	builder.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='FF8E46'/>");
			    	builder.append("</graph>");
			    }
		} 
		request.setAttribute("chart", builder);//request中存放XML格式的数据
		return "chartSexFCF";
	}
}
