package com.weige.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;

public class TbOrder {
	private String orderId;//id,rowKye:id+ʱ���
	@NotBlank
	private String payment;//ʵ�����
	private Integer paymentType; //֧�����ͣ�1������֧����2����������
	private String postFee;//�ʷ�
	/**
	 * ��ʼ�׶Σ�1��δ���δ��������ʼ����������
	 * ����׶Σ�2���Ѹ�����ĸ���ʱ��
	 * �����׶Σ�3���ѷ��������ķ���ʱ�䡢����ʱ�䡢�������ơ���������
	 * �ɹ��׶Σ�4���ѳɹ������ĸ���ʱ�䣬���׽���ʱ�䣬������ԣ��Ƿ�������
	 * �رս׶Σ�5���رգ�   ���ĸ���ʱ�䣬���׹ر�ʱ�䡣
	 * */
	private Integer status;//״̬:1��δ���2���Ѹ��3��δ������4���ѷ�����5�����׳ɹ���6�����׹ر�
	@JsonDeserialize(using = DateDeserializer.class)
	private Date createTime;//����ʱ��
	@JsonDeserialize(using = DateDeserializer.class)
	private Date updateTime;//����ʱ��
	@JsonDeserialize(using = DateDeserializer.class)
	private Date paymentTime;//����ʱ��
	@JsonDeserialize(using = DateDeserializer.class)
	private Date consignTime;//����ʱ��
	@JsonDeserialize(using = DateDeserializer.class)
	private Date endTime;//���׽���ʱ��
	@JsonDeserialize(using = DateDeserializer.class)
	private Date closeTime;//���׹ر�ʱ��
	private String shippingName;//��������
	private String shippingCode;//��������
	@Min(value = 1L)
	private Long userId;//�û�id
	private String buyerMessage;//�������
	@NotBlank
	private String buyerNick;//����ǳ�
	private Integer buyerRate;//����Ƿ��Ѿ�����
	@NotEmpty
	private List<TbOrderItem> orderItems;//��Ʒ����
	
	private TbOrderShipping orderShipping; //������ַ��Ϣ
	
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	public String getBuyerMessage() {
		return buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public Integer getBuyerRate() {
		return buyerRate;
	}
	public void setBuyerRate(Integer buyerRate) {
		this.buyerRate = buyerRate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getPostFee() {
		return postFee;
	}
	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	public Date getConsignTime() {
		return consignTime;
	}
	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}