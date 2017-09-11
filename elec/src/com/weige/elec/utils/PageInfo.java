package com.weige.elec.utils;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import javax.servlet.http.HttpServletRequest;

public class PageInfo {
	 //HTTP����
    private HttpServletRequest req;
    //ÿҳ�ļ�¼��
    private int pageSize = 5;
    //��ǰҳ
    private int currentPageNo = 1;
    //��ʼ��¼��
    private int beginResult = 0;
    //�ܼ�¼��
    private int totalResult = 0;
    //��ҳ��
    private int totalPage = 0;
    
    private PageBean page=null;

    //��־
   // protected  static final Logger log = Logger.getLogger("testLogger");

    /**
     * ��ʼ��
     * @param req HttpServletRequest HTTP����
     */
    public PageInfo(HttpServletRequest req) {
       this.currentPageNo = req.getParameter("pageNO")!=null && !req.getParameter("pageNO").equals("")?new Integer(req.getParameter("pageNO")).intValue():1;
      /* this.pageSize = 2;
       if(this.pageSize<=0){
    	   this.pageSize=2;
    	   
       }*/
       this.req = req;      
    }
    
    public PageInfo(){
    	this.currentPageNo=1;
    	//this.pageSize=2;
    	
    }
    public PageInfo(int currentPageNo){
    	this.currentPageNo=currentPageNo;
    	//this.pageSize=2;
    	
    }
    /**
     * ������ҳ��
     */
    private void countPages() {
        if(totalResult==0) {
            this.totalPage=1;
        }
        else {
            this.totalPage = (totalResult / pageSize); //�ܹ���ҳ
            if ((totalResult % pageSize) != 0) this.totalPage = this.totalPage + 1;
        }
    }

    public boolean isFirstPage(){
    	
    	if(this.currentPageNo<=1){
    		return true;
    	}else
    	{
    		return false;	
    	}	
    }
    
    public boolean isLastPage(){
    	
    	if(this.currentPageNo>=this.totalPage){
    		
    		return true;
    	}else
    	{
    		return false;	
    	}	
    }
    
    /**
     * ��õ�ǰҳ
     * @return int ��ǰҳ
     */
    public int getCurrentPageNo() {
          return currentPageNo;
    }

    /**
     * ���ÿҳ�ļ�¼��
     * @return int
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * ����ܼ�¼��
     * @return int
     */
    public int getTotalResult() {
        return totalResult;
    }

    /**
     * ���õ�ǰҳ
     * @param current int ��ǰҳ��
     */
    public void setCurrentPageNo(int current) {
        this.currentPageNo  =current;
    }

    /**
     * ����ÿҳ�ļ�¼��
     * @param i int ��¼��
     */
    public void setPageSize(int i) {
        this.pageSize = i;
    }

    /**
     * ��ÿ�ʼ��¼��
     * @return int ��ʼ��¼��
     */
    public int getBeginResult() {
        if(totalPage!=1)
        {	if(currentPageNo>=totalPage){
        		 currentPageNo=totalPage;
        		 beginResult= (currentPageNo - 1) * pageSize;
                 pageSize=totalResult-beginResult;
            }else{
            	beginResult = (currentPageNo - 1) * pageSize;
        	}
        }
        if(totalPage==1)
        {
        	currentPageNo=totalPage;
        	
        	beginResult=0;
        	pageSize=totalResult;
        	
        }
        setRequestValue();   
        return beginResult;
    }

    /**
     * @param i
     */
    public void setBeginResult(int i) {
        this.beginResult = i;
    }

    /**
     * �����ҳ��
     * @return int ��ҳ��
     */
    public int getTotalPage() {
        
    	//log.info("run here totalPage:"+totalPage);
        return totalPage;
        
    }

    /**
     * @param totalResult
     *            The totalResult to set. ���ø÷�ҳ��Ϣ�ܹ��ж�������¼
     */
    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
        countPages();
    }

    /**
     * �������
     */
    private void setRequestValue(){    	
    	page=new PageBean();
    	page.setFirstPage(isFirstPage());
    	page.setLastPage(isLastPage()); 		
        page.setPageNo(currentPageNo);	
    	page.setPageSize(pageSize);
    	page.setSumPage(totalPage);
    	page.setTotalResult(totalResult);

    }
    public PageBean getPageBean(){
    		return page;
    	
    }
    public void setTotalPage(int totalPage){
    	this.totalPage=totalPage;
    }
  
}
