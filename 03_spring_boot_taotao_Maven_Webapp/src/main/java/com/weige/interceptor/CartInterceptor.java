package com.weige.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.weige.model.TbUser;
import com.weige.service.BaseService;
import com.weige.service.UserService;
import com.weige.threadlocal.UserThreadLocal;
import com.weige.utils.CookieUtils;

public class CartInterceptor extends BaseService implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	/**
	 * ��Controller�������֮��ִ��
	 */
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}
	
	/**
	 * ��Controller֮��������ͼ��Ⱦ֮ǰִ��
	 */
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}
	
	/**
	 * ��Controller֮ǰ���þͻ�ִ��
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		String token = CookieUtils.getCookieValue(request, TAOTAO_TOKEN);
		if (userService == null) {//���serviceΪnull�޷�ע������ 
	         BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()); 
	         userService = (UserService) factory.getBean("userService"); 
	    } 
		if(!StringUtils.isEmpty(token)){
			TbUser tbUser = userService.queryUserByToken(token);
			UserThreadLocal.set(tbUser);
		}
		
		return true;//true����ִ�У�falseִֹͣ��
	}

}
