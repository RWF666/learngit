package com.weige.elec.domain;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;




public class ElecFileUpload  implements Serializable {
	 // Fields    
    private Integer seqId;         //����ID
    private String projId;			//����ID/������λ
    private String belongTo;		//����ģ��/ͼֽ���
    private String fileName;		//�ļ���
    private String fileUrl;		//�ļ��ϴ���·��
    private String progressTime;	//�ϴ�ʱ��
    private String comment;		//�ļ�����

   public Integer getSeqId() {
       return this.seqId;
   }
   
   public void setSeqId(Integer seqId) {
       this.seqId = seqId;
   }

   public String getProjId() {
       return this.projId;
   }
   
   public void setProjId(String projId) {
       this.projId = projId;
   }

   public String getBelongTo() {
       return this.belongTo;
   }
   
   public void setBelongTo(String belongTo) {
       this.belongTo = belongTo;
   }

   public String getFileName() {
       return this.fileName;
   }
   
   public void setFileName(String fileName) {
       this.fileName = fileName;
   }

   public String getFileUrl() {
       return this.fileUrl;
   }
   
   public void setFileUrl(String fileUrl) {
       this.fileUrl = fileUrl;
   }

   public String getProgressTime() {
       return this.progressTime;
   }
   
   public void setProgressTime(String progressTime) {
       this.progressTime = progressTime;
   }

   public String getComment() {
       return this.comment;
   }
   
   public void setComment(String comment) {
       this.comment = comment;
   }

   /**�ǳ־û�javabean������*/
   //ҳ���ϣ����ļ����ƺ�������ѯ
   private String queryString;
   
   //�ļ��ϴ���File
   private File [] uploads;
   //�ļ��ϴ����ļ���
   private String [] uploadsFileName;
   //�ϴ��ļ�������
   private String [] comments;
   
   //�ļ�����
   private InputStream inputStream;
   
   private String hideProjId; 
   private String hideBelongTo;
   private String hideQueryString;
   private Integer hideId;
   
	public Integer getHideId() {
		return hideId;
	}

	public void setHideId(Integer hideId) {
		this.hideId = hideId;
	}

	public String getHideProjId() {
		return hideProjId;
	}

	public void setHideProjId(String hideProjId) {
		this.hideProjId = hideProjId;
	}

	public String getHideBelongTo() {
		return hideBelongTo;
	}

	public void setHideBelongTo(String hideBelongTo) {
		this.hideBelongTo = hideBelongTo;
	}

	public String getHideQueryString() {
		return hideQueryString;
	}

	public void setHideQueryString(String hideQueryString) {
		this.hideQueryString = hideQueryString;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public File[] getUploads() {
		return uploads;
	}

	public void setUploads(File[] uploads) {
		this.uploads = uploads;
	}

	public String[] getUploadsFileName() {
		return uploadsFileName;
	}

	public void setUploadsFileName(String[] uploadsFileName) {
		this.uploadsFileName = uploadsFileName;
	}

	public String[] getComments() {
		return comments;
	}

	public void setComments(String[] comments) {
		this.comments = comments;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
}