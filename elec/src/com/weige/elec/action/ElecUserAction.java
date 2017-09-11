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
	
	/**ע���û�Service*/
	@Resource(name=IElecUserService.SERVICE_NAME)
	IElecUserService elecUserService;
	
	/**ע�������ֵ�Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: �û��������ҳ��ʾ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/userIndex.jsp
	*/
	@AnnotationLimit(mid="an",pid="am")
	public String home(){
		System.out.println(request.getParameter("pageNO"));
		//1����������������������λ�������ֵ�ļ��ϣ�������ҳ��������˵���
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("������λ");
		request.setAttribute("jctList", jctList);
		//2����֯ҳ���д��ݵĲ�ѯ��������ѯ�û�������List<ElecUser>
		List<ElecUser> userList = elecUserService.findUserListByCondition(elecUser);
		request.setAttribute("userList", userList);
		
		/**2017-08-21 ��ӷ���  begin*/
		String initpage = request.getParameter("initpage");
		if(initpage!=null && initpage.equals("1")){
			return "list";
		}
		/**2017-08-21 ��ӷ���  end*/
		/*try {
			int a = 1/0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("����ʱ�쳣");
		}*/
		return "home";
	}
	
	/**  
	* @Name: add
	* @Description: ��ת���û������������ʾ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/userAdd.jsp
	*/
	@AnnotationLimit(mid="fb",pid="fa")
	public String add(){
		this.initSystemDDL();
		return "add";
	}
	
	/**  
	* @Name: home
	* @Description: ʹ��jquery��ajax��ɶ������������������󣬹�����������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ��
	* @Return: String��ʹ��struts2�Ĳ����
	*/
	@AnnotationLimit(mid="fd",pid="fa")
	public String findJctUnit(){
		// 1����ȡ������λ�µ��������ֵ
		String jctID = elecUser.getJctID();
		// 2��ʹ�ø�ֵ��Ϊ�������ͣ���ѯ��Ӧ�����ֵ��ֵ������List<ElecSystemDDL>
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(jctID);
		// 3����List<ElecSystemDDL>ת����json�����飬��List���Ϸ��õ�ջ��
		ValueUtils.putValueStack(list);
		return "findJctUnit";
	}
	
	/**  
	* @Name: checkUser
	* @Description: ʹ��jquery��ajax��ɵ�½���ĺ�̨��֤���ж����ݿ����Ƿ����,��֤���ݵ�Ψһ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ��
	* @Return: String��ʹ��struts2�Ĳ����
	*/
	@AnnotationLimit(mid="ff",pid="fa")
	public String checkUser(){
		// 1����ȡ������λ�µ��������ֵ
		String logonName = elecUser.getLogonName();
		// 2��ʹ�ø�ֵ��Ϊ�������ͣ���ѯ��Ӧ�����ֵ��ֵ������List<ElecSystemDDL>
		// 3����List<ElecSystemDDL>ת����json�����飬��List���Ϸ��õ�ջ��
		String message = elecUserService.checkUser(logonName);
		elecUser.setMessage(message);
		/*ValueUtils.putValueStack(message);*/
		return "checkUser";
	}
	
	/**  
	* @Name: save
	* @Description: �����û���Ϣ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-2���������ڣ�
	* @Parameters: ��
	* @Return: String ��ת��close.jsp
	*/
	@AnnotationLimit(mid="fc",pid="fa")
	public String save(){
		elecUserService.saveUser(elecUser);
		/**
		 * �����ж�
		 * roleflag==null��ϵͳ����Ա�����༭ҳ�棬��ʱ���棺close.jsp
		 * roleflag==1���������ϵͳ����Ա�����༭ҳ�棬��ʱ���棺�ض��򵽱༭ҳ��
	 */
		String roleflag = elecUser.getRoleflag();
		//�������ϵͳ����Ա�����༭ҳ�棬��ʱ���棺�ض��򵽱༭ҳ��
		if(roleflag!=null && roleflag.equals("1")){
			return "redirectEdit";
		}
		return "close";
	}
	
	/**  
	* @Name: edit
	* @Description: ��ת���༭����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-3���������ڣ�
	* @Parameters: ��
	* @Return: String ��ת��system/userEdit.jsp
	*/
	@AnnotationLimit(mid="fd",pid="fa")
	public String edit(){
		String userID = elecUser.getUserID();
		ElecUser user = elecUserService.findUserByUserID(userID);
		user.setViewflag(elecUser.getViewflag());
		user.setRoleflag(elecUser.getRoleflag());
		this.initSystemDDL();
		//4�����������ı�����
		//(1)��ȡ��������λ�ı��
		String ddlCode = user.getJctID();
		//(2)ʹ��������λ��������ı�ţ���ȡ�������ֵ
		String ddlName = elecSystemDDLService.findDdlNameByKeywordAndDdlCode("������λ",ddlCode);
		//(3)ʹ�ò�ѯ���������ֵ����Ϊ�������ͣ���ѯ���������͵Ķ�Ӧ�ļ��ϣ�����List<ElecSystemDDL>
		List<ElecSystemDDL> jctUnitList = elecSystemDDLService.findSystemDDLListByKeyword(ddlName);
		request.setAttribute("jctUnitList", jctUnitList);
		ValueUtils.putValueStack(user);
		
		return "edit";
	}
	
	/**1�����������ֵ䣬���������Ա�ְλ��������λ���Ƿ���ְ*/
	@AnnotationLimit(mid="fd",pid="fa")
	private void initSystemDDL() {
		List<ElecSystemDDL> sexList = elecSystemDDLService.findSystemDDLListByKeyword("�Ա�");
		request.setAttribute("sexList", sexList);
		List<ElecSystemDDL> postList = elecSystemDDLService.findSystemDDLListByKeyword("ְλ");
		request.setAttribute("postList", postList);
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("������λ");
		request.setAttribute("jctList", jctList);
		List<ElecSystemDDL> isDutyList = elecSystemDDLService.findSystemDDLListByKeyword("�Ƿ���ְ");
		request.setAttribute("isDutyList", isDutyList);
	}
	
	/**  
	* @throws Exception 
	* @Name: download
	* @Description: �ļ�����(��ͨ��ʽ)
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-4���������ڣ�
	* @Parameters: ��
	* @Return: ��
	*//*
	public String download() throws Exception{
		//��ȡ�ļ�ID
		String fileID = elecUser.getFileID();
		
		//1��ʹ���ļ�ID,��ѯ�û��ļ�����ȡ��·��path
		ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
		
		//·��path
		String path = ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
		//�ļ�����
		String filename =  elecUserFile.getFileName();
		//���Գ�������
		filename = new String(filename.getBytes("gbk"),"iso8859-1");
		//��д�����ļ���ͷ����ʽ
//		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition","attachment;filename="+filename);//inline������,ֱ�Ӵ�  attachment:����,������ʽ��
		
		//2��ʹ��·��path�����ҵ���Ӧ���ļ���װ����InputStream
		InputStream in = new FileInputStream(new File(path));
		
		//3������Ӧ����Response�л�ȡ�������outputStream
		OutputStream out = response.getOutputStream();
		
		//4��������������Ϣ��д�뵽�������
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
	* @Description: �ļ�����(struts2��ʽ)
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-4���������ڣ�
	* @Parameters: ��
	* @Return: ��
	*/
	@AnnotationLimit(mid="fd",pid="fa")
	public String download() throws Exception{
		//��ȡ�ļ�ID
		String fileID = elecUser.getFileID();
		
		//1��ʹ���ļ�ID,��ѯ�û��ļ�����ȡ��·��path
		ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
		
		//·��path
		String path = ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
		//�ļ�����
		String filename =  elecUserFile.getFileName();
		//���Գ�������
		filename = new String(filename.getBytes("gbk"),"iso8859-1");
		request.setAttribute("filename", filename);
		//2��ʹ��·��path�����ҵ���Ӧ���ļ���װ����InputStream
		InputStream in = new FileInputStream(new File(path));
		
		//��ջ����InputStream�������
		elecUser.setInputStream(in);
		
		return "download";
	}
	
	/**  
	* @Name: delete
	* @Description: ����ɾ���û���Ϣ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-4���������ڣ�
	* @Parameters: ��
	* @Return: String �ض���system/userIndex.jsp
	*/
	@AnnotationLimit(mid="fe",pid="fa")
	public String delete(){
		elecUserService.deleteUserByID(elecUser);
		/**���ִ��ɾ��,���������Զ��򵽸ղŵ�ҳ��*/
		request.setAttribute("pageNO", request.getParameter("pageNO"));
		return "delete";
	}
	
	/**  
	* @throws UnsupportedEncodingException 
	* @Name: exportExcel
	* @Description: ������ͨ����ѯ������������Ӧ���ݵ�excel����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-21���������ڣ�
	* @Parameters: ��
	* @Return: ��  ������struts2���е���
	*/
	/*@AnnotationLimit(mid="fg",pid="fa")
	public String exportExcel() throws UnsupportedEncodingException{
		//��ʼ������
		//excel�ı������ݣ�һ����
		ArrayList<String> fieldName = elecUserService.findFieldNameWithExcel();
		ArrayList<ArrayList<String>> fieldDate = elecUserService.findFieldDataWithExcel(elecUser);
		//���÷�װ��POI����ĵ�����,���excel����ĵ���
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDate);
		String filename = "�û�����"+DateUtils.dateToStringWithExcel(new Date())+"��.xls";
		filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		*//**response�н������ã��ܽ����أ���������Ҫio����ͷ*//*
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+filename);
		response.setBufferSize(1024);
		try {
			//��ȡ�����
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
	* @Description: ������ͨ����ѯ������������Ӧ���ݵ�excel����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-24���������ڣ�
	* @Parameters: ��
	* @Return: ��  ʹ��struts2���е���
	*/
	@AnnotationLimit(mid="fg",pid="fa")
	public String exportExcel() throws Exception{
		//��ʼ������
		//excel�ı������ݣ�һ����
		ArrayList<String> fieldName = elecUserService.findFieldNameWithExcel();
		ArrayList<ArrayList<String>> fieldDate = elecUserService.findFieldDataWithExcel(elecUser);
		//���÷�װ��POI����ĵ�����,���excel����ĵ���
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDate);
		String filename = "�û�����"+DateUtils.dateToStringWithExcel(new Date())+"��.xls";
		filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		request.setAttribute("filename", filename);
		//��ȡ�����
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		excelFileGenerator.expordExcel(os);
		
		byte[] buf = os.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		//���ļ�ת����������
		elecUser.setInputStream(in);
		return "exportExcel";
	}
	
	/**  
	* @Name: exportPage
	* @Description: ��ת���������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-24���������ڣ�
	* @Parameters: ��
	* @Return: 
	*/
	@AnnotationLimit(mid="fh",pid="fa")
	public String exportPage(){
		return "exportPage";
	}
	

	/**  
	* @throws Exception 
	 * @Name: exportPage
	* @Description: ��ת���������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-24���������ڣ�
	* @Parameters: ��
	* @Return: 
	*/
	@AnnotationLimit(mid="fh",pid="fa")
	public String importData() throws Exception{
		//��ȡ�ϴ����ļ�
		File fromFile = elecUser.getFile();
		//��ȡexcel���������ݣ����ڼ��ϵ���
		GenerateSqlFromExcel formExcel = new GenerateSqlFromExcel();
		ArrayList<String[]> arrayList = formExcel.generateUserSql(fromFile);
		/**
		 * List<String> errorList:������Ӵ�����Ϣ�ļ���
		 *   * ���û�д���errorList==null
		 *   * ������ڴ���errorList��ֵ
		 */
		List<String> errorList = new ArrayList<String>();
		//��ArrayList<String[]>ת����List<ElecUser>��ͬʱ���У��
		List<ElecUser> userList = this.fromExcelListToUserList(arrayList,errorList);
		//ִ�б���
		//������ڴ���errorList��ֵ
		if(errorList!=null && errorList.size()>0){
			request.setAttribute("errorList", errorList);
		}
		//���û�д���errorList==null
		else{//ִ�б���
			elecUserService.saveUserList(userList);
		}
		return "exportPage";
	}

	private List<ElecUser> fromExcelListToUserList(
			ArrayList<String[]> arrayList,List<String> errorList) {
		List<ElecUser> userList = new ArrayList<ElecUser>();
		if(arrayList!=null && arrayList.size()>0){
			for(int i = 0;i<arrayList.size();i++){
				//��ÿһ�е����ݷ�װ��po������
				String[] arrays = arrayList.get(i); 
				ElecUser elecUser = new ElecUser();
				//��ֵ�������ж�ȡ���������õ�PO������
				//��¼��
				if(StringUtils.isNotBlank(arrays[0])){
					//У���¼���������ݿ����Ƿ�����ظ�
					String message = elecUserService.checkUser(arrays[0]);
					//��¼���������ظ�����ʱ���Ա���
					if(message!=null && message.equals("3")){
						elecUser.setLogonName(arrays[0]);
					}
					else{
						errorList.add("��"+(i+2)+"�У���"+(0+1)+"�У���¼�������ݿ����Ѿ����ڣ�");
					}
				}
				else{
					errorList.add("��"+(i+2)+"�У���"+(0+1)+"�У���¼������Ϊ�գ�");
				}
				//����
				if(StringUtils.isNotBlank(arrays[1])){
					//md5���������
					MD5keyBean bean = new MD5keyBean();
					String logonPwd = bean.getkeyBeanofStr(arrays[1]);
					elecUser.setLogonPwd(logonPwd);
				}
				//�û�����
				if(StringUtils.isNotBlank(arrays[2])){
					elecUser.setUserName(arrays[2]);
				}
				//�Ա�
				if(StringUtils.isNotBlank(arrays[3])){
					//ʵ��һ�������ֵ��ת��,ʹ���������ͺ��������ֵ����ȡ������ı��
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("�Ա�",arrays[3]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setSexID(ddlCode);
					}
					else{
						errorList.add("��"+(i+2)+"�У���"+(3+1)+"�У��Ա��������ֵ�ת���в����ڣ�");
					}
				}
				else{
					errorList.add("��"+(i+2)+"�У���"+(3+1)+"�У��Ա���Ϊ�գ�");
				}
				//������λ
				if(StringUtils.isNotBlank(arrays[4])){
					//ʵ��һ�������ֵ��ת��,ʹ���������ͺ��������ֵ����ȡ������ı��
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("������λ",arrays[4]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setJctID(ddlCode);
					}
					else{
						errorList.add("��"+(i+2)+"�У���"+(4+1)+"�У�������λ�������ֵ�ת���в����ڣ�");
					}
				}
				else{
					errorList.add("��"+(i+2)+"�У���"+(4+1)+"�У�������λ����Ϊ�գ�");
				}
				//��ϵ��ַ
				if(StringUtils.isNotBlank(arrays[5])){
					elecUser.setAddress(arrays[5]);
				}
				//�Ƿ���ְ
				if(StringUtils.isNotBlank(arrays[6])){
					//ʵ��һ�������ֵ��ת��,ʹ���������ͺ��������ֵ����ȡ������ı��
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("�Ƿ���ְ",arrays[6]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setIsDuty(ddlCode);
					}
					else{
						errorList.add("��"+(i+2)+"�У���"+(6+1)+"�У��Ƿ���ְ�������ֵ�ת���в����ڣ�");
					}
				}
				else{
					errorList.add("��"+(i+2)+"�У���"+(6+1)+"�У��Ƿ���ְ����Ϊ�գ�");
				}
				//��������
				if(StringUtils.isNotBlank(arrays[7])){
					Date birthday = DateUtils.stringToDate(arrays[7]);
					elecUser.setBirthday(birthday);
				}
				//ְλ
				if(StringUtils.isNotBlank(arrays[8])){
					//ʵ��һ�������ֵ��ת��,ʹ���������ͺ��������ֵ����ȡ������ı��
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("ְλ",arrays[8]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setPostID(ddlCode);
					}
					else{
						errorList.add("��"+(i+2)+"�У���"+(8+1)+"�У�ְλ�������ֵ�ת���в����ڣ�");
					}
				}
				else{
					errorList.add("��"+(i+2)+"�У���"+(8+1)+"�У�ְλ����Ϊ�գ�");
				}
				userList.add(elecUser);
			}
		}
		return userList;
	}
	
	/**  
	* @throws �û� ͳ��
	 * @Name: exportPage
	* @Description: ��ת���û�ͳ��(����������λ)����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-29���������ڣ�
	* @Parameters: ��
	* @Return: string ��ת��system/userReport.jsp
	*/
	@AnnotationLimit(mid="fg",pid="fa")
	public String chartUser(){
		//��ѯ���ݿ⣬�����Ӧ�����ݼ���
		List<Object[]> list = elecUserService.chartUser("������λ","jctID");
		String filename = ChartUtils.createBarChart(list);
		request.setAttribute("filename", filename);
		//1��ʹ��jfreechart����ͼƬ�������ɵ�ͼƬ�ŵ�chart�ļ����£������ļ���
		return "chartUser";
	}
	
	/**  
	* @throws �û� ͳ��
	 * @Name: exportPage
	* @Description: ��ת���û�ͳ��(�����Ա�)����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-29���������ڣ�
	* @Parameters: ��
	* @Return: string ��ת��system/userReport.jsp
	*/
	@AnnotationLimit(mid="fg",pid="fa")
	public String chartSexFCF(){
		//��ѯ���ݿ⣬�����Ӧ�����ݼ���
		List<Object[]> list = elecUserService.chartUser("�Ա�","sexID");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
				/**b.keyword,b.ddlName,COUNT(b.ddlCode)*/
				Object[] objects = (Object[])list.get(i);
				if(i==0){//��֯��һ��ֵ
					String x = "��Ů����ͳ��";
					String y = "unit";//����FusionChart�е�һ�����⣬Y�����ʾ��֧�����ģ�����������Ӣ�Ĵ���
					builder.append("<graph caption='�û�ͳ�Ʊ���("+objects[0].toString()+")' xAxisName='"+x+"' bgColor='FFFFDD' yAxisName='"+y+"' showValues='1'  decimals='0' baseFontSize='18'  maxColWidth='60' showNames='1' decimalPrecision='0'> ");
					builder.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='AFD8F8'/>");
				}
			    
			    if(i==list.size()-1){//��֯���һ��ֵ
			    	builder.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='FF8E46'/>");
			    	builder.append("</graph>");
			    }
		} 
		request.setAttribute("chart", builder);//request�д��XML��ʽ������
		return "chartSexFCF";
	}
}
