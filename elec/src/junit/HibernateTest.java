package junit;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Test;

import com.weige.elec.domain.ElecText;

public class HibernateTest {
	@Test
	public void testSave(){
		Configuration configuration = new Configuration();
		configuration.configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		ElecText elecText = new ElecText();
		elecText.setTextName("测试");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("说了就是测试啦");
		session.save(elecText);
		transaction.commit();
		session.close();
	}
}
