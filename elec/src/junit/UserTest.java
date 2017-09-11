package junit;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.weige.elec.dao.IElecCommonMsgDao;
import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecUserService;

public class UserTest {
	@Test
	public void testHibernate(){
		Configuration configuration = new Configuration();
		configuration.configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
	}
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
		IElecUserService elecUserService = (IElecUserService) applicationContext.getBean(IElecUserService.SERVICE_NAME);
		ElecUser elecUser = new ElecUser();
		elecUser.setUserID("1");
		elecUserService.findUserListByCondition(elecUser);
	}
}
