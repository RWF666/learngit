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
		elecText.setTextName("����");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("˵�˾��ǲ���service");
		elecTextService.saveElecText(elecText);
	}
	/**ģ��action�㣬���Եط�������ָ����ѯ������ѯ����б�*/
	@Test
	public void findCollectionByConditionNoPage(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		IElecTextService elecTextService = (IElecTextService) applicationContext.getBean(IElecTextService.SERVICE_NAME);
		
		ElecText elecText = new ElecText();
//		elecText.setTextName("��");
//		elecText.setTextRemark("��");
		List<ElecText> elecTexts = elecTextService.findCollectionByConditionNoPage(elecText);
		if(elecTexts!=null && elecTexts.size()>0){
			for(ElecText text : elecTexts){
				System.out.println(text.getTextName()+":"+text.getTextRemark());
			}
		}
	}
}
