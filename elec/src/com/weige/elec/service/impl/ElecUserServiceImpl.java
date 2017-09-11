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

	/**�û���Dao*/
	@Resource(name=IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;
	
	/**�û�������Dao*/
	@Resource(name=IElecUserFileDao.SERVICE_NAME)
	IElecUserFileDao elecUserFileDao;
	
	/**�����ֵ��Dao*/
	@Resource(name=IElecSystemDDLDao.SERVICE_NAME)
	IElecSystemDDLDao elecSystemDDLDao;
	
	/**��������Dao*/
	@Resource(name=IElecExportFieldsDao.SERVICE_NAME)
	IElecExportFieldsDao elecExportFieldsDao;
	
	/**  
	* @Name: findUserListByCondition
	* @Description: ��֯��ѯ��������ѯ�û��б�
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ElecUser:VO����
	* @Return: List<ElecUser>���û�����
	*/
	public List<ElecUser> findUserListByCondition(ElecUser elecUser) {
		//��֯��ѯ����
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//�û�����
		String userName = elecUser.getUserName();
		if(StringUtils.isNotBlank(userName)){
			condition += " and o.userName like ?";
			paramsList.add("%"+userName+"%");
		}
		//������λ
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//��ְ��ʼʱ��
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//��ְ����ʱ��
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object [] params = paramsList.toArray();
		//���򣨰�����ְʱ����������У�
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		/**����һ�� ��ѯ�û�����ת�������ֵ��*/
		//List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage(condition, params, orderby);
		/**2017-08-21 ��ӷ���  begin*/
		PageInfo pageInfo = new PageInfo(ServletActionContext.getRequest());
		List<ElecUser> list = elecUserDao.findCollectionByConditionWithPage(condition, params, orderby,pageInfo);
		ServletActionContext.getRequest().setAttribute("page",pageInfo.getPageBean());
		/**2017-08-21 ��ӷ���  end*/
		/**
		 * 3�������ֵ��ת��
		 	* ʹ���������ͺ�������ı�ţ���ѯ�����ֵ䣬��ȡ�������ֵ
		 */
		this.convertSystemDDL(list);
		
		/**��������ֱ��ʹ��sql��� ���һ�����*/
		//List<ElecUser> list = elecUserDao.findCollectionByConditionNoPageWithSql(condition, params, orderby);
		
		
		
		
		return list;
	}

	/**  
	* @Name: convertSystemDDL
	* @Description: ʹ���������ͺ�������ı�ţ���ѯ�����ֵ䣬��ȡ�������ֵ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters:List<ElecUser> list:VO����
	* @Return: ��
	*/
	private void convertSystemDDL(List<ElecUser> list) {
		if(list!=null && list.size()>0){
			for(ElecUser user:list){
				//�Ա�
				String sexID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("�Ա�",user.getSexID());
				user.setSexID(sexID);
				//ְλ
				String postID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("ְλ",user.getPostID());
				user.setPostID(postID);
			}
		}
	}
	
	
	/**  
	* @Name: checkUser
	* @Description: ��֤��¼���Ƿ����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: String ��¼��
	* @Return: String ����message
	* �жϵ�¼���Ƿ�����ظ�������һ����ʶmessage����
			* message=1����ʾ��¼��Ϊ�գ������Ա���
			* message=2����ʾ��¼�������ݿ����Ѿ����ڣ������Ա���
			* message=3����ʾ��¼�������ݿ��в����ڣ����Ա���
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
	* @Description: �����û���Ϣ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ElecUser VO����
	* @Return: String ����message
	*/
	@Transactional(readOnly=false)
	public void saveUser(ElecUser elecUser) {
		//1�����������������֯����po��������ļ��ϴ��������û��ĸ�����Ϣ��������Ϣ����������������û���Ĺ�ϵ
		this.saveUserFile(elecUser);
		
		//����
		this.md5Password(elecUser);
		
		//��ȡҳ���userID
		String userID = elecUser.getUserID();
		//update
		if(StringUtils.isNotBlank(userID)){
			elecUserDao.update(elecUser);
		}
		//save
		else {
			//2����֯po���󣬱����û���1�����ݣ�
			elecUserDao.save(elecUser);
		}
		
		
	}
	
	private void md5Password(ElecUser elecUser) {
		String logonPwd = elecUser.getLogonPwd();
		
		//���û����д���룬���ó�ʼ����Ϊ123
		if(StringUtils.isBlank(logonPwd)){
			logonPwd = "123";
		}
		String md5LogonPwd ="";
		String password = elecUser.getPassword();
		//�༭��ʱ��û���޸������ʱ���ǲ���Ҫ���ܵ�
		if(password!=null && password.equals(logonPwd)){
			md5LogonPwd = logonPwd;
		}else {
			md5LogonPwd = new MD5keyBean().getkeyBeanofStr(logonPwd);
		}
		
		elecUser.setLogonPwd(md5LogonPwd);
	}

	@Transactional(readOnly=false)
	private void saveUserFile(ElecUser elecUser) {
		//�ϴ�ʱ��
		Date progressTime = new Date();
 		//��ȡ�ϴ����ļ�
		File[] uploads = elecUser.getUploads();
		//��ȡ�ϴ����ļ���
		String[] fileNames = elecUser.getUploadsFileName();
		//��ȡ�ϴ����ļ�����
		String[] contentType = elecUser.getUploadsContentType();
		//ʵ���ļ��ϴ�
		ServletContext servletContext = ServletActionContext.getServletContext();
		//����·����
		String directory = servletContext.getRealPath("/files");
		if(uploads!=null&&uploads.length>0){
			for(int i = 0;i<uploads.length;i++){
				//��֯������po����
				ElecUserFile elecUserFile = new ElecUserFile();
				elecUserFile.setFileName(fileNames[i]);//�ļ���
				elecUserFile.setProgressTime(progressTime);//�ϴ�ʱ��
				/**���ļ��ϴ���ͬʱ����·��path*/
				String fileURL = FileUtils.fileUploadReturnPath(uploads[i],fileNames[i],"�û�����");
				elecUserFile.setFileURL(fileURL);//�ϴ�·�������棬Ӧ�������أ�
				elecUserFile.setElecUser(elecUser);//���û�������ϵ
				elecUserFileDao.save(elecUserFile);
			}
		}
	}
	

	/**  
	* @Name: findUserByID
	* @Description: ʹ���û�ID����ѯ�û�����
	* @Author:	��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-4���������ڣ�
	* @Parameters: String���û�ID
	* @Return: ElecUser���û���Ϣ
	*/
	public ElecUser findUserByUserID(String userID) {
		return elecUserDao.findObjectByID(userID);
		
	}
	
	/**  
	* @Name: findUserFileByID
	* @Description: ʹ���û�����ID����ѯ�û���������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-4���������ڣ�
	* @Parameters: String���û�����ID
	* @Return: ElecUserFile���û�������Ϣ
	*/
	@Transactional(readOnly=false)
	public ElecUserFile findUserFileByID(String fileID) {
		return elecUserFileDao.findObjectByID(fileID);
	}

	/**  
	* @Name: deleteUserByID
	* @Description: ʹ���û�ID����ɾ���û�
	*  	 1��ɾ�����û���Ӧ���ļ�
		 2��ɾ�����û���Ӧ���û�����������
		 3��ɾ���û������Ϣ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-4���������ڣ�
	* @Parameters: String���û�����ID
	* @Return: ��
	*/
	@Transactional(readOnly=false)
	public void deleteUserByID(ElecUser elecUser) {
		// ��ȡ�û�ID
		String userID = elecUser.getUserID();
		String[] userIDs = userID.split(", ");
		
		if (userIDs != null && userIDs.length > 0) {
			for (String uid : userIDs) {
				// ʹ���û�ID����ѯ�û����󣬻�ȡ��ǰ�û����еĸ���
				ElecUser user = elecUserDao.findObjectByID(uid);
				Set<ElecUserFile> elecUserFiles = user.getElecUserFiles();
				if (elecUserFiles != null && elecUserFiles.size() > 0) {
					for (ElecUserFile elecUserFile : elecUserFiles) {
						// 1��ɾ�����û����ļ�
						// ��ȡ·��
						String path = ServletActionContext.getServletContext()
								.getRealPath("") + elecUserFile.getFileURL();
						File file = new File(path);
						if (file.exists()) {
							file.delete();
						}

						// 2��ɾ��ÿ���û��ĸ���
						// elecUserFileDao.deleteObjectByIds(elecUserFile.getFileID());û��Ҫ��ôɾ��ֱ��ɾ���û���ʱ����ɾ��
					}
				}
				
				/**2017-8-06��� ͬʱɾ�����û�����������*/
				Set<ElecRole> elecRoles = user.getElecRoles();
				if(elecRoles!=null && elecRoles.size()>0){
					for(ElecRole elecRole:elecRoles){
						elecRole.getElecUsers().clear();
					}
				}
			}
			// ɾ���û���Ϣ
			elecUserDao.deleteObjectByIds(userIDs);
		}
	}

	/**  
	* @Name: findUserByLogonName
	* @Description: ʹ�õ�¼����ѯ�û�
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-6���������ڣ�
	* @Parameters: String����¼��
	* @Return: ElecUser �û�
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
	* @Description: ��ȡexcel�ı����ֶΣ�ͨ���������ñ���̬��ʾ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-17���������ڣ�
	* @Parameters: ��
	* @Return: ArrayList<String> excel�ı���
	*/
	public ArrayList<String> findFieldNameWithExcel() {
		ElecExportFields elecExportFields = elecExportFieldsDao.findObjectByID("5-1");
		ArrayList<String> fieldName = (ArrayList<String>) ListUtils.stringToList(elecExportFields.getExpNameList(), "#");
		return fieldName;
	}

	/**  
	* @Name: findFieldDateWithExcel
	* @Description: ��ȡexcel�����ݣ�ͨ���������ñ���̬��ʾ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-17���������ڣ�
	* @Parameters: ��
	* @Return: ArrayList<String> excel������
	*/
	public ArrayList<ArrayList<String>> findFieldDataWithExcel(ElecUser elecUser) {
		//��֯����
		ArrayList<ArrayList<String>> fieldData = new ArrayList<ArrayList<String>>();
		//��֯ͶӰ��ѯ���������ӵ������ñ��е�Ӣ���ֶλ�ȡ
		ElecExportFields elecExportFields = elecExportFieldsDao.findObjectByID("5-1");
		String selectCondition = elecExportFields.getExpFieldName().replace("#", ",");
		List<String> zList = ListUtils.stringToList(elecExportFields.getExpNameList(), "#");
		List<ElecSystemDDL> elecSystemList= elecSystemDDLDao.findSystemDDLListByDistinct();
		List<String> keywordList = new ArrayList<String>();
		for(ElecSystemDDL elecSystemDDL:elecSystemList){
			keywordList.add(elecSystemDDL.getKeyword());
		}
		//��֯��ѯ����
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//�û�����
		String userName = elecUser.getUserName();
		/**
		 * ����һ�����ҳ����JS����ʹ�ã�userName = encodeURI(userName,"UTF-8");
		 *      �������ˣ�ʹ��userName = new String(userName.getBytes("iso-8859-1"),"UTF-8");
		 */
//		try {
//			userName = new String(userName.getBytes("iso-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		/**
		 * �����������ҳ����JS����ʹ�ã�userName = encodeURI(userName,"UTF-8");
		 * 						userName = encodeURI(userName,"UTF-8");
		 *      �������ˣ�ʹ��userName = URLDecoder.decode(userName, "UTF-8");
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
		//������λ
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//��ְ��ʼʱ��
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//��ְ����ʱ��
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object [] params = paramsList.toArray();
		//���򣨰�����ְʱ����������У�
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		/**��ѯ���ݽ����list�д�ŵ������е��鼮*/
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
				//��Object����ת����Arraylist<String> �������ÿһ�е�����
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
	* @Description: ����һ���û�����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-24���������ڣ�
	* @Parameters: ArrayList<String[]> ���϶���
	* @Return: ��
	*/
	@Transactional(readOnly=false)
	public void saveUserList(List<ElecUser> userList) {
		elecUserDao.saveList(userList);
	}

	/**  
	* @Name: chartUser
	* @Description: ͳ���û��������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-29���������ڣ�
	* @Parameters: String zName ���ݵ���������
	* 				String eName �ֶ�����
	* @Return: List<Object[]>�� �û����ݼ���
	*/
	public List<Object[]> chartUser(String zName, String eName) {
		return elecUserDao.chartUser(zName,eName);
	}
	
	
}
