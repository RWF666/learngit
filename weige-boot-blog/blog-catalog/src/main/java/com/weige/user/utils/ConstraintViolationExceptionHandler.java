package com.weige.user.utils;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;



/**
 * ConstrainViolationExceptionHandler 处理器
 * @author RWF
 *
 */
public class ConstraintViolationExceptionHandler{
	
	public static String getMessage(ConstraintViolationException e) {
		List<String> MsgList = new ArrayList<String>();
		
		for(ConstraintViolation<?> constraintViolation:e.getConstraintViolations()) {
			MsgList.add(constraintViolation.getMessage());
		}
		return StringUtils.join(MsgList.toArray(),";");
	}
}
