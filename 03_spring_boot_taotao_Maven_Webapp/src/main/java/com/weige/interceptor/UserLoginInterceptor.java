package com.weige.interceptor;

import javax.persistence.criteria.From;
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

public class UserLoginInterceptor extends BaseService implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	/**
	 * 在Controller处理结束之后执行
	 */
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}
	
	/**
	 * 在Controller之后但是在试图渲染之前执行
	 */
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}
	
	/**
	 * 在Controller之前调用就会执行
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		String token = CookieUtils.getCookieValue(request, TAOTAO_TOKEN);
		
		
		if(StringUtils.isEmpty(token)){
			//未登录，跳转到登录界面
			response.sendRedirect("http://weige.taotao.com/rest/customer/user/toLogin");
			UserThreadLocal.set(null);
			return false;
		}
		if (userService == null) {//解决service为null无法注入问题 
	         BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()); 
	         userService = (UserService) factory.getBean("userService"); 
	    } 
		TbUser tbUser = userService.queryUserByToken(token);
		
		if(tbUser==null){
			//登录超时
			response.sendRedirect("http://weige.taotao.com/rest/customer/user/toLogin");
			UserThreadLocal.set(null);
			return false;
		}
		
		//已经登录
		UserThreadLocal.set(tbUser);
		return true;//true继续执行，false停止执行
	}

}
