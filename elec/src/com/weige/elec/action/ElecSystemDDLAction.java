package com.weige.elec.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.service.IElecSystemDDLService;
import com.weige.elec.utils.AnnotationLimit;



@SuppressWarnings("serial")
@Controller("elecSystemDDLAction")
@Scope(value="prototype")
public class ElecSystemDDLAction extends BaseAction<ElecSystemDDL> {
	
	ElecSystemDDL elecSystemDDL = this.getModel();
	
	/**�����ֵ��Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: �����ֵ����ҳ��ʾ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-1���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/dictionaryIndex.jsp
	*/
	@AnnotationLimit(mid="aq",pid="am")
	public String home(){
		// 1����ѯ���ݿ������е��������ͣ�����List<ElecSystemDDL>���ϣ�������ҳ��������˵���
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByDistinct();
		request.setAttribute("list", list);
		return "home";
	}
	/**  
	* @Name: edit
	* @Description: ��ת�������ֵ�༭ҳ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-1���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/dictionaryEdit.jsp
	*/
	@AnnotationLimit(mid="eb",pid="ea")
	public String edit(){
		//��ȡ��������
		String keyword = elecSystemDDL.getKeyword();
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(keyword);
		request.setAttribute("list", list);
		return "edit";
	}
	/**  
	* @Name: save
	* @Description: ���������ֵ�
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-1���������ڣ�
	* @Parameters: ��
	* @Return: String���ض���system/dictionaryEdit.jsp
	*/
	@AnnotationLimit(mid="ec",pid="ea")
	public String save(){
		String keyword = elecSystemDDL.getKeyword();
		elecSystemDDLService.saveSystemDDL(elecSystemDDL);
		return "save";
	}
}
