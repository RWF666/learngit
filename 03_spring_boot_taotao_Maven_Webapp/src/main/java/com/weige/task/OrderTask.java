package com.weige.task;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weige.mapper.TbOrderMapper;
import com.weige.model.TbOrder;

@Service
public class OrderTask extends BaseTask{
	@Autowired
	private TbOrderMapper tbOrderMapper;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		tbOrderMapper.paymentOrderScan(new DateTime().minusDays(1).toDate());
	}
	
	public String getCronExpression(){
		return "0 0/1 * * * ?";
	}
}
