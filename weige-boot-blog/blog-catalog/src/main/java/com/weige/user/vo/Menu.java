package com.weige.user.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 菜单对象
 * @author RWF
 *
 */
@Data
public class Menu implements Serializable{
 
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String url;
	
	public Menu(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
	
}
