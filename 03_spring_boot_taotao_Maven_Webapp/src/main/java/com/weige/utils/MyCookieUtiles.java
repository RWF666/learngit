package com.weige.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;

public class MyCookieUtiles {
	public static void addCookie(String name,String value,Integer time,boolean isEncoder,HttpServletResponse response){
		if (value == null) {
			value = "";
		} else if (isEncoder) {
			try {
				value = URLEncoder.encode(value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(time);
		cookie.setDomain("taotao.com");
		cookie.setPath("/rest");
		response.addCookie(cookie);
	}

	public static void deleteCookie(String name,String value,boolean isEncoder,HttpServletResponse response) {
		if (value == null) {
			value = "";
		} else if (isEncoder) {
			try {
				value = URLEncoder.encode(value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(-1);
		cookie.setDomain("taotao.com");
		cookie.setPath("/rest");
		response.addCookie(cookie);
	}
}
