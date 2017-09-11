package com.weige.elec.utils;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.service.IElecRoleService;

public class ErrorAndLimitInterceptor extends MethodFilterInterceptor {

	/**������*/
	@Override
	protected String doIntercept(ActionInvocation actioninvocation) throws Exception {
		//���Զ��������Ϣ ���õ�request��
		HttpServletRequest request = (HttpServletRequest) actioninvocation
						.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
		try {
			//��ȡ�����action����
			Object action = actioninvocation.getAction();
			//��ȡ����ķ���������
			String methodName = actioninvocation.getProxy().getMethod();
			//��ȡaction�еķ����ķ�װ��(action�еķ���û�в���)
			Method method = action.getClass().getMethod(methodName, null);
			// Action�ķ���ֵ   
			String result = null; 
			//�������תAction֮ǰ���ϸ����Ȩ�޿��ƣ�����Action��ÿ������
			//���ע�⣬�Ƿ���Բ���Ȩ�޵�URL
			boolean flag = isCheckLimit(request,method);
			if(flag){
				// ���б����ص�Action,�ڼ���������쳣�ᱻcatchס   
				result = actioninvocation.invoke();
			}
			else{
				request.setAttribute("errorMsg", "�Բ�����û��Ȩ�޲����˹��ܣ�");
				return "errorMsg";
			}
			return result;
		} catch (Exception e) {
			/**  
			 * �����쳣  
			 */
			String errorMsg = "���ִ�����Ϣ����鿴��־��";
			//ͨ��instanceof�жϵ�����ʲô�쳣����   
			if (e instanceof RuntimeException) {
				//δ֪������ʱ�쳣   
				RuntimeException re = (RuntimeException) e;
				//re.printStackTrace();
				errorMsg = re.getMessage().trim();
			}
			/**  
			 * ���ʹ�����Ϣ��ҳ��  
			 */
			request.setAttribute("errorMsg", errorMsg);

			/**  
			 * log4j��¼��־  
			 */
			Log log = LogFactory
					.getLog(actioninvocation.getAction().getClass());
			log.error(errorMsg, e);
			return "errorMsg";
		}// ...end of catch   
	}
	
	
	/**��֤ϸ����Ȩ�޿���*/
	public boolean isCheckLimit(HttpServletRequest request, Method method) {
		if(method == null){
			return false;
		}
		//��ȡ��ǰ�ĵ�½�û�
		ElecUser elecUser = (ElecUser)request.getSession().getAttribute("globle_user");
		if(elecUser == null){
			return false;
		}
		
		//��ȡ��ǰ��½�û��Ľ�ɫ��һ���û����Զ�Ӧ�����ɫ��
		Hashtable<String, String> ht = (Hashtable)request.getSession().getAttribute("globle_role");
		if(ht == null){
			return false;
		}
		//����ע�⣬�жϷ������Ƿ����ע�⣨ע�������Ϊ��AnnotationLimit��
		
		 /* ���磺
		 * 	@AnnotationLimit(mid="aa",pid="0")
	        public String home(){
	     */
		
		
		boolean isAnnotationPresent = method.isAnnotationPresent(AnnotationLimit.class);
		
		//������ע�⣨��ʱ���ܲ����÷�����
		if(!isAnnotationPresent){
			return false;
		}
		
		//����ע�⣨����ע�⣩
		AnnotationLimit limit = method.getAnnotation(AnnotationLimit.class);
		
		//��ȡע���ϵ�ֵ
		String mid = limit.mid();  //Ȩ����ģ������
		String pid = limit.pid();  //Ȩ�޸���������
		
		/**
		 * �����½�û��Ľ�ɫid+ע���ϵ�@AnnotationLimit(mid="aa",pid="0")
		 *   * ��elec_role_popedom���д���   flag=true����ʱ���Է���Action�ķ���;
		 *   * ��elec_role_popedom���в����� flag=false����ʱ���ܷ���Action�ķ���;
		 */
		boolean flag = false;
		//�������м���spring�������Ӷ���ȡService�࣬ʹ��Service���ѯ��Ӧ���û���Ϣ
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		IElecRoleService elecRoleService = (IElecRoleService)wac.getBean(IElecRoleService.SERVICE_NAME);
		//������ɫID
		if(ht!=null && ht.size()>0){
			for(Iterator<Entry<String, String>> ite = ht.entrySet().iterator();ite.hasNext();){
				Entry<String, String> entry = ite.next();
				//��ȡ��ɫID
				String roleID = entry.getKey();
				flag = elecRoleService.findRolePopedomByID(roleID, mid, pid);
				if(flag){
					break;
				}
			}
		}
		//�˷�������ȫ������˺ű����ã�����Աֻ�ܸı����ݿ��Ȩ�ޣ�ȱ���ܸı�session�����Ȩ�ޣ����в���ȫ
		/*String popedom = (String) request.getSession().getAttribute("globle_popedom");
		if(popedom.contains(mid)){
			flag = true;
		}*/
		return flag;
	}


}
