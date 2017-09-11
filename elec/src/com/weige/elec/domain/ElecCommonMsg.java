package com.weige.elec.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ElecCommonMsg implements Serializable {
	
	private String comID;			//����ID
	private String stationRun;		//վ���������
	private String devRun;			//�豸�������
	private Date createDate;		//��������
	
	public String getComID() {
		return comID;
	}
	public void setComID(String comID) {
		this.comID = comID;
	}
	public String getStationRun() {
		return stationRun;
	}
	public void setStationRun(String stationRun) {
		this.stationRun = stationRun;
	}
	public String getDevRun() {
		return devRun;
	}
	public void setDevRun(String devRun) {
		this.devRun = devRun;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
	
}
