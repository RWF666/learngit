package com.weige.threadlocal;

import com.weige.model.TbUser;

public class UserThreadLocal {
	private static final ThreadLocal<TbUser> LOCAL = new  ThreadLocal<TbUser>();
	
	public static void set(TbUser tbUser){
		LOCAL.set(tbUser);
	}
	
	public static TbUser get(){
		return LOCAL.get();
	}
}
