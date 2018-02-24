package com.weige.pojo;

import lombok.Data;

@Data
public class HttpResult {
	private Integer code;
	private String data;
	
	public HttpResult(Integer code, String data) {
		super();
		this.code = code;
		this.data = data;
	}
}
