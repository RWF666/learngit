package com.weige.elec.action;

import java.awt.Menu;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.dao.impl.CommonDaoImpl;
import com.weige.elec.dao.impl.ElecUserDaoImpl;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecPopedom;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecText;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.form.MenuForm;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecRoleService;
import com.weige.elec.service.IElecTextService;
import com.weige.elec.service.IElecUserService;
import com.weige.elec.utils.LogonUtils;
import com.weige.elec.utils.MD5keyBean;
import com.weige.elec.utils.ValueUtils;

/*	@Controller("elecTextAction")
 * 	�൱��spring������
 * 	<bean id="elecTextAction" class="com.weige.elec.action.ElecTextAction" scope="prototype">
 */
@SuppressWarnings("serial")
@Controller("elecMenuAction")
@Scope(value="prototype")
public class ElecMenuAction extends BaseAction<MenuForm>{
	MenuForm menuForm = this.getModel();
	/**���м��service*/
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	
	/**�û�service*/
	@Resource(name=IElecUserService.SERVICE_NAME)
	IElecUserService elecUserService;
	
	/**Ȩ�޿���service*/
	@Resource(name=IElecRoleService.SERVICE_NAME)
	IElecRoleService elecRoleService;
	
	/**  
	* @Name: menuHome
	* @Description: ��ת��ϵͳ��¼����ҳ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-30 ���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��menu/home.jsp
	*/
	public String menuHome(){
		
		String name = menuForm.getName();
		String password = menuForm.getPassword();
		
		/**��֤��*/
		boolean flag = LogonUtils.checkNumber(request);
		System.out.println(flag);
		if(flag==false){
			this.addActionError("��֤�����");
			return "logonError";
		}
		
		ElecUser elecUser =elecUserService.findUserByLogonName(name);
		if(elecUser==null){
			this.addActionError("��¼���������");
			return "logonError";
		}else{
			if(password.trim().equals("")){
				this.addActionError("���벻��δ��");
				return "logonError";
			}else{
				if(!new MD5keyBean().getkeyBeanofStr(password).equals(elecUser.getLogonPwd())){
					this.addActionError("�����������");
					return "logonError";
				}
			}
			
		}
		/**�����ж��û��Ƿ�����˽�ɫ����������˽�ɫ������ɫ����Ϣ�������*/
		/**
		 * ��Ž�ɫ��Ϣ
		 * key����ɫID
		 * value����ɫ����
		 */
		Hashtable<String, String> ht = new Hashtable<String, String>();
		Set<ElecRole> elecRoles = elecUser.getElecRoles();
		//��ǰ�û�û�з����ɫ
		if(elecRoles==null || 0==elecRoles.size()){
			this.addActionError("��ǰ�û�û�з����ɫ���������Ա��ϵ��");
			return "logonError";//��ת����¼ҳ��
		}
		//��������˽�ɫ������ɫ����Ϣ�������
		else{
			for(ElecRole elecRole:elecRoles){
				//һ���û����Զ�Ӧ�����ɫ
				ht.put(elecRole.getRoleID(), elecRole.getRoleName());
			}
		}
		/**�����ж��û���Ӧ�Ľ�ɫ�Ƿ������Ȩ�ޣ����������Ȩ�ޣ���Ȩ�޵���Ϣ���������aa��*/
		//��Ȩ�޵���Ϣ���ŵ��ַ����У���ŵ�Ȩ�޵�mid���ַ����ĸ�ʽ��aa@ab@ac@ad@ae�� -----jquery��ztree��̬���ݼ���
		String popedom = elecRoleService.findPopedomByRoleIDs(ht);
		if(StringUtils.isBlank(popedom)){
			this.addActionError("��ǰ�û����еĽ�ɫû�з���Ȩ�ޣ��������Ա��ϵ��");
			return "logonError";//��ת����¼ҳ��
		}
		
		/**��֤��*/
		LogonUtils.remeberMe(name,password,request,response);
		
		//��ElecUser������õ�Session��
		request.getSession().setAttribute("globle_user", elecUser);
		//��Hashtable�д�ŵĽ�ɫ��Ϣ�����õ�Session��
		request.getSession().setAttribute("globle_role", ht);
		//��Ȩ�޵��ַ�������ʽ��aa@ab@ac@ad@ae����ŵ�Session��
		request.getSession().setAttribute("globle_popedom", popedom);
		return "menuHome";
	}
	/**����*/
	public String title(){
		return "title";
	}
	/**�˵�*/
	public String left(){
		return "left";
	}
	/**��ܴ�С�ı�*/
	public String change(){
		return "change";
	}
	/**  
	* @Name: loading
	* @Description: ��ת��������ʾҳ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-31���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��menu/loading.jsp
	*/
	public String loading(){
		//��ѯ�豸������������õ���������
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(elecCommonMsg);
		return "loading";
	}
	/**  
	* @Name: logout
	* @Description: ���µ�½
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-30 ���������ڣ�
	* @Parameters: ��
	* @Return: String���ض���index.jsp
	*/
	public String logout(){
		//�������Session
		request.getSession().invalidate();
		return "logout";
	}
	/**  
	* @Name: alermStation
	* @Description: ��ʾ��վ���������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-31 ���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��menu/alermStation.jsp
	*/
	/**վ���������*/
	public String alermStation(){
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(elecCommonMsg);
		return "alermStation";
	}
	/**  
	* @Name: alermDevice
	* @Description: ��ʾ�豸�������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-31���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��menu/alermDevice.jsp
	*/
	/**�豸�������*/
	public String alermDevice(){
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		ValueUtils.putValueStack(elecCommonMsg);
		return "alermDevice";
	}
	
	/**  
	* @Name: showMenuAction
	* @Description: ʹ��ajax��̬�����������Ͳ˵�
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-7���������ڣ�
	* @Parameters: ��
	* @Return: String��showMenu ʹ��strut2��json�����
	*/
	public String showMenu(){
		//��ȡ��ǰ�û��Ľ�ɫ
		Hashtable<String, String> ht = (Hashtable<String, String>) request.getSession().getAttribute("globle_role");
		//��ȡ��ǰ�û�
		ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
		
		//��Session��ȡ��ǰ�û����е�Ȩ�޵��ַ���
		String popedom = (String) request.getSession().getAttribute("globle_popedom");
		//��ѯ��ǰ�û��Ĺ���Ȩ��List<ElecPopedom>,ת����json
		List<ElecPopedom> list = elecRoleService.findPopedomListByString(popedom);
		
		//ʹ�ý�ɫ����ϵͳ��url
		if(!ht.containsKey("1")){
			for(ElecPopedom elecPopedom:list){
				String mid = elecPopedom.getMid();
				String pid = elecPopedom.getPid();
				//�ı��û������url
				if("an".equals(mid) && "am".equals(pid)){
					elecPopedom.setUrl("../system/elecUserAction_edit.do?userID="+elecUser.getUserID()+"&roleflag=1");
				}
			}
		}
		//��listת����json������ջ��
		ValueUtils.putValueStack(list);
		return "showMenu";
	}
}
