package com.weige.task;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


public class BaseTask implements Job{
	@Autowired
	private Scheduler scheduler;
	
	String className = this.getClass().getSimpleName();
	@PostConstruct //等同于 init-method
	public void init(){
		//具体任务.
		JobDetail jobDetail = JobBuilder.newJob(this.getClass()).withIdentity(className+"job1",className+"group1").build();
		
		//触发时间点. (每5秒执行1次.)
//		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
//		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startNow().withSchedule(simpleScheduleBuilder).build();
		String cronExpression = getCronExpression();//我们希望的是子类进行提供
		Trigger trigger =TriggerBuilder.newTrigger().
				withIdentity(className+"trigger",className+"group").
				withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
		// 交由Scheduler安排触发
		try {
			scheduler.scheduleJob(jobDetail,trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//删除定时任务
		/*
		try {
			JobKey jobKey = new JobKey("job1");
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	public String getCronExpression() {
		return null;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
}
