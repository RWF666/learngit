package com.weige.elec.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ElecCommonMsgContent implements Serializable {
	
	private String comID;			//����ID
	private String type;			//�ж�վ����豸���еı�ʶ
	private String content;			//��������
	private Integer orderby;		//������ʾ����
	
	public String getComID() {
		return comID;
	}
	public void setComID(String comID) {
		this.comID = comID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}
}
