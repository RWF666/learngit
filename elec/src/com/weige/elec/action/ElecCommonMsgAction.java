package com.weige.elec.action;

import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecText;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecTextService;
import com.weige.elec.utils.AnnotationLimit;
import com.weige.elec.utils.ValueUtils;

@SuppressWarnings("serial")
@Controller("elecCommonMsgAction")
@Scope(value="prototype")
public class ElecCommonMsgAction extends BaseAction<ElecCommonMsg>{
	ElecCommonMsg elecCommonMsg = this.getModel();
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	
	/**  
	* @Name: home
	* @Description: ���м�ص���ҳ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-31���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/actingIndex.jsp
	*/
	@AnnotationLimit(mid="ap",pid="am")
	public String home(){
		//1����ѯ���ݿ����м�ر�����ݽ��л���
		ElecCommonMsg elecCommonMsg = elecCommonMsgService.findCommonMsg();
		//2����elecCommonMsgѹ��ջ��,���б�����
		ValueUtils.putValueStack(elecCommonMsg);
		return "home";
	}
	
	/**  
	* @Name: save
	* @Description:�������м������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-31���������ڣ�
	* @Parameters: ��
	* @Return: String���ض���system/actingIndex.jsp
	*/
	@AnnotationLimit(mid="cb",pid="ca")
	public String save(){
		//ģ�Ᵽ��150�Σ��������ٷֱ�
		for(int i=1;i<=100;i++){
			elecCommonMsgService.saveCommonMsg(elecCommonMsg);
			request.getSession().setAttribute("percent", (double)i/100*100);//��ż���İٷֱ�
		}
		//�߳̽���ʱ����յ�ǰsession
		request.getSession().removeAttribute("percent");
		return "save";
	}
	/**  
	* @Name: actingView
	* @Description: ʹ��highsliderJS��ɲ�ѯ�豸�����������ϸ��Ϣ
	* @Author: �������ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-7-31���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��system/actingView.jsp
	*/
	@AnnotationLimit(mid="ap",pid="am")
	public String actingView(){
		//��ѯ���м�ص�����
		//1����ѯ���ݿ����м�ر�����ݣ�����ΩһElecCommonMsg
		ElecCommonMsg commonMsg = elecCommonMsgService.findCommonMsg();
		//2����ElecCommonMsg����ѹ��ջ����֧�ֱ�����
		ValueUtils.putValueStack(commonMsg);
		return "actingView";
	}
	
	/**  
	* @throws Exception 
	* @Name: progressBar
	* @Description: ��ҳ��ִ�б����ʹ��ajax������ִ�еİٷֱ�������������ʾ��ҳ����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-19���������ڣ�
	* @Parameters: ��
	* @Return: ajax�������Ҫ��תҳ�棬����null����NONE
	*/
	@AnnotationLimit(mid="ap",pid="am")
	public String progressBar() throws Exception{
		//��session�л�ȡ���������м���İٷֱ�
		Double percent = (Double) request.getSession().getAttribute("percent");
		String res = "";
		//��ʱ˵��������ҵ�񷽷���Ȼ������ִ��
		if(percent!=null){
			//�����С������������ȡ��
			int percentInt = (int) Math.rint(percent); 
			res = "<percent>" + percentInt + "</percent>";
		}
		//��ʱ˵��������ҵ�񷽷��Ѿ�ִ����ϣ�session�е�ֵ�Ѿ������
		else{
			//��Űٷֱ�
			res = "<percent>" + 100 + "</percent>";
		}
		//����ajax�ķ��ؽ����XML����ʽ
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		//��Ž�����ݣ����磺<response><percent>88</percent></response>
		out.println("<response>");
		out.println(res);
		out.println("</response>");
		out.close();
		return null;
	}
}
