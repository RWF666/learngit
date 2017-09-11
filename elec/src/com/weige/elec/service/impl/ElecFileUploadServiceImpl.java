package com.weige.elec.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecFileUploadDao;
import com.weige.elec.domain.ElecFileUpload;
import com.weige.elec.service.IElecFileUploadService;
import com.weige.elec.utils.Configuration;
import com.weige.elec.utils.DateUtils;
import com.weige.elec.utils.FileUploadDocument;
import com.weige.elec.utils.FileUtils;
import com.weige.elec.utils.LuceneUtils;



@Service(IElecFileUploadService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecFileUploadServiceImpl implements IElecFileUploadService {

	/**����ͼֽ�����Dao*/
	@Resource(name=IElecFileUploadDao.SERVICE_NAME)
	IElecFileUploadDao elecFileUploadDao;

	/**  
	* @Name: saveFileUpload
	* @Description: ��������ͼֽ����
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-18���������ڣ�
	* @Parameters: ��
	* @Return: String���ض���dataChart/dataChartAdd.jsp
	*/
	@Transactional(readOnly=false)
	public void saveFileUpload(ElecFileUpload elecFileUpload) {
		//1���ļ��ϴ�
		//��ǰʱ��
		String progressTime = DateUtils.dateToString(new Date());
		//������λ
		String projId = elecFileUpload.getProjId();
		//ͼֽ���
		String belongTo = elecFileUpload.getBelongTo();
		//�ļ�
		File[] uploads = elecFileUpload.getUploads();
		//�ļ���
		String[] uploadsFileNames = elecFileUpload.getUploadsFileName();
		//�ļ�����
		String[] comments = elecFileUpload.getComments();
		//2����֯po���󣬱��浽����ͼֽ��������ݿ���
		if(uploads!=null &&uploads.length>0){
			for(int i = 0;i<uploads.length;i++){
				ElecFileUpload fileUpload = new ElecFileUpload();
				fileUpload.setProjId(projId);
				fileUpload.setBelongTo(belongTo);
				fileUpload.setFileName(uploadsFileNames[i]);
				/**���ļ��ϴ���ͬʱ����·��path*/
				String fileUrl = FileUtils.fileUploadReturnPath(uploads[i],uploadsFileNames[i],"����ͼֽ����");
				fileUpload.setFileUrl(fileUrl);
				fileUpload.setProgressTime(progressTime);
				fileUpload.setComment(comments[i]);
				//ִ�б���Ĵ���
				elecFileUploadDao.save(fileUpload);
				//���lucene,���������������
				LuceneUtils.addIndex(fileUpload);
				
			}
		}
	}

	@Override
	public List<ElecFileUpload> findFileUploadListByCondition(
			ElecFileUpload elecFileUpload) {
		//��֯��ѯ����
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//������λ
		String projId = elecFileUpload.getProjId();
		if(StringUtils.isNotBlank(projId) && !projId.equals("0")){
			condition += " and o.projId=?";
			paramsList.add(projId);
		}
		//ͼֽ���
		String belongTo = elecFileUpload.getBelongTo();
		if(StringUtils.isNotBlank(belongTo) && !belongTo.equals("0")){
			condition += " and o.belongTo=?";
			paramsList.add(belongTo);
		}
		//����
		Object [] params = paramsList.toArray();
		//���򣬰���ʱ������
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.progressTime", "asc");
		List<ElecFileUpload> list = elecFileUploadDao.findFileUploadListByCondition(condition,params,orderby);
		return list;
	}
	
	/**  
	* @Name: findFileByID
	* @Description: ʹ������ID����ѯ����ͼֽ��Ϣ
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-18���������ڣ�
	* @Parameters: Integer������ID
	* @Return: ElecFileUpload������ͼֽ�Ķ���
	*/
	@Override
	public ElecFileUpload findFileByID(Integer fileID) {
		return elecFileUploadDao.findObjectByID(fileID);
	}
	
	/**  
	* @Name: findFileUploadListByLuceneCondition
	* @Description: ʹ��lucene��ѯ�����⣬��ʹ������id��ѯ���ݿ⣬����List<ElecFileUpload>
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-19���������ڣ�
	* @Parameters: ElecFileUpload��vo����
	* @Return: List<ElecFileUpload>���ļ��ϴ��ļ���
	*/
	@Override
	public List<ElecFileUpload> findFileUploadListByLuceneCondition(
			ElecFileUpload elecFileUpload) {
		//��ȡҳ�洫�ݵ�ֵ����֯��������
		//������λ
		String projId = elecFileUpload.getProjId();
		//ͼֽ���
		String belongTo = elecFileUpload.getBelongTo();
		//��������
		String queryString = elecFileUpload.getQueryString();
		
		//���յĽ����
		List<ElecFileUpload> finalList = new ArrayList<ElecFileUpload>();
		//ʹ�ò�ѯ������ѯ�����⣬����list����
		List<ElecFileUpload> list = LuceneUtils.searcherIndexByConditon(projId,belongTo,queryString);
		//�����������ʹ������Id�����ݿ��ѯ����������
		if(list!=null && list.size()>0){
			for(ElecFileUpload fileUpload:list){
				Integer seqId = fileUpload.getSeqId();
				String condition = " and o.seqId=?";
				Object[] params = {seqId};
				List<ElecFileUpload> finalElecFileUpload = elecFileUploadDao.findFileUploadListByCondition(condition, params, null);
				//�������������õĸ��������õ�������id��ѯ���Ķ�����
				if(StringUtils.isNotEmpty(fileUpload.getFileName())){
					finalElecFileUpload.get(0).setFileName(fileUpload.getFileName());
				}
				if(StringUtils.isNotEmpty(fileUpload.getComment())){
					finalElecFileUpload.get(0).setComment(fileUpload.getComment());
				}
				
				finalList.addAll(finalElecFileUpload);
			}
		}
		
		return finalList;
	}
	
	/**  
	* @Name: deleteFileUploadByID
	* @Description: ʹ������ID��ɾ��ͼֽ��������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-19���������ڣ�
	* @Parameters: Integer������ID
	* @Return: ��
	*/
	@Override
	@Transactional(readOnly=false)
	public void deleteFileUploadByID(Integer seqId) {
		ElecFileUpload elecFileUpload = elecFileUploadDao.findObjectByID(seqId);
		//ɾ������
		String path = ServletActionContext.getServletContext().getRealPath("")+elecFileUpload.getFileUrl();
		if(StringUtils.isNotBlank(path)){
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
		}
		//ɾ�����ݿ������
		elecFileUploadDao.deleteObjectByIds(seqId);
		//ɾ�������������
		deleteIndex(seqId);
	}
	
	/**����Idɾ����������Ϣ*/
	private void deleteIndex(Integer seqId) {
		//ʹ�ô���ɾ��
		String id = NumericUtils.intToPrefixCoded(seqId);
		Term term = new Term("seqId",id);
		/**�������޸ģ���ѯ��ɾ������Ҫʹ�÷ִ���*/
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, Configuration.getAnalyzer());
			IndexWriter indexWriter = new IndexWriter(Configuration.getDirectory(), indexWriterConfig);
			//�������
			indexWriter.deleteDocuments(term);
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
