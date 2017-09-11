package junit;

import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.weige.elec.dao.IElecCommonMsgDao;
import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.service.IElecCommonMsgService;

public class CommonMsgTest {
	@Test
	public void testDao(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecCommonMsgDao elecCommonMsgDao = (IElecCommonMsgDao) applicationContext.getBean(IElecCommonMsgDao.SERVICE_NAME);
		ElecCommonMsg elecCommonMsg = new ElecCommonMsg();
		elecCommonMsg.setDevRun("正常");
		elecCommonMsg.setStationRun("正常");
		elecCommonMsg.setCreateDate(new Date());
		elecCommonMsgDao.save(elecCommonMsg);
	}
	@Test
	public void testService(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecCommonMsgService elecCommonMsgService = (IElecCommonMsgService) applicationContext.getBean(IElecCommonMsgService.SERVICE_NAME);
		ElecCommonMsg elecCommonMsg = new ElecCommonMsg();
		elecCommonMsg.setDevRun("正常");
		elecCommonMsg.setStationRun("正常");
		elecCommonMsg.setCreateDate(new Date());
		elecCommonMsgService.saveCommonMsg(elecCommonMsg);
	}
}
