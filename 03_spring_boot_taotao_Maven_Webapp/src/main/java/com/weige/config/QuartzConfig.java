package com.weige.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * Quartz配置类
 * @author RWF
 *
 */
@Configuration
public class QuartzConfig {
	@Autowired
	private SpringBeanJobFactory springBeanJobFactory;
	
	@Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
	//把Job交给Spring来管理，这样Job就能使用由Spring产生的Bean了
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setJobFactory(springBeanJobFactory);
    return schedulerFactoryBean;
  }

  @Bean
  public Scheduler scheduler() {
    return schedulerFactoryBean().getScheduler();
  }
}
