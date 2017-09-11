package com.weige.elec.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.org.eclipse.jdt.core.IField;

public class LogonUtils {

	public static boolean checkNumber(HttpServletRequest request) {
		//获取输入的验证码
		String checkNumber = request.getParameter("checkNumber");
		if(StringUtils.isBlank(checkNumber)){
			return false;
		}
		String CHECK_NUMBER_KEY = (String) request.getSession().getAttribute("CHECK_NUMBER_KEY");
		if(StringUtils.isBlank(CHECK_NUMBER_KEY)){
			return false;
		}
		return checkNumber.equals(CHECK_NUMBER_KEY);
	}
	
	/**记住我*/
	public static void remeberMe(String name, String password,
			HttpServletRequest request, HttpServletResponse response) {
		//创建两个cookie，分别存放账号和密码
		//z注意cookie中不能存放中文
		try {
			name = URLEncoder.encode(name,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie nameCookie = new Cookie("name", name);
		Cookie passwordCookie = new Cookie("password", password);
		String remeberMe = request.getParameter("remeberMe");
		//设置有效路径
		nameCookie.setPath(request.getContextPath()+"/");
		passwordCookie.setPath(request.getContextPath()+"/");
		if(remeberMe!=null && remeberMe.equals("yes")){
			//设置有效时间
			nameCookie.setMaxAge(7*24*60*60);//一周
			passwordCookie.setMaxAge(7*24*60*60);
			
		}else{
			nameCookie.setMaxAge(0);//清空cookie
			passwordCookie.setMaxAge(0);
		}
		
		//将cookie存放到response
		response.addCookie(nameCookie);
		response.addCookie(passwordCookie);
	}
	
}
