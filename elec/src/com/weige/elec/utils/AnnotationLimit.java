package com.weige.elec.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * �Զ���ע��
 */
//�����ע�����ε�ע�⣬���÷��䣬��������ע���ȡ����
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationLimit {
	String mid();//Ȩ�޵�code
	String pid();//����Ȩ�޵�code
}
