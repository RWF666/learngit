package com.weige.elec.domain;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class ElecUser implements java.io.Serializable {
	private String userID;		//����ID
	private String jctID;		//������λcode
	private String jctUnitID;	//������λ�ĵ�λ���ƣ�������
	private String userName;	//�û�����
	private String logonName;	//��¼��
	private String logonPwd;	//����
	private String sexID;		//�Ա�
	private Date birthday;		//��������
	private String address;		//��ϵ��ַ
	private String contactTel;	//��ϵ�绰 
	private String email;		//��������
	private String mobile;		//�ֻ�
	private String isDuty;		//�Ƿ���ְ
	private String postID;      //ְλ
	private Date onDutyDate;	//��ְʱ��
	private Date offDutyDate;	//��ְʱ��
	private String remark;		//��ע
	
	//�ǳ־û�����
	private String message;     //���ݿ��Ƿ��Ѵ����û����ı�ʾ
	private File[] uploads;		//�ϴ����ļ�
	private String[] uploadsFileName;			//�ϴ����ļ���
	private String[] uploadsContentType;		//�ϴ����ļ�����
	private String fileID;  //�ļ�id
	private InputStream inputStream;   //�ļ�������������
	private String viewflag; // �鿴�û���viewflag=1,�༭�û�ʱviewflag=null
	private String password; //�����жϱ༭��ʱ�������Ƿ��޸�
	private String roleflag; //�����жϹ���Ա�Ĳ㼶�����ڱ༭�������ת
	
	
	
	public String getRoleflag() {
		return roleflag;
	}
	public void setRoleflag(String roleflag) {
		this.roleflag = roleflag;
	}

	/**
	 * ���һ����ʶ���ж�ҳ��ĸ�ѡ���Ƿ�ѡ�У��ñ�ʶҪ���õ�ElecUser������
   flag=1��ѡ��
   flag=2��û�б�ѡ��
	 */
	private String flag;

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getViewflag() {
		return viewflag;
	}
	public void setViewflag(String viewflag) {
		this.viewflag = viewflag;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getFileID() {
		return fileID;
	}
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	public File[] getUploads() {
		return uploads;
	}
	public void setUploads(File[] uploads) {
		this.uploads = uploads;
	}
	public String[] getUploadsFileName() {
		return uploadsFileName;
	}
	public void setUploadsFileName(String[] uploadsFileName) {
		this.uploadsFileName = uploadsFileName;
	}
	public String[] getUploadsContentType() {
		return uploadsContentType;
	}
	public void setUploadsContentType(String[] uploadsContentType) {
		this.uploadsContentType = uploadsContentType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	private Set<ElecUserFile> elecUserFiles = new HashSet<ElecUserFile>();

	public Set<ElecUserFile> getElecUserFiles() {
		return elecUserFiles;
	}
	public void setElecUserFiles(Set<ElecUserFile> elecUserFiles) {
		this.elecUserFiles = elecUserFiles;
	}

	private Set<ElecRole> elecRoles = new HashSet<ElecRole>();
	
	public Set<ElecRole> getElecRoles() {
		return elecRoles;
	}
	public void setElecRoles(Set<ElecRole> elecRoles) {
		this.elecRoles = elecRoles;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getJctID() {
		return jctID;
	}
	public void setJctID(String jctID) {
		this.jctID = jctID;
	}
	public String getJctUnitID() {
		return jctUnitID;
	}
	public void setJctUnitID(String jctUnitID) {
		this.jctUnitID = jctUnitID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLogonName() {
		return logonName;
	}
	public void setLogonName(String logonName) {
		this.logonName = logonName;
	}
	public String getLogonPwd() {
		return logonPwd;
	}
	public void setLogonPwd(String logonPwd) {
		this.logonPwd = logonPwd;
	}
	public String getSexID() {
		return sexID;
	}
	public void setSexID(String sexID) {
		this.sexID = sexID;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIsDuty() {
		return isDuty;
	}
	public void setIsDuty(String isDuty) {
		this.isDuty = isDuty;
	}
	public String getPostID() {
		return postID;
	}
	public void setPostID(String postID) {
		this.postID = postID;
	}
	public Date getOnDutyDate() {
		return onDutyDate;
	}
	public void setOnDutyDate(Date onDutyDate) {
		this.onDutyDate = onDutyDate;
	}
	public Date getOffDutyDate() {
		return offDutyDate;
	}
	public void setOffDutyDate(Date offDutyDate) {
		this.offDutyDate = offDutyDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**�ǳ־�javabean����*/
	//��ְ��ʼʱ��
	private Date onDutyDateBegin;
	//��ְ����ʱ��
	private Date onDutyDateEnd;
	//����ϴ���xsl�ļ�
	private File file;

	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Date getOnDutyDateBegin() {
		return onDutyDateBegin;
	}
	public void setOnDutyDateBegin(Date onDutyDateBegin) {
		this.onDutyDateBegin = onDutyDateBegin;
	}
	public Date getOnDutyDateEnd() {
		return onDutyDateEnd;
	}
	public void setOnDutyDateEnd(Date onDutyDateEnd) {
		this.onDutyDateEnd = onDutyDateEnd;
	}
}
