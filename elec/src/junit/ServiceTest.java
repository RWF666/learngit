package junit;

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
import com.weige.elec.service.IElecTextService;
import com.weige.elec.service.impl.ElecTextServiceImpl;

public class ServiceTest {
	@Test
	public void testSave(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextService elecTextService = (IElecTextService) applicationContext.getBean(IElecTextService.SERVICE_NAME);
		ElecText elecText = new ElecText();
		elecText.setTextName("测试");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("说了就是测试service");
		elecTextService.saveElecText(elecText);
	}
	/**模拟action层，测试地方方法，指定查询条件查询结果列表*/
	@Test
	public void findCollectionByConditionNoPage(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextService elecTextService = (IElecTextService) applicationContext.getBean(IElecTextService.SERVICE_NAME);
		
		ElecText elecText = new ElecText();
//		elecText.setTextName("张");
//		elecText.setTextRemark("张");
		List<ElecText> elecTexts = elecTextService.findCollectionByConditionNoPage(elecText);
		if(elecTexts!=null && elecTexts.size()>0){
			for(ElecText text : elecTexts){
				System.out.println(text.getTextName()+":"+text.getTextRemark());
			}
		}
	}
}
