package com.weige.elec.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	/**
	 * @param wholecontent:���ݵ��ı��ַ���
	 * @param cutcount����Ҫ�ָ���ַ����ĳ���
	 * @return���ָ���List���ϣ���Ž����
	 */
	public static List<String> getContentByList(String wholecontent,int cutcount){
		List<String> list = new ArrayList<String>();
		//��ȡ���������ַ������ܳ���
    	int contentlen = wholecontent.length(); 
        //���ݽ�ȡ���������ܳ��ͽ�ȡ���Ƚ��бȽϣ������ȡ�Ļ�ֱ�Ӳ���
	    if (contentlen < cutcount){ 
	    	list.add(wholecontent);
	    }
	    //���ݳ��ȳ�����ȡ����
	    else{
	    	//���岢��ʼ�����ݶ���
	    	String contentpart ="";
	    	//���岢��ʼ������ȡ�Ķ�������
	    	int contentround =0;
            //��ʼ��ȡ��λ��
	    	int begincount = 0; 
            //�жϽ�ȡ�Ķ�����
	 	    int contentcutpart = contentlen/cutcount; 
		    int contentcutparts = contentlen%cutcount; //������
		    //������Ϊ0��˵�������������ݵĳ��������ǽ�ȡ���ȵı�����
		    if (contentcutparts==0){
		    	contentround = contentcutpart;
		    }
		    else{
		    	contentround = contentcutpart+1;
		    }
		    //ѭ����ȡ����
	    	for (int i = 1; i <= contentround; i++) {
	    		//����������һ����ȡ����
	            if (i != contentround){
	            	//���սضϳ��Ƚ�ȡ����
	            	contentpart = wholecontent.substring(begincount, cutcount*i);
	            }
	            else{
	            	//��ȡ���һ��������
	            	contentpart = wholecontent.substring(begincount, contentlen);
	            }
	            //��ֵ��һ��ȡ���ֵ����λ��
	 		    begincount = cutcount*i; 
	 		    list.add(contentpart);
	    	}
	    }
	    return list;
	}

}
