package com.weige.elec.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileUpload;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.domain.ElecFileUpload;
import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.service.IElecFileUploadService;
import com.weige.elec.service.IElecSystemDDLService;
import com.weige.elec.service.impl.ElecFileUploadServiceImpl;
import com.weige.elec.utils.AnnotationLimit;
import com.weige.elec.utils.LuceneUtils;
@SuppressWarnings("serial")
@Controller("elecFileUploadAction")
@Scope(value="prototype")
public class ElecFileUploadAction extends BaseAction<ElecFileUpload> {
	
	ElecFileUpload elecFileUpload = this.getModel();
	
	/**ע������ͼֽ����Service*/
	@Resource(name=IElecFileUploadService.SERVICE_NAME)
	IElecFileUploadService elecFileUploadService;
	
	/**ע�������ֵ��Service*/
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	IElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: ����ͼֽ�������ҳ��ʾ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-17���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��dataChart/dataChartIndex.jsp
	*/
	@AnnotationLimit(mid="af",pid="ae")
	public String home(){
		//1������ҳ������ʾ�������˵���������λ��ͼֽ�б���б�
		this.initSystemDDL();
		return "home";
	}
	
	/**����ҳ������ʾ�������˵���������λ��ͼֽ�б���б�*/
	private void initSystemDDL() {
		//������λ
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("������λ");
		request.setAttribute("jctList", jctList);
		//ͼֽ���
		List<ElecSystemDDL> picList = elecSystemDDLService.findSystemDDLListByKeyword("ͼֽ���");
		request.setAttribute("picList", picList);
	}
	
	/**  
	* @Name: add
	* @Description: ��ת����������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-17���������ڣ�
	* @Parameters: ��
	* @Return: String����ת��dataChart/dataChartAdd.jsp
	*/
	@AnnotationLimit(mid="ig",pid="ia")
	public String add(){
		//1������ҳ������ʾ�������˵���������λ��ͼֽ�б���б�
		this.initSystemDDL();
		return "add";
	}
	
	/**  
	* @throws InterruptedException 
	 * @Name: save
	* @Description: ��������ͼֽ����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-18���������ڣ�
	* @Parameters: ��
	* @Return: String���ض���dataChart/dataChartAdd.jsp
	*/
	@AnnotationLimit(mid="ib",pid="ia")
	public String save() throws InterruptedException{
		elecFileUploadService.saveFileUpload(elecFileUpload);
		
		/*//�߳̽���ʱ����յ�ǰsession
		request.getSession().removeAttribute("percent");*/
		
		return "save";
	}
	
	/**  
	* @Name: saaddListve
	* @Description: ʹ��ͼֽ����������λ��ѯ��Ӧ��ͼֽ�ϴ��ļ���Ϣ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-18���������ڣ�
	* @Parameters: ��
	* @Return: String����ת����dataChart/dataChartAddList.jsp
	* 					Ŀ���ǽ�dataChartAddList.jsp�����ݷŵ�dataChartAdd.jsp��form2��
	*/
	@AnnotationLimit(mid="ic",pid="ia")
	public String addList(){
		List<ElecFileUpload> list = elecFileUploadService.findFileUploadListByCondition(elecFileUpload);
		request.setAttribute("list", list);
		return "addList";
	}
	
	/**  
	* @Name: download
	* @Description: �ļ����أ�struts2�ķ�ʽ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-18���������ڣ�
	* @Parameters: ��
	* @Return: struts2�Ľ������
	*/
	@AnnotationLimit(mid="id",pid="ia")
	public String download(){
		try {
			//��ȡ�ļ�ID
			Integer fileID = elecFileUpload.getSeqId();
			//1��ʹ���ļ�ID����ѯ����ͼֽ�������ȡ��·��path
			ElecFileUpload fileUpload = elecFileUploadService.findFileByID(fileID);
			//·��path
			String path = ServletActionContext.getServletContext().getRealPath("")+fileUpload.getFileUrl();
			//�ļ�����
			String filename = fileUpload.getFileName();
			//���Գ�������
			filename = new String(filename.getBytes("gbk"),"iso8859-1");
			request.setAttribute("filename", filename);
			
			//2��ʹ��·��path�����ҵ���Ӧ���ļ���ת����InputStream
			InputStream in = new FileInputStream(new File(path));
			//��ջ����InputStream����
			elecFileUpload.setInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "download";
	}
	/**  
	* @throws UnsupportedEncodingException 
	 * @Name: luceneHome
	* @Description: ʹ��lucene���м���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-19���������ڣ�
	* @Parameters: ��
	* @Return: string ��ת��dataChart/dataChartIndex.jsp
	*/
	@AnnotationLimit(mid="ie",pid="ia")
	public String luceneHome() throws UnsupportedEncodingException{
		this.initSystemDDL();
		System.out.println("�������"+elecFileUpload.getQueryString());
		elecFileUpload.setQueryString(URLDecoder.decode(elecFileUpload.getQueryString().replace(".", "%"),"utf-8"));
		System.out.println("�����"+elecFileUpload.getQueryString());
		//ʹ��lucene��ѯ�����⣬��ʹ������id��ѯ���ݿ⣬����List<ElecFileUpload>
		List<ElecFileUpload> list = elecFileUploadService.findFileUploadListByLuceneCondition(elecFileUpload);
		request.setAttribute("list", list);
		return "luceneHome";
	}
	
	/**  
	* @throws UnsupportedEncodingException 
	 * @Name: delete
	* @Description: ɾ������ͼֽ���������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-19���������ڣ�
	* @Parameters: ��
	* @Return: string �ض���dataChart/elecFileUploadAction_home.do
	*/
	@AnnotationLimit(mid="if",pid="ia")
	public String delete() throws UnsupportedEncodingException{
		//��ȡ����ID
		Integer seqId = elecFileUpload.getHideId();
		//String hideProjId = elecFileUpload.getHideProjId();
		//String hideBelongTo = elecFileUpload.getHideBelongTo();
		System.out.println("����ǰ��"+elecFileUpload.getHideQueryString());
		elecFileUpload.setHideQueryString(URLEncoder.encode(elecFileUpload.getHideQueryString(),"utf-8").replace("%", "."));
		System.out.println("����1��"+elecFileUpload.getHideQueryString());
		elecFileUploadService.deleteFileUploadByID(seqId);
		return "delete";
	}
}
