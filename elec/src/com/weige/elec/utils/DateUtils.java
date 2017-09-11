package com.weige.elec.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**将日期类型转换成String类型，file的格式*/
	public static String dateToStringByFile(Date date) {
		String sDate = new SimpleDateFormat("/yyyy/MM/dd/").format(date);
		return sDate;
	}

	/**将日期类型转换成String类型,yyyy-MM-dd HH:mm:ss的格式*/
	public static String dateToString(Date date) {
		String sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return sDate;
	}
	/**将日期类型转换成String类型，yyyyMMddHHmmss格式*/
	public static String dateToStringWithExcel(Date date) {
		String sDate = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		return sDate;
	}
	
	/**将string类型转换为date类型*/
	public static Date stringToDate(String sDate) {
		Date date = new Date();
		try {
			 date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
}
