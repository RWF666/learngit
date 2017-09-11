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

	/**资料图纸管理表Dao*/
	@Resource(name=IElecFileUploadDao.SERVICE_NAME)
	IElecFileUploadDao elecFileUploadDao;

	/**  
	* @Name: saveFileUpload
	* @Description: 保存资料图纸管理
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-18（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到dataChart/dataChartAdd.jsp
	*/
	@Transactional(readOnly=false)
	public void saveFileUpload(ElecFileUpload elecFileUpload) {
		//1、文件上传
		//当前时间
		String progressTime = DateUtils.dateToString(new Date());
		//所属单位
		String projId = elecFileUpload.getProjId();
		//图纸类别
		String belongTo = elecFileUpload.getBelongTo();
		//文件
		File[] uploads = elecFileUpload.getUploads();
		//文件名
		String[] uploadsFileNames = elecFileUpload.getUploadsFileName();
		//文件描述
		String[] comments = elecFileUpload.getComments();
		//2、组织po对象，保存到资料图纸管理的数据库中
		if(uploads!=null &&uploads.length>0){
			for(int i = 0;i<uploads.length;i++){
				ElecFileUpload fileUpload = new ElecFileUpload();
				fileUpload.setProjId(projId);
				fileUpload.setBelongTo(belongTo);
				fileUpload.setFileName(uploadsFileNames[i]);
				/**将文件上传，同时返回路径path*/
				String fileUrl = FileUtils.fileUploadReturnPath(uploads[i],uploadsFileNames[i],"资料图纸管理");
				fileUpload.setFileUrl(fileUrl);
				fileUpload.setProgressTime(progressTime);
				fileUpload.setComment(comments[i]);
				//执行保存的代码
				elecFileUploadDao.save(fileUpload);
				//添加lucene,想索引库添加数据
				LuceneUtils.addIndex(fileUpload);
				
			}
		}
	}

	@Override
	public List<ElecFileUpload> findFileUploadListByCondition(
			ElecFileUpload elecFileUpload) {
		//组织查询条件
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//所属单位
		String projId = elecFileUpload.getProjId();
		if(StringUtils.isNotBlank(projId) && !projId.equals("0")){
			condition += " and o.projId=?";
			paramsList.add(projId);
		}
		//图纸类别
		String belongTo = elecFileUpload.getBelongTo();
		if(StringUtils.isNotBlank(belongTo) && !belongTo.equals("0")){
			condition += " and o.belongTo=?";
			paramsList.add(belongTo);
		}
		//数组
		Object [] params = paramsList.toArray();
		//排序，按照时间排序
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.progressTime", "asc");
		List<ElecFileUpload> list = elecFileUploadDao.findFileUploadListByCondition(condition,params,orderby);
		return list;
	}
	
	/**  
	* @Name: findFileByID
	* @Description: 使用主键ID，查询资料图纸信息
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-18（创建日期）
	* @Parameters: Integer：主键ID
	* @Return: ElecFileUpload：资料图纸的对象
	*/
	@Override
	public ElecFileUpload findFileByID(Integer fileID) {
		return elecFileUploadDao.findObjectByID(fileID);
	}
	
	/**  
	* @Name: findFileUploadListByLuceneCondition
	* @Description: 使用lucene查询索引库，在使用主键id查询数据库，返回List<ElecFileUpload>
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-19（创建日期）
	* @Parameters: ElecFileUpload：vo对象
	* @Return: List<ElecFileUpload>：文件上传的集合
	*/
	@Override
	public List<ElecFileUpload> findFileUploadListByLuceneCondition(
			ElecFileUpload elecFileUpload) {
		//获取页面传递的值，组织索引条件
		//所属单位
		String projId = elecFileUpload.getProjId();
		//图纸类别
		String belongTo = elecFileUpload.getBelongTo();
		//描述搜索
		String queryString = elecFileUpload.getQueryString();
		
		//最终的结果集
		List<ElecFileUpload> finalList = new ArrayList<ElecFileUpload>();
		//使用查询条件查询索引库，返回list集合
		List<ElecFileUpload> list = LuceneUtils.searcherIndexByConditon(projId,belongTo,queryString);
		//遍历结果集，使用主键Id从数据库查询完整的数据
		if(list!=null && list.size()>0){
			for(ElecFileUpload fileUpload:list){
				Integer seqId = fileUpload.getSeqId();
				String condition = " and o.seqId=?";
				Object[] params = {seqId};
				List<ElecFileUpload> finalElecFileUpload = elecFileUploadDao.findFileUploadListByCondition(condition, params, null);
				//将索引库中设置的高亮，设置到用主键id查询到的对象当中
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
	* @Description: 使用主键ID，删除图纸管理数据
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-08-19（创建日期）
	* @Parameters: Integer：主键ID
	* @Return: 无
	*/
	@Override
	@Transactional(readOnly=false)
	public void deleteFileUploadByID(Integer seqId) {
		ElecFileUpload elecFileUpload = elecFileUploadDao.findObjectByID(seqId);
		//删除附件
		String path = ServletActionContext.getServletContext().getRealPath("")+elecFileUpload.getFileUrl();
		if(StringUtils.isNotBlank(path)){
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
		}
		//删除数据库的数据
		elecFileUploadDao.deleteObjectByIds(seqId);
		//删除索引库的数据
		deleteIndex(seqId);
	}
	
	/**根据Id删除索引库信息*/
	private void deleteIndex(Integer seqId) {
		//使用词条删除
		String id = NumericUtils.intToPrefixCoded(seqId);
		Term term = new Term("seqId",id);
		/**新增，修改，查询，删除都需要使用分词器*/
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, Configuration.getAnalyzer());
			IndexWriter indexWriter = new IndexWriter(Configuration.getDirectory(), indexWriterConfig);
			//添加数据
			indexWriter.deleteDocuments(term);
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
