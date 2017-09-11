package com.weige.elec.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**����������ת����String���ͣ�file�ĸ�ʽ*/
	public static String dateToStringByFile(Date date) {
		String sDate = new SimpleDateFormat("/yyyy/MM/dd/").format(date);
		return sDate;
	}

	/**����������ת����String����,yyyy-MM-dd HH:mm:ss�ĸ�ʽ*/
	public static String dateToString(Date date) {
		String sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return sDate;
	}
	/**����������ת����String���ͣ�yyyyMMddHHmmss��ʽ*/
	public static String dateToStringWithExcel(Date date) {
		String sDate = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		return sDate;
	}
	
	/**��string����ת��Ϊdate����*/
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
