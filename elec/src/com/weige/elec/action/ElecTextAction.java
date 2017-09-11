package com.weige.elec.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.domain.ElecText;
import com.weige.elec.service.IElecTextService;

/*	@Controller("elecTextAction")
 * 	�൱��spring������
 * 	<bean id="elecTextAction" class="com.weige.elec.action.ElecTextAction" scope="prototype">
 */
@SuppressWarnings("serial")
@Controller("elecTextAction")
@Scope(value="prototype")
public class ElecTextAction extends BaseAction<ElecText>{
	ElecText elecText = this.getModel();
	@Resource(name=IElecTextService.SERVICE_NAME)
	IElecTextService elecTextService;
	/**ִ�б���*/
	public String save(){
		elecTextService.saveElecText(elecText);
		return "save";
	}
}
