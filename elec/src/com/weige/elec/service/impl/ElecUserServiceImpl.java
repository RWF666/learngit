package com.weige.elec.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecExportFieldsDao;
import com.weige.elec.dao.IElecSystemDDLDao;
import com.weige.elec.dao.IElecUserDao;
import com.weige.elec.dao.IElecUserFileDao;
import com.weige.elec.domain.ElecExportFields;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.domain.ElecUserFile;
import com.weige.elec.service.IElecUserService;
import com.weige.elec.utils.FileUtils;
import com.weige.elec.utils.ListUtils;
import com.weige.elec.utils.MD5keyBean;
import com.weige.elec.utils.PageInfo;



@Service(IElecUserService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecUserServiceImpl implements IElecUserService {

	/**用户表Dao*/
	@Resource(name=IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;
	
	/**用户附件表Dao*/
	@Resource(name=IElecUserFileDao.SERVICE_NAME)
	IElecUserFileDao elecUserFileDao;
	
	/**数据字典表Dao*/
	@Resource(name=IElecSystemDDLDao.SERVICE_NAME)
	IElecSystemDDLDao elecSystemDDLDao;
	
	/**导出设置Dao*/
	@Resource(name=IElecExportFieldsDao.SERVICE_NAME)
	IElecExportFieldsDao elecExportFieldsDao;
	
	/**  
	* @Name: findUserListByCondition
	* @Description: 组织查询条件，查询用户列表
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: ElecUser:VO对象
	* @Return: List<ElecUser>：用户集合
	*/
	public List<ElecUser> findUserListByCondition(ElecUser elecUser) {
		//组织查询条件
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//用户名称
		String userName = elecUser.getUserName();
		if(StringUtils.isNotBlank(userName)){
			condition += " and o.userName like ?";
			paramsList.add("%"+userName+"%");
		}
		//所属单位
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//入职开始时间
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//入职结束时间
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object [] params = paramsList.toArray();
		//排序（按照入职时间的升序排列）
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		/**方案一： 查询用户表再转换数据字典表*/
		//List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage(condition, params, orderby);
		/**2017-08-21 添加分类  begin*/
		PageInfo pageInfo = new PageInfo(ServletActionContext.getRequest());
		List<ElecUser> list = elecUserDao.findCollectionByConditionWithPage(condition, params, orderby,pageInfo);
		ServletActionContext.getRequest().setAttribute("page",pageInfo.getPageBean());
		/**2017-08-21 添加分类  end*/
		/**
		 * 3：数据字典的转换
		 	* 使用数据类型和数据项的编号，查询数据字典，获取数据项的值
		 */
		this.convertSystemDDL(list);
		
		/**方案二：直接使用sql语句 完成一条语句*/
		//List<ElecUser> list = elecUserDao.findCollectionByConditionNoPageWithSql(condition, params, orderby);
		
		
		
		
		return list;
	}

	/**  
	* @Name: convertSystemDDL
	* @Description: 使用数据类型和数据项的编号，查询数据字典，获取数据项的值
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters:List<ElecUser> list:VO对象
	* @Return: 无
	*/
	private void convertSystemDDL(List<ElecUser> list) {
		if(list!=null && list.size()>0){
			for(ElecUser user:list){
				//性别
				String sexID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("性别",user.getSexID());
				user.setSexID(sexID);
				//职位
				String postID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("职位",user.getPostID());
				user.setPostID(postID);
			}
		}
	}
	
	
	/**  
	* @Name: checkUser
	* @Description: 验证登录名是否存在
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: String 登录名
	* @Return: String 返回message
	* 判断登录名是否出现重复，返回一个标识message属性
			* message=1：表示登录名为空，不可以保存
			* message=2：表示登录名在数据库中已经存在，不可以保存
			* message=3：表示登录名在数据库中不存在，可以保存
	*/
	
	@Override
	public String checkUser(String logonName) {
		String message = "";
		if(StringUtils.isNotBlank(logonName)){
			String condition = " and o.logonName = ?";
			String[] params = {logonName};
			List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage(condition, params, null);
			if(list!=null && list.size()>0){
				message = "2";
			}else {
				message = "3";
			}
		}else {
			message = "1";
		}
		
		return message;
	}
	

	/**  
	* @Name: saveUser
	* @Description: 保存用户信息
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-2（创建日期）
	* @Parameters: ElecUser VO对象
	* @Return: String 返回message
	*/
	@Transactional(readOnly=false)
	public void saveUser(ElecUser elecUser) {
		//1、遍历多个附件，组织附件po对象，完成文件上传，保存用户的附件信息（多条信息），建立附件表和用户表的关系
		this.saveUserFile(elecUser);
		
		//加密
		this.md5Password(elecUser);
		
		//获取页面的userID
		String userID = elecUser.getUserID();
		//update
		if(StringUtils.isNotBlank(userID)){
			elecUserDao.update(elecUser);
		}
		//save
		else {
			//2、组织po对象，保存用户（1条数据）
			elecUserDao.save(elecUser);
		}
		
		
	}
	
	private void md5Password(ElecUser elecUser) {
		String logonPwd = elecUser.getLogonPwd();
		
		//如果没有填写密码，设置初始密码为123
		if(StringUtils.isBlank(logonPwd)){
			logonPwd = "123";
		}
		String md5LogonPwd ="";
		String password = elecUser.getPassword();
		//编辑的时候，没有修改密码的时候，是不需要加密的
		if(password!=null && password.equals(logonPwd)){
			md5LogonPwd = logonPwd;
		}else {
			md5LogonPwd = new MD5keyBean().getkeyBeanofStr(logonPwd);
		}
		
		elecUser.setLogonPwd(md5LogonPwd);
	}

	@Transactional(readOnly=false)
	private void saveUserFile(ElecUser elecUser) {
		//上传时间
		Date progressTime = new Date();
 		//获取上传的文件
		File[] uploads = elecUser.getUploads();
		//获取上传的文件名
		String[] fileNames = elecUser.getUploadsFileName();
		//获取上传的文件类型
		String[] contentType = elecUser.getUploadsContentType();
		//实现文件上传
		ServletContext servletContext = ServletActionContext.getServletContext();
		//真是路径名
		String directory = servletContext.getRealPath("/files");
		if(uploads!=null&&uploads.length>0){
			for(int i = 0;i<uploads.length;i++){
				//组织附件的po对象
				ElecUserFile elecUserFile = new ElecUserFile();
				elecUserFile.setFileName(fileNames[i]);//文件名
				elecUserFile.setProgressTime(progressTime);//上传时间
				/**将文件上传，同时返回路径path*/
				String fileURL = FileUtils.fileUploadReturnPath(uploads[i],fileNames[i],"用户管理");
				elecUserFile.setFileURL(fileURL);//上传路径（保存，应用与下载）
				elecUserFile.setElecUser(elecUser);//与用户建立关系
				elecUserFileDao.save(elecUserFile);
			}
		}
	}
	

	/**  
	* @Name: findUserByID
	* @Description: 使用用户ID，查询用户对象
	* @Author:	饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-4（创建日期）
	* @Parameters: String：用户ID
	* @Return: ElecUser：用户信息
	*/
	public ElecUser findUserByUserID(String userID) {
		return elecUserDao.findObjectByID(userID);
		
	}
	
	/**  
	* @Name: findUserFileByID
	* @Description: 使用用户附件ID，查询用户附件对象
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-4（创建日期）
	* @Parameters: String：用户附件ID
	* @Return: ElecUserFile：用户附件信息
	*/
	@Transactional(readOnly=false)
	public ElecUserFile findUserFileByID(String fileID) {
		return elecUserFileDao.findObjectByID(fileID);
	}

	/**  
	* @Name: deleteUserByID
	* @Description: 使用用户ID批量删除用户
	*  	 1：删除该用户对应的文件
		 2：删除该用户对应的用户附件表数据
		 3：删除用户表的信息
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-4（创建日期）
	* @Parameters: String：用户附件ID
	* @Return: 无
	*/
	@Transactional(readOnly=false)
	public void deleteUserByID(ElecUser elecUser) {
		// 获取用户ID
		String userID = elecUser.getUserID();
		String[] userIDs = userID.split(", ");
		
		if (userIDs != null && userIDs.length > 0) {
			for (String uid : userIDs) {
				// 使用用户ID，查询用户对象，获取当前用户具有的附件
				ElecUser user = elecUserDao.findObjectByID(uid);
				Set<ElecUserFile> elecUserFiles = user.getElecUserFiles();
				if (elecUserFiles != null && elecUserFiles.size() > 0) {
					for (ElecUserFile elecUserFile : elecUserFiles) {
						// 1、删除该用户的文件
						// 获取路径
						String path = ServletActionContext.getServletContext()
								.getRealPath("") + elecUserFile.getFileURL();
						File file = new File(path);
						if (file.exists()) {
							file.delete();
						}

						// 2、删除每个用户的附件
						// elecUserFileDao.deleteObjectByIds(elecUserFile.getFileID());没必要这么删，直接删除用户的时候级联删除
					}
				}
				
				/**2017-8-06添加 同时删除与用户关联的数据*/
				Set<ElecRole> elecRoles = user.getElecRoles();
				if(elecRoles!=null && elecRoles.size()>0){
					for(ElecRole elecRole:elecRoles){
						elecRole.getElecUsers().clear();
					}
				}
			}
			// 删除用户信息
			elecUserDao.deleteObjectByIds(userIDs);
		}
	}

	/**  
	* @Name: findUserByLogonName
	* @Description: 使用登录名查询用户
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-6（创建日期）
	* @Parameters: String：登录名
	* @Return: ElecUser 用户
	*/
	public ElecUser findUserByLogonName(String name) {
		
		String condition = "";
		List<ElecUser> list = new ArrayList<ElecUser>();
		ElecUser elecUser= null ;
		if(StringUtils.isNotBlank(name)){
			condition =" and o.logonName=?";
			String[] params = {name};
			list = elecUserDao.findCollectionByConditionNoPage(condition, params, null);
		}
		if(list!=null &&  list.size()>0){
			elecUser = list.get(0);
		}
		return elecUser;
	}

	/**  
	* @Name: findFieldNameWithExcel
	* @Description: 获取excel的标题字段，通过导出设置表（动态表示）
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-17（创建日期）
	* @Parameters: 无
	* @Return: ArrayList<String> excel的标题
	*/
	public ArrayList<String> findFieldNameWithExcel() {
		ElecExportFields elecExportFields = elecExportFieldsDao.findObjectByID("5-1");
		ArrayList<String> fieldName = (ArrayList<String>) ListUtils.stringToList(elecExportFields.getExpNameList(), "#");
		return fieldName;
	}

	/**  
	* @Name: findFieldDateWithExcel
	* @Description: 获取excel的数据，通过导出设置表（动态表示）
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-17（创建日期）
	* @Parameters: 无
	* @Return: ArrayList<String> excel的数据
	*/
	public ArrayList<ArrayList<String>> findFieldDataWithExcel(ElecUser elecUser) {
		//组织数据
		ArrayList<ArrayList<String>> fieldData = new ArrayList<ArrayList<String>>();
		//组织投影查询的条件，从导出设置表中的英文字段获取
		ElecExportFields elecExportFields = elecExportFieldsDao.findObjectByID("5-1");
		String selectCondition = elecExportFields.getExpFieldName().replace("#", ",");
		List<String> zList = ListUtils.stringToList(elecExportFields.getExpNameList(), "#");
		List<ElecSystemDDL> elecSystemList= elecSystemDDLDao.findSystemDDLListByDistinct();
		List<String> keywordList = new ArrayList<String>();
		for(ElecSystemDDL elecSystemDDL:elecSystemList){
			keywordList.add(elecSystemDDL.getKeyword());
		}
		//组织查询条件
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//用户名称
		String userName = elecUser.getUserName();
		/**
		 * 方案一：如果页面中JS代码使用：userName = encodeURI(userName,"UTF-8");
		 *      服务器端：使用userName = new String(userName.getBytes("iso-8859-1"),"UTF-8");
		 */
//		try {
//			userName = new String(userName.getBytes("iso-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		/**
		 * 方案二：如果页面中JS代码使用：userName = encodeURI(userName,"UTF-8");
		 * 						userName = encodeURI(userName,"UTF-8");
		 *      服务器端：使用userName = URLDecoder.decode(userName, "UTF-8");
		 */
		try {
			userName = URLDecoder.decode(userName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(userName)){
			condition += " and o.userName like ?";
			paramsList.add("%"+userName+"%");
		}
		//所属单位
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//入职开始时间
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//入职结束时间
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object [] params = paramsList.toArray();
		//排序（按照入职时间的升序排列）
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		/**查询数据结果，list中存放的是所有的书籍*/
		List list = elecUserDao.findCollectionByConditionNoPageWithSelectCodition(condition, params, orderby,selectCondition);
		if(list!=null && list.size()>0){
			for(int i = 0;i<list.size();i++){
				Object[] arrays = null;
				if(selectCondition.contains(",")){
					arrays = (Object[])list.get(i);
				}else {
					arrays = new Object[1];
					arrays[0] = list.get(i);
				}
				//将Object数组转换成Arraylist<String> 用来存放每一行的数据
				ArrayList<String> data = new ArrayList<String>();
				if(arrays!=null && arrays.length>0){
					for(int j =0;j<arrays.length;j++){
						Object o = arrays[j];
						if(zList.get(j)!=null && keywordList.contains(zList.get(j))){
							data.add(o!=null?elecSystemDDLDao.findDdlNameByKeywordAndDdlCode(zList.get(j), o.toString()):"");
						}else {
							data.add(o!=null?o.toString():"");
						}
						
					}
				}
				fieldData.add(data);
			}
		}
		return fieldData;
	}

	/**  
	* @Name: saveUserList
	* @Description: 保存一个用户集合
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-24（创建日期）
	* @Parameters: ArrayList<String[]> 集合对象
	* @Return: 无
	*/
	@Transactional(readOnly=false)
	public void saveUserList(List<ElecUser> userList) {
		elecUserDao.saveList(userList);
	}

	/**  
	* @Name: chartUser
	* @Description: 统计用户分配情况
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-29（创建日期）
	* @Parameters: String zName 传递的数据类型
	* 				String eName 字段名称
	* @Return: List<Object[]>： 用户数据集合
	*/
	public List<Object[]> chartUser(String zName, String eName) {
		return elecUserDao.chartUser(zName,eName);
	}
	
	
}
