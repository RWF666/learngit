package com.weige.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

public class TbOrderShipping {
	private String orderId; // ����ID
	@Length(max = 20)
	private String receiverName; // �ջ���ȫ��
	@Length(max = 20)
	private String receiverPhone; // �̶��绰
	@Length(max = 30)
	private String receiverMobile; // �ƶ��绰
	@Length(max = 10)
	private String receiverState; // ʡ��
	@Length(max = 10)
	private String receiverCity; // ����
	@Length(max = 20)
	private String receiverDistrict; // ��/��
	@Length(max = 200)
	private String receiverAddress; // �ջ���ַ���磺xx·xx��
	@Length(max = 6)
	private String receiverZip; // ��������,�磺310001
	
	private Date created;
	private Date updated;
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverState() {
		return receiverState;
	}
	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState;
	}
	public String getReceiverCity() {
		return receiverCity;
	}
	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}
	public String getReceiverDistrict() {
		return receiverDistrict;
	}
	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverZip() {
		return receiverZip;
	}
	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}
}