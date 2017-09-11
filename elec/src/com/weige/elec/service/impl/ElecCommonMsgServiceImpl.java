package com.weige.elec.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.taglibs.standard.lang.jstl.ELEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecCommonMsgContentDao;
import com.weige.elec.dao.IElecCommonMsgDao;
import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecCommonMsgContent;
import com.weige.elec.domain.ElecText;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecTextService;
import com.weige.elec.utils.StringUtil;

@Service(IElecCommonMsgService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecCommonMsgServiceImpl implements IElecCommonMsgService{
	/**���м�ر�Dao*/
	@Resource(name=IElecCommonMsgDao.SERVICE_NAME)
	IElecCommonMsgDao elecCommonMsgDao;
	
	
	/**���м�����ݱ�Dao*/
	@Resource(name=IElecCommonMsgContentDao.SERVICE_NAME)
	IElecCommonMsgContentDao elecCommonMsgContentDao;
	
	
	
//	/**  
//	* @Name: findCommonMsg
//	* @Description: ��ѯ���м�ر������
//	* @Author: ��ΰ�壨���ߣ�
//	* @Version: V1.00 ���汾�ţ�
//	* @Create Date: 2017-7-31���������ڣ�
//	* @Parameters: ��
//	* @Return: ElecCommonMsg������po����
//	*/
//	@Override
//	public ElecCommonMsg findCommonMsg() {
//		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
//		ElecCommonMsg elecCommonMsg = null;
//		if(list!=null && list.size()>0){
//			elecCommonMsg = list.get(0);
//		}
//		return elecCommonMsg;
//	}
//	/**  
//	* @Name: saveCommonMsg
//	* @Description: �������м�ر������
//	* @Author: ��ΰ�壨���ߣ�
//	* @Version: V1.00 ���汾�ţ�
//	* @Create Date: 2017-7-31���������ڣ�
//	* @Parameters: elecCommonMsg vo����
//	* @Return: wu
//	*/
//	@Transactional(readOnly=false)
//	public void saveCommonMsg(ElecCommonMsg elecCommonMsg) {
//		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
//		/**������ݴ��������*/
//		if(list!=null && list.size()>0){
//			/**ʹ�ÿ��ո���*/
//			ElecCommonMsg commonMag = list.get(0);
//			commonMag.setStationRun(elecCommonMsg.getStationRun());
//			commonMag.setDevRun(elecCommonMsg.getDevRun());
//			commonMag.setCreateDate(new Date());
//		}
//		/**������ݲ������򱣴�*/
//		else{
//			elecCommonMsg.setCreateDate(new Date());
//			elecCommonMsgDao.save(elecCommonMsg);
//		}
//	}
	/**  
	* @Name: findElecCommonMsg
	* @Description: ��ȡ���м���е�����
	* @Author: �������ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 
	* @Parameters: ��
	* @Return: ElecCommonMsg����װ���ݶ���
	*/
	public ElecCommonMsg findCommonMsg() {
		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
		ElecCommonMsg commonMsg = null;
		if(list!=null && list.size()>0){
			commonMsg = list.get(0);
			/**********************************************begin**********************************************************/
			//��ȡ��������
			//��������Ϊ������������ʾ˳���������У���ѯվ���������������
			String stationCondition = " and o.type=?";
			Object [] stationParams = {"1"};
			Map<String, String> stationOrderby = new LinkedHashMap<String, String>();
			stationOrderby.put("o.orderby", "asc");
			List<ElecCommonMsgContent> stationList = elecCommonMsgContentDao.findCollectionByConditionNoPage(stationCondition, stationParams, stationOrderby);
			//��ȡ���ص����ݣ�ƴװ֮��
			String stationContent = "";
			if(stationList!=null && stationList.size()>0){
				for(ElecCommonMsgContent elecCommonMsgContent:stationList){
					String content = elecCommonMsgContent.getContent();
					stationContent += content;
				}
			}
			//�����ݸ�ֵ��ҳ������ԣ�վ�����������
			commonMsg.setStationRun(stationContent);
			/**********************************************************************************/
			//��������Ϊ������������ʾ˳���������У���ѯվ���������������
			String devCondition = " and o.type=?";
			Object [] devParams = {"2"};
			Map<String, String> devOrderby = new LinkedHashMap<String, String>();
			devOrderby.put("o.orderby", "asc");
			List<ElecCommonMsgContent> devList = elecCommonMsgContentDao.findCollectionByConditionNoPage(devCondition, devParams, devOrderby);
			//��ȡ���ص����ݣ�ƴװ֮��
			String devContent = "";
			if(devList!=null && devList.size()>0){
				for(ElecCommonMsgContent elecCommonMsgContent:devList){
					String content = elecCommonMsgContent.getContent();
					devContent += content;
				}
			}
			//�����ݸ�ֵ��ҳ������ԣ��豸���������
			commonMsg.setDevRun(devContent);
			/**********************************************end**********************************************************/
		}
		return commonMsg;
	}
	
	/**  
	* @Name: saveElecCommonMsg
	* @Description: �������м��
	* @Author: �������ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 
	* @Parameters: ElecCommonMsg����װ����Ĳ���
	* @Return: ��
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveCommonMsg(ElecCommonMsg elecCommonMsg) {
		/**********************************************begin**********************************************************/
		//���浽���ݱ���
		//ɾ��֮ǰ������
		List<ElecCommonMsgContent> contentList = elecCommonMsgContentDao.findCollectionByConditionNoPage("", null, null);
		elecCommonMsgContentDao.deleteObjectByCollection(contentList);
		//��ҳ���ȡվ������������豸�������������վ��������������豸���������������
		String stationRun = elecCommonMsg.getStationRun();
		String devRun = elecCommonMsg.getDevRun();
		//����StirngUtil�ķ������ָ��ַ���
		List<String> stationList = StringUtil.getContentByList(stationRun, 50);
		if(stationList!=null && stationList.size()>0){
			for(int i=0;i<stationList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("1");//1��ʾվ���������
				elecCommonMsgContent.setContent(stationList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		List<String> devList = StringUtil.getContentByList(devRun, 50);
		if(devList!=null && devList.size()>0){
			for(int i=0;i<devList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("2");//2��ʾ�豸�������
				elecCommonMsgContent.setContent(devList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		/**********************************************end**********************************************************/
		
		
		//��ѯ���м�ر���ȡ���м�ر�����ݣ�����List<ElecCommonMsg> list��ʹ��list��Ϊ�ж����ݿ����Ƿ��������
		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
		//���list!=null:���ݱ���д������ݣ���ȡҳ�洫�ݵ�2����������֯PO����ִ�и��£�update��
		if(list!=null && list.size()>0){
			//����һ����ɾ���ٴ���
			//����������֯PO����ִ��update
			ElecCommonMsg commonMsg = list.get(0);
			commonMsg.setStationRun("1");//1��ʾվ���������
			commonMsg.setDevRun("2");//2��ʾ�豸�������
			commonMsg.setCreateDate(new Date());
		}
		//���list==null:���ݱ���в��������ݣ���ȡҳ�洫�ݵ�2����������֯PO����ִ��������save��
		else{
			ElecCommonMsg commonMsg = new ElecCommonMsg();
			commonMsg.setCreateDate(new Date());
			commonMsg.setStationRun("1");//1��ʾվ���������
			commonMsg.setDevRun("2");//2��ʾ�豸�������
			elecCommonMsgDao.save(commonMsg);
		}
		
		
	}
}
