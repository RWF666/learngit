package com.weige.elec.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.weige.elec.domain.ElecPopedom;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.service.IElecRoleService;
import com.weige.elec.utils.AnnotationLimit;



@SuppressWarnings("serial")
@Controller("elecRoleAction")
@Scope(value="prototype")
public class ElecRoleAction extends BaseAction<ElecPopedom> {
	
	ElecPopedom elecPopedom = this.getModel();
	
	/**ע���ɫ��Service*/
	@Resource(name=IElecRoleService.SERVICE_NAME)
	IElecRoleService elecRoleService;
	
	/**  
	* @Name: home
	* @Description: Ȩ�޹������ҳ��ʾ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-05���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/roleIndex.jsp
	*/
	@AnnotationLimit(mid="ga",pid="0")
	public String home(){
		//1����ѯϵͳ�����еĽ�ɫ
		List<ElecRole> roleList = elecRoleService.findAllRoleList();
		request.setAttribute("roleList", roleList);
		//2����ѯϵͳ�����е�Ȩ��
		//����List<ElecPopedom>,������е�tr��Ҳ����pid=0�ļ��ϣ�������
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomList();
		request.setAttribute("popedomList", popedomList);
		return "home";
	}
	
	/**  
	* @Name: edit
	* @Description: ��ת���༭ҳ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-05���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/roleEdit.jsp
	*/
	@AnnotationLimit(mid="ga",pid="0")
	public String edit(){
		//��ɫID
		String roleID = elecPopedom.getRoleID();
		//һ��ʹ�õ�ǰ��ɫID����ѯϵͳ�����е�Ȩ�ޣ�����ʾ��ƥ�䣩 
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomListByRoleID(roleID);
		request.setAttribute("popedomList", popedomList);
		//����ʹ�õ�ǰ��ɫID����ѯϵͳ�����е��û�������ʾ��ƥ�䣩
		List<ElecUser> userList = elecRoleService.findAllUserListByRoleID(roleID);
		request.setAttribute("userList", userList);
		return "edit";
	}
	
	/**  
	* @Name: save
	* @Description: �����ɫȨ�ޣ���ɫ���û��Ĺ�����ϵ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-06���������ڣ�
	* @Parameters: ��
	* @Return: String���ض���system/roleIndex.jsp
	*/
	@AnnotationLimit(mid="gc",pid="ga")
	public String save(){
		elecRoleService.saveRole(elecPopedom);
		return "save";
	}
	
}
