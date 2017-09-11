package com.weige.elec.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.util.NumericUtils;

import com.weige.elec.domain.ElecFileUpload;


public class FileUploadDocument {

	/**��ElecFileUpload����ת����Document����*/
	public static Document FileUploadToDocument(ElecFileUpload elecFileUpload){
		Document document = new Document();
		String seqId = NumericUtils.intToPrefixCoded(elecFileUpload.getSeqId());
		//����ID
		document.add(new Field("seqId",seqId,Store.YES,Index.NOT_ANALYZED));
		//�ļ���
		document.add(new Field("fileName", elecFileUpload.getFileName(), Store.YES, Index.ANALYZED));
		//�ļ�����
		document.add(new Field("comment", elecFileUpload.getComment(), Store.YES, Index.ANALYZED));
		//������λ
		document.add(new Field("projId",elecFileUpload.getProjId(),Store.YES,Index.NOT_ANALYZED));
		//ͼֽ���
		document.add(new Field("belongTo",elecFileUpload.getBelongTo(),Store.YES,Index.NOT_ANALYZED));
		return document;
	}
	
	/**��Document����ת����ElecFileUpload����*/
	public static ElecFileUpload documentToFileUpload(Document document){
		ElecFileUpload elecFileUpload = new ElecFileUpload();
		Integer seqId = NumericUtils.prefixCodedToInt(document.get("seqId"));
		//����ID
		elecFileUpload.setSeqId(seqId);
		//�ļ���
		elecFileUpload.setFileName(document.get("fileName"));
		//�ļ�����
		elecFileUpload.setComment(document.get("comment"));
		//������λ
		elecFileUpload.setProjId(document.get("projId"));
		//ͼֽ���
		elecFileUpload.setBelongTo(document.get("belongTo"));
		return elecFileUpload;
	}
}
