package com.weige.elec.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.org.eclipse.jdt.core.dom.ThisExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.domain.ElecExportFields;
import com.weige.elec.service.IElecExportFieldsService;
import com.weige.elec.utils.AnnotationLimit;
import com.weige.elec.utils.ListUtils;
import com.weige.elec.utils.ValueUtils;



@SuppressWarnings("serial")
@Controller("elecExportFieldsAction")
@Scope(value="prototype")
public class ElecExportFieldsAction extends BaseAction<ElecExportFields>{
	ElecExportFields elecExportFields = this.getModel();
	
	/**ע�뵼������Service*/
	@Resource(name=IElecExportFieldsService.SERVICE_NAME)
	IElecExportFieldsService elecExportFieldsService;
	
	/**  
	* @Name: setExportField
	* @Description: ��ת���������ý���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-17���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/exportExcel.jsp
	*/
	@AnnotationLimit(mid="db",pid="da")
	public String setExportField(){
		String belongTo = (String) request.getAttribute("belongTo");
		ElecExportFields elecExportFields = elecExportFieldsService.findExportFieldsByID(belongTo);
		Map<String,String> expFields = new LinkedHashMap<String, String>();
		Map<String,String> noExpFields = new LinkedHashMap<String, String>();
		List<String> expFieldNames = ListUtils.stringToList(elecExportFields.getExpFieldName(),"#");
		List<String> expNameLists = ListUtils.stringToList(elecExportFields.getExpNameList(),"#");
		List<String> noExpFieldNames = ListUtils.stringToList(elecExportFields.getNoExpFieldName(),"#");
		List<String> noExpNameLists = ListUtils.stringToList(elecExportFields.getNoExpNameList(),"#");
		if(expFieldNames!=null && expFieldNames.size()>0 && expNameLists!=null && expNameLists.size()>0){
			for(int i = 0;i<expFieldNames.size();i++){
				expFields.put(expFieldNames.get(i), expNameLists.get(i));
			}
		}
		if(noExpFieldNames!=null && noExpFieldNames.size()>0 && noExpNameLists!=null && noExpNameLists.size()>0){
			for(int i = 0;i<noExpFieldNames.size();i++){
				noExpFields.put(noExpFieldNames.get(i), noExpNameLists.get(i));
			}
		}
		request.setAttribute("expFields", expFields);
		request.setAttribute("noExpFields", noExpFields);
		return "setExportField";
	}
	
	/**  
	* @Name: saveSetExportExcel
	* @Description: ���浼������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-17���������ڣ�
	* @Parameters: ��
	* @Return: String���ر����ý��棬ˢ�¸�����
	*/
	@AnnotationLimit(mid="dc",pid="da")
	public String saveSetExportExcel(){
		elecExportFieldsService.saveSetExportExcel(elecExportFields);
		return "close";
	}
	
	
}
