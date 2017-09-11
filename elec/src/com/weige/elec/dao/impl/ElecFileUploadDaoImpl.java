package com.weige.elec.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecFileUploadDao;
import com.weige.elec.domain.ElecFileUpload;



@Repository(IElecFileUploadDao.SERVICE_NAME)
public class ElecFileUploadDaoImpl  extends CommonDaoImpl<ElecFileUpload> implements IElecFileUploadDao {

	/**  
	* @Name: findFileUploadListByCondition
	* @Description: 通过选择所属单位和图纸类别的查询条件，查询对应单位和图纸下的文件上传的列表，返回List<ElecFileUpload>
	* @Author: 饶伟峰（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2017-8-18（创建日期）
	* @Parameters: String condition:查询条件
	* 			   Object[] params:可变参数
	* 			   Map<String,String> orderby：排序
	* @Return: List<ElecFileUpload>：资料图纸的集合
	*/
	public List<ElecFileUpload> findFileUploadListByCondition(String condition,
			final Object[] params, Map<String, String> orderby) {
		//sql语句
		String sql = "SELECT o.seqID,a.ddlName,b.ddlName,o.FileName,o.FileURL,o.progressTime,o.comment FROM elec_fileupload o "+
					 " INNER JOIN elec_systemddl a ON o.projID = a.ddlCode AND a.keyword='所属单位' " +
					 " INNER JOIN elec_systemddl b ON o.belongTo = b.ddlCode AND b.keyword='图纸类别' " +
					 " WHERE 1=1";
		//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbySql(orderby);
		//添加查询条件
		final String finalSql = sql + condition + orderbyCondition;
		
		List<Object[]> list = this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(finalSql)
									 .addScalar("o.seqID")
									 .addScalar("a.ddlName")
									 .addScalar("b.ddlName")
									 .addScalar("o.FileName")
									 .addScalar("o.FileURL")
									 .addScalar("o.progressTime")
									 .addScalar("o.comment");
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				return query.list();
			}
			
		});
		//将查询的结果，封装到List<ElecFileUpload>
		List<ElecFileUpload> fileUploadList = new ArrayList<ElecFileUpload>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Object [] arrays = list.get(i);
				//组织页面显示的对象
				//o.seqID,a.ddlName,b.ddlName,o.FileName,o.FileURL,o.progressTime,o.comment
				ElecFileUpload elecFileUpload = new ElecFileUpload();
				elecFileUpload.setSeqId(Integer.parseInt(arrays[0].toString()));
				elecFileUpload.setProjId(arrays[1].toString());
				elecFileUpload.setBelongTo(arrays[2].toString());
				elecFileUpload.setFileName(arrays[3].toString());
				elecFileUpload.setFileUrl(arrays[4].toString());
				elecFileUpload.setProgressTime(arrays[5].toString());
				elecFileUpload.setComment(arrays[6].toString());
				fileUploadList.add(elecFileUpload);
			}
		}
		return fileUploadList;
	}
	
	
	/**将Map集合中存放的字段排序，组织成
	 * ORDER BY o.textDate ASC,o.textName DESC*/
	private String orderbySql(Map<String, String> orderby) {
		StringBuffer buffer = new StringBuffer("");
		if(orderby!=null && orderby.size()>0){
			buffer.append(" ORDER BY ");
			for(Map.Entry<String, String> map:orderby.entrySet()){
				buffer.append(map.getKey()+" "+map.getValue()+",");
			}
			//在循环后，删除最后一个逗号
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}
}
