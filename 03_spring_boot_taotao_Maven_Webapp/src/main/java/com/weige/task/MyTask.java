package com.weige.task;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;


//@Service
public class MyTask extends BaseTask{
	/**
	 * ΪʲôhelloService��Ϊnull
	 * 
	 * Quartz�е�job����Quartz��ܶ�̬������,������spring�����
	 * 
	 * ������job��ʹ��spring bean�ǲ����еġ�������Ҫ�Ƚ�job����spring����
	 * 
	 * ͨ�� AutowireCapableBeanFactory�Զ�ע��jobInstance
	 */
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(className+",date="+new Date().toLocaleString());
		
	}
	
	public String getCronExpression(){
		//�� �� Сʱ �� �� ���� 
		return "0/3 * * * * ?";
	}
}
