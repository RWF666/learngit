package com.weige.user.vo;

import lombok.Data;

/**
 * 返回对象
 * @author RWF
 *
 */
@Data
public class Response {
	private boolean success; //处理是否成功
	private String message; //处理后的消息提示
	private Object body; //返回的数据
	
	public Response(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public Response(boolean success, String message, Object body) {
		super();
		this.success = success;
		this.message = message;
		this.body = body;
	}
	
	
}
