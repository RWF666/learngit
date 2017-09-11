package com.weige.elec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




public class ElecPopedom implements Serializable {
	
	
	private String mid;			//Ȩ��Code������ID��
	private String pid;			//����Ȩ��code������Ѿ��Ǹ��ڵ���Ϊ0
	private String name;		//Ȩ������
	private String url;			//Ȩ����ϵͳ��ִ�з��ʵ�ַ��URL
	private String icon;		//����ǲ˵�����Ϊ��ʾͼƬ��URL
	private String target;		//����ǲ˵�������ִ�е�Frame��������
	private boolean isParent;	//�Ƿ��Ǹ��ڵ㣬���ڵ�Ϊtrue���ӽڵ�Ϊfalse
	private boolean isMenu;		//�Ƿ���ϵͳ�˵��ṹ
	
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public boolean getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(boolean isMenu) {
		this.isMenu = isMenu;
	}
	
	/**�ǳ־û�javabean����*/
	//��ʾ���һ���������������ӵļ���
	private List<ElecPopedom> list = new ArrayList<ElecPopedom>();
	//��ɫID
	private String roleID;
	/**
	 * ���һ����ʶ���ж�ҳ��ĸ�ѡ���Ƿ�ѡ�У��ñ�ʶҪ���õ�ElecPopedom������
   flag=1��ѡ��
   flag=2��û�б�ѡ��
	 */
	private String flag;
	
	//ҳ���д��ݵ�ѡ�еĹ���Ȩ��
	private String [] selectoper;
	
	//ҳ���д��ݵ�ѡ�е��û�ID
	private String [] selectuser;


	public String[] getSelectuser() {
		return selectuser;
	}
	public void setSelectuser(String[] selectuser) {
		this.selectuser = selectuser;
	}
	public String[] getSelectoper() {
		return selectoper;
	}
	public void setSelectoper(String[] selectoper) {
		this.selectoper = selectoper;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public List<ElecPopedom> getList() {
		return list;
	}
	public void setList(List<ElecPopedom> list) {
		this.list = list;
	}
}
