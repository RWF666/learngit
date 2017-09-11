package com.weige.elec.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileUpload;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.domain.ElecFileUpload;
import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.service.IElecFileUploadService;
import com.weige.elec.service.IElecSystemDDLService;
import com.weige.elec.service.impl.ElecFileUploadServiceImpl;
import com.weige.elec.utils.AnnotationLimit;
import com.weige.elec.utils.LuceneUtils;
@SuppressWarnings("serial")
@Controller("elecFileUploadAction")
@Scope(value="prototype")
public class ElecFileUploadAction extends BaseAction<ElecFileUpload> {
	
	ElecFileUpload elecFileUpload = this.getModel();
	
	/**注入资料图纸管理Service*/
	@Resource(name=IElecFileUploadService.SERVICE_NAME)
	IElecFileUploadService elecFileUploadService;
	
	/**注入数据字典的Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: 资料图纸管理的首页显示
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-17（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到dataChart/dataChartIndex.jsp
	*/
	@AnnotationLimit(mid="af",pid="ae")
	public String home(){
		//1：加载页面上显示的下拉菜单，所属单位和图纸列表的列表
		this.initSystemDDL();
		return "home";
	}
	
	/**加载页面上显示的下拉菜单，所属单位和图纸列表的列表*/
	private void initSystemDDL() {
		//所属单位
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		//图纸类别
		List<ElecSystemDDL> picList = elecSystemDDLService.findSystemDDLListByKeyword("图纸类别");
		request.setAttribute("picList", picList);
	}
	
	/**  
	* @Name: add
	* @Description: 跳转到新增界面
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-17（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到dataChart/dataChartAdd.jsp
	*/
	@AnnotationLimit(mid="ig",pid="ia")
	public String add(){
		//1：加载页面上显示的下拉菜单，所属单位和图纸列表的列表
		this.initSystemDDL();
		return "add";
	}
	
	/**  
	* @throws InterruptedException 
	 * @Name: save
	* @Description: 保存资料图纸管理
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-18（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到dataChart/dataChartAdd.jsp
	*/
	@AnnotationLimit(mid="ib",pid="ia")
	public String save() throws InterruptedException{
		elecFileUploadService.saveFileUpload(elecFileUpload);
		
		/*//线程结束时，清空当前session
		request.getSession().removeAttribute("percent");*/
		
		return "save";
	}
	
	/**  
	* @Name: saaddListve
	* @Description: 使用图纸类别和所属单位查询对应的图纸上传文件信息
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-18（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到到dataChart/dataChartAddList.jsp
	* 					目的是将dataChartAddList.jsp的内容放到dataChartAdd.jsp的form2中
	*/
	@AnnotationLimit(mid="ic",pid="ia")
	public String addList(){
		List<ElecFileUpload> list = elecFileUploadService.findFileUploadListByCondition(elecFileUpload);
		request.setAttribute("list", list);
		return "addList";
	}
	
	/**  
	* @Name: download
	* @Description: 文件下载（struts2的方式）
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-18（创建日期）
	* @Parameters: 无
	* @Return: struts2的结果类型
	*/
	@AnnotationLimit(mid="id",pid="ia")
	public String download(){
		try {
			//获取文件ID
			Integer fileID = elecFileUpload.getSeqId();
			//1：使用文件ID，查询资料图纸管理表，获取到路径path
			ElecFileUpload fileUpload = elecFileUploadService.findFileByID(fileID);
			//路径path
			String path = ServletActionContext.getServletContext().getRealPath("")+fileUpload.getFileUrl();
			//文件名称
			String filename = fileUpload.getFileName();
			//可以出现中文
			filename = new String(filename.getBytes("gbk"),"iso8859-1");
			request.setAttribute("filename", filename);
			
			//2：使用路径path，查找到对应的文件，转化成InputStream
			InputStream in = new FileInputStream(new File(path));
			//与栈顶的InputStream关联
			elecFileUpload.setInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "download";
	}
	/**  
	* @throws UnsupportedEncodingException 
	 * @Name: luceneHome
	* @Description: 使用lucene进行检索
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-19（创建日期）
	* @Parameters: 无
	* @Return: string 跳转到dataChart/dataChartIndex.jsp
	*/
	@AnnotationLimit(mid="ie",pid="ia")
	public String luceneHome() throws UnsupportedEncodingException{
		this.initSystemDDL();
		System.out.println("乱码二："+elecFileUpload.getQueryString());
		elecFileUpload.setQueryString(URLDecoder.decode(elecFileUpload.getQueryString().replace(".", "%"),"utf-8"));
		System.out.println("乱码后："+elecFileUpload.getQueryString());
		//使用lucene查询索引库，在使用主键id查询数据库，返回List<ElecFileUpload>
		List<ElecFileUpload> list = elecFileUploadService.findFileUploadListByLuceneCondition(elecFileUpload);
		request.setAttribute("list", list);
		return "luceneHome";
	}
	
	/**  
	* @throws UnsupportedEncodingException 
	 * @Name: delete
	* @Description: 删除资料图纸管理的数据
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-19（创建日期）
	* @Parameters: 无
	* @Return: string 重定向到dataChart/elecFileUploadAction_home.do
	*/
	@AnnotationLimit(mid="if",pid="ia")
	public String delete() throws UnsupportedEncodingException{
		//获取主键ID
		Integer seqId = elecFileUpload.getHideId();
		//String hideProjId = elecFileUpload.getHideProjId();
		//String hideBelongTo = elecFileUpload.getHideBelongTo();
		System.out.println("乱码前："+elecFileUpload.getHideQueryString());
		elecFileUpload.setHideQueryString(URLEncoder.encode(elecFileUpload.getHideQueryString(),"utf-8").replace("%", "."));
		System.out.println("乱码1："+elecFileUpload.getHideQueryString());
		elecFileUploadService.deleteFileUploadByID(seqId);
		return "delete";
	}
}
