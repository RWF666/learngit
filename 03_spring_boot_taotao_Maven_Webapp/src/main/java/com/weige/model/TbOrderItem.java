package com.weige.model;


public class TbOrderItem {
	private Long itemId;//��Ʒid
	private String orderId;//����id
	private Integer num;//��Ʒ��������
	private String title;//��Ʒ����
	private Long price;//��Ʒ����
	private Long totalFee;//��Ʒ�ܼ�
	private String picPath;//ͼƬ·��
	
	public Long getItemId() {
		return itemId;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}
	public Integer getNum() {
		return num;
	}
}