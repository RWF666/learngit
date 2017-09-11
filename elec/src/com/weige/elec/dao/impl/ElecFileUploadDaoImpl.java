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
	* @Description: ͨ��ѡ��������λ��ͼֽ���Ĳ�ѯ��������ѯ��Ӧ��λ��ͼֽ�µ��ļ��ϴ����б�����List<ElecFileUpload>
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-18���������ڣ�
	* @Parameters: String condition:��ѯ����
	* 			   Object[] params:�ɱ����
	* 			   Map<String,String> orderby������
	* @Return: List<ElecFileUpload>������ͼֽ�ļ���
	*/
	public List<ElecFileUpload> findFileUploadListByCondition(String condition,
			final Object[] params, Map<String, String> orderby) {
		//sql���
		String sql = "SELECT o.seqID,a.ddlName,b.ddlName,o.FileName,o.FileURL,o.progressTime,o.comment FROM elec_fileupload o "+
					 " INNER JOIN elec_systemddl a ON o.projID = a.ddlCode AND a.keyword='������λ' " +
					 " INNER JOIN elec_systemddl b ON o.belongTo = b.ddlCode AND b.keyword='ͼֽ���' " +
					 " WHERE 1=1";
		//��Map�����д�ŵ��ֶ�������֯��ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbySql(orderby);
		//��Ӳ�ѯ����
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
		//����ѯ�Ľ������װ��List<ElecFileUpload>
		List<ElecFileUpload> fileUploadList = new ArrayList<ElecFileUpload>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Object [] arrays = list.get(i);
				//��֯ҳ����ʾ�Ķ���
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
	
	
	/**��Map�����д�ŵ��ֶ�������֯��
	 * ORDER BY o.textDate ASC,o.textName DESC*/
	private String orderbySql(Map<String, String> orderby) {
		StringBuffer buffer = new StringBuffer("");
		if(orderby!=null && orderby.size()>0){
			buffer.append(" ORDER BY ");
			for(Map.Entry<String, String> map:orderby.entrySet()){
				buffer.append(map.getKey()+" "+map.getValue()+",");
			}
			//��ѭ����ɾ�����һ������
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}
}
