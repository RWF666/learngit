package com.weige.elec.domain;


@SuppressWarnings("serial")
public class ElecExportFields implements java.io.Serializable {
	
	private String belongTo;		//����ģ�飨��Ȼ�����������û�����Ϊ��5-1
	private String expNameList;		//�����ֶε�������
	private String expFieldName;	//�����ֶε�Ӣ����
	private String noExpNameList;	//δ�����ֶε�������
	private String noExpFieldName;	//δ�����ֶε�Ӣ����
	
	public String getBelongTo() {
		return belongTo;
	}
	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}
	public String getExpNameList() {
		return expNameList;
	}
	public void setExpNameList(String expNameList) {
		this.expNameList = expNameList;
	}
	public String getExpFieldName() {
		return expFieldName;
	}
	public void setExpFieldName(String expFieldName) {
		this.expFieldName = expFieldName;
	}
	public String getNoExpNameList() {
		return noExpNameList;
	}
	public void setNoExpNameList(String noExpNameList) {
		this.noExpNameList = noExpNameList;
	}
	public String getNoExpFieldName() {
		return noExpFieldName;
	}
	public void setNoExpFieldName(String noExpFieldName) {
		this.noExpFieldName = noExpFieldName;
	}
	
	
	
	
}
