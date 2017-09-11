package com.weige.elec.action;


import java.lang.reflect.ParameterizedType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.domain.ElecText;
import com.weige.elec.utils.TUtil;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T>,ServletRequestAware,ServletResponseAware{
	
	T entity;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	//T型实例化
	public BaseAction(){
		/**T型装换*/
		Class entityClass = TUtil.getActualType(this.getClass());
		try {
			entity = (T) entityClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return entity;
	}
	@Override
	public void setServletResponse(HttpServletResponse req) {
		this.response = (HttpServletResponse) req;
	}
	@Override
	public void setServletRequest(HttpServletRequest res) {
		this.request = (HttpServletRequest) res;
	}

}
