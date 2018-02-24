package com.weige.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * ��quartz����spring����
 * @author RWF
 *
 */
@Component
public class SpringBeanJobFactory extends AdaptableJobFactory {
	
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;
	
	 /**
     * �������Ǹ�����super��createJobInstance���������䴴�����������ٽ���autowire��
     */
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
	    capableBeanFactory.autowireBean(jobInstance);
	    return jobInstance;
	}
	
}
