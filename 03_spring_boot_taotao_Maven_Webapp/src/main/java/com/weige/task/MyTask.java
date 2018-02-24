package com.weige.task;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;


//@Service
public class MyTask extends BaseTask{
	/**
	 * 为什么helloService会为null
	 * 
	 * Quartz中的job是由Quartz框架动态创建的,并不是spring管理的
	 * 
	 * 所以在job中使用spring bean是不可行的。我们需要先将job交给spring管理
	 * 
	 * 通过 AutowireCapableBeanFactory自动注入jobInstance
	 */
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(className+",date="+new Date().toLocaleString());
		
	}
	
	public String getCronExpression(){
		//秒 分 小时 日 月 星期 
		return "0/3 * * * * ?";
	}
}
