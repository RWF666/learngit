package junit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.dao.impl.ElecTextDaoImpl;
import com.weige.elec.domain.ElecText;

public class DaoTest {
	/**保存*/
	@Test
	public void testSave(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) applicationContext.getBean(IElecTextDao.SERVICE_NAME);
		ElecText elecText = new ElecText();
		elecText.setTextName("王六");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("七麻子");
		elecTextDao.save(elecText);
	}
	/**保存*/
	@Test
	public void testUpdate(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) applicationContext.getBean(IElecTextDao.SERVICE_NAME);
		ElecText elecText = new ElecText();
		elecText.setTextID("402881e85d8ec70b015d8ec712130001");;
		elecText.setTextName("测试");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("是测试dao才对辣");
		elecTextDao.update(elecText);
	}
	/**使用主键查询ID查询对象*/
	@Test
	public void findObjectByID(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) applicationContext.getBean(IElecTextDao.SERVICE_NAME);
		Serializable id = "402881e85d8ec70b015d8ec712130001";
		ElecText elecText = elecTextDao.findObjectByID(id);
		System.out.println(elecText.getTextName());
		System.out.println(elecText.getTextRemark());
	}
	
	/**使用主键删除对象*/
	@Test
	public void deleteObjectByIds(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) applicationContext.getBean(IElecTextDao.SERVICE_NAME);
		Serializable[] ids = {"402881e85d91d39b015d91d4064d0001"};
		elecTextDao.deleteObjectByIds(ids);
	}
	/**使用集合对象*/
	@Test
	public void deleteObjectByCollection(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextDao elecTextDao = (IElecTextDao) applicationContext.getBean(IElecTextDao.SERVICE_NAME);
		List<ElecText> elecTexts = new ArrayList<ElecText>();
		ElecText elecText1 = new ElecText();
		ElecText elecText2 = new ElecText();
		ElecText elecText3 = new ElecText();
		elecText1.setTextID("402881e85d920971015d920977c60001");
		elecText2.setTextID("402881e85d920984015d92098a180001");
		elecText3.setTextID("402881e85d920997015d92099d380001");
		elecTexts.add(elecText1);
		elecTexts.add(elecText2);
		elecTexts.add(elecText3);
		elecTextDao.deleteObjectByCollection(elecTexts);
	}
}
