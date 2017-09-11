package com.weige.elec.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.weige.elec.domain.ElecUser;


public class SystemUtils implements Filter {

	/**�ڵ�¼֮ǰ����ЩURL��û��Session�ģ����ҵ���ЩURL��ͳһ���п��ƣ��൱�ڹ������в�������Щ����������*/
	List<String> list = new ArrayList<String>();
	/**web������ʼ��*/
	public void init(FilterConfig config) throws ServletException {
		//û��Session������Ҫ����
		list.add("/index.jsp");
		list.add("/image.jsp");
		list.add("/system/elecMenuAction_menuHome.do");
		
		//5�뵹��ʱ��ҳ��
		list.add("/error.jsp");
		list.add("/system/elecMenuAction_logout.do");
	}
	
	/**��ת֮ǰ����ɹ������ķ���*/
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//��ȡ���ʵ�servlet��·��
		String path = request.getServletPath();
		//����ת��index.jspҳ��֮ǰ���Ȼ�ȡCookie����ֵ�ķ�ʽ��index.jsp
		this.initIndexPage(request,path);
		/**��Ӵֿ���Ȩ�޿���*/
		//û��Session��Ҫ���е�����
		if(list.contains(path)){
			//����
			chain.doFilter(request, response);
			return;
		}
		//�ж�Session�Ƿ����
		ElecUser elecUser = (ElecUser)request.getSession().getAttribute("globle_user");
		//Session�д������ݣ����У�ָ����Ӧ��URL��ҳ��
		if(elecUser!=null){
			//����
			chain.doFilter(request, response);
			return;
		}
		
		//���Session�в��������ݣ��ض���index.jsp��ҳ����
		//response.sendRedirect(request.getContextPath()+"/index.jsp");
		//���Session�в��������ݣ��ض���error.jsp��ҳ����
		response.sendRedirect(request.getContextPath()+"/error.jsp");
	}

	

	/**����*/
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	/**����ת��index.jspҳ��֮ǰ���Ȼ�ȡCookie����ֵ�ķ�ʽ��index.jsp*/
	private void initIndexPage(HttpServletRequest request, String path) {
		if(path!=null && path.equals("/index.jsp")){
			//��ȡCookie�д�ŵ��û���������
			//�û���
			String name = "";
			//����
			String password = "";
			//��ѡ��
			String checked = "";
			//��Cookie�л�ȡ��ס���д�ŵĵ�¼��������
			Cookie[] cookies = request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(Cookie cookie:cookies){
					if("name".equals(cookie.getName())){
						//�û���
						name = cookie.getValue();
						//��������ģ���Ҫ����
						try {
							name = URLDecoder.decode(name, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						//��ѡ��ѡ��
						checked = "checked";
					}
					if("password".equals(cookie.getName())){
						//����
						password = cookie.getValue();
					}
					
				}
			}
			//��ŵ�request��
			request.setAttribute("name", name);
			request.setAttribute("password", password);
			request.setAttribute("checked", checked);
		}
	}
}
