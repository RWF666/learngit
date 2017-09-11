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
		//��ȡ�������֤��
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
	
	/**��ס��*/
	public static void remeberMe(String name, String password,
			HttpServletRequest request, HttpServletResponse response) {
		//��������cookie���ֱ����˺ź�����
		//zע��cookie�в��ܴ������
		try {
			name = URLEncoder.encode(name,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie nameCookie = new Cookie("name", name);
		Cookie passwordCookie = new Cookie("password", password);
		String remeberMe = request.getParameter("remeberMe");
		//������Ч·��
		nameCookie.setPath(request.getContextPath()+"/");
		passwordCookie.setPath(request.getContextPath()+"/");
		if(remeberMe!=null && remeberMe.equals("yes")){
			//������Чʱ��
			nameCookie.setMaxAge(7*24*60*60);//һ��
			passwordCookie.setMaxAge(7*24*60*60);
			
		}else{
			nameCookie.setMaxAge(0);//���cookie
			passwordCookie.setMaxAge(0);
		}
		
		//��cookie��ŵ�response
		response.addCookie(nameCookie);
		response.addCookie(passwordCookie);
	}
	
}
