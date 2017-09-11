package junit;

import org.junit.Test;

import com.weige.elec.domain.xsd.ElecSystemDDL;
import com.weige.elec.webservice.FindSystemByKeyword;
import com.weige.elec.webservice.FindSystemByKeywordResponse;
import com.weige.elec.webservice.IWebSystemDDLServiceSkeleton;

public class WebserviceTest {

	/**���Խӿڵ�ʵ����*/
	@Test
	public void testWebService(){
		IWebSystemDDLServiceSkeleton ddlServiceSkeleton = new IWebSystemDDLServiceSkeleton();
		FindSystemByKeyword findSystemByKeyword = new FindSystemByKeyword();
		findSystemByKeyword.setArgs0("������λ");
		FindSystemByKeywordResponse byKeywordResponse = ddlServiceSkeleton.findSystemByKeyword(findSystemByKeyword);
		ElecSystemDDL [] ddl = byKeywordResponse.get_return();
		if(ddl!=null && ddl.length>0){
			for(ElecSystemDDL systemDDL:ddl){
				System.out.println(systemDDL.getKeyword()+"   "+systemDDL.getDdlCode()+"   "+systemDDL.getDdlName());
			}
		}
		
	}
	
}
