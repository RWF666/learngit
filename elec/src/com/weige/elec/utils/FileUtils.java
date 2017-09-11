package com.weige.elec.utils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;

public class FileUtils {
	/**
	 * ����ļ��ϴ�������·��path
	 * @param file �ϴ����ļ�
	 * @param fileName �ϴ����ļ���
	 * @param model  ģ������
	 * @return
	 *  1������ļ��ϴ���Ҫ��
		  1�����ϴ����ļ�ͳһ���õ�upload���ļ�����
		  2����ÿ���ϴ����ļ���ʹ�����ڸ�ʽ���ļ��зֿ�����ÿ��ҵ���ģ�����ͳһ�ļ�����
		  3���ϴ����ļ���Ҫָ��Ψһ������ʹ��UUID�ķ�ʽ��Ҳ����ʹ��������Ϊ�ļ���
		  4����װһ���ļ��ϴ��ķ������÷�������֧�ֶ��ļ����ϴ�����֧�ָ��ָ�ʽ�ļ����ϴ�
		  5������·��path��ʱ��ʹ�����·�����б��棬����������Ŀ�Ŀ���ֲ��
	 */
	public static String fileUploadReturnPath(File file, String fileName, String model ){
		//1:��ȡ��Ҫ�ϴ��ļ�ͳһ��·��path����upload��
		String basepath = ServletActionContext.getServletContext().getRealPath("/upload");
		//2����ȡ�����ļ��У���ʽ/yyyy/MM/dd/��
		String datepath = DateUtils.dateToStringByFile(new Date());
		//��ʽ��upload\2014\12\01\�û�����
		String filePath = basepath+datepath+model;
		//3���жϸ��ļ����Ƿ���ڣ���������ڣ�����
		File dateFile = new File(filePath);
		if(!dateFile.exists()){
			dateFile.mkdirs();//����
		}
		//4��ָ����Ӧ���ļ���
		//�ļ��ĺ�׺
		String prifix = fileName.substring(fileName.lastIndexOf("."));
		//uuid���ļ���
		String uuidFileName = UUID.randomUUID().toString()+prifix;
		//�����ϴ����ļ���Ŀ���ļ���
		File destFile = new File(dateFile,uuidFileName);
		//�ϴ��ļ�
		file.renameTo(destFile);//�൱�ڼ���
		return "/upload"+datepath+model+"/"+uuidFileName;
	}
}
