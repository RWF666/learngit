package com.weige.pojo;

public class ResultMsg {
	private String code;//200³É¹¦ 400Ê§°Ü
	private String msg;
	
	public ResultMsg(){
		
	}
	
	public ResultMsg(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
