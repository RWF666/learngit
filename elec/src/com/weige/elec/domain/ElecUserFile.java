package com.weige.elec.domain;

import java.util.Date;

@SuppressWarnings("serial")
public class ElecUserFile implements java.io.Serializable {
	private String fileID;		//����ID
	private String fileName;	//�ļ���
	private String fileURL;		//�ļ�·��
	private Date progressTime;	//�ϴ�ʱ��
	
	private ElecUser elecUser;
	
	public ElecUser getElecUser() {
		return elecUser;
	}

	public void setElecUser(ElecUser elecUser) {
		this.elecUser = elecUser;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public Date getProgressTime() {
		return progressTime;
	}

	public void setProgressTime(Date progressTime) {
		this.progressTime = progressTime;
	}
}
