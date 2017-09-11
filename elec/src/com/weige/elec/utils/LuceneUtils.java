package com.weige.elec.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import com.weige.elec.domain.ElecFileUpload;

public class LuceneUtils {

	/**�����������������*/
	public static void addIndex(ElecFileUpload fileUpload) {
		//��javabean����ת����document����
		Document document = FileUploadDocument.FileUploadToDocument(fileUpload);
		/**�������޸ģ���ѯ��ɾ������Ҫʹ�÷ִ���*/
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, Configuration.getAnalyzer());
			IndexWriter indexWriter = new IndexWriter(Configuration.getDirectory(), indexWriterConfig);
			//�������
			indexWriter.addDocument(document);
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**ʹ�ò�ѯ������������������ݣ�����List<elelFileUpload>*/
	/**
	 * 
	 * @param projId   ������λ
	 * @param belongTo   ͼֽ���
	 * @param queryString   �ļ������ļ�����
	 * @return
	 */
	public static List<ElecFileUpload> searcherIndexByConditon(String projId,
			String belongTo, String queryString) {
		//���ս����
		List<ElecFileUpload> fileUploadlist = new ArrayList<ElecFileUpload>();
		try {
			IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(Configuration.getDirectory()));
			//��װ��ѯ����(ʹ��BooleanQuery��������������ѯ)
			BooleanQuery query = new BooleanQuery();
			//����һ
			if(StringUtils.isNotEmpty(projId)){
				//������ѯ
				TermQuery query1 = new TermQuery(new Term("projId", projId));
				query.add(query1,Occur.MUST);//Occur.MUST�൱��sql��and
			}
			//������
			if(StringUtils.isNotEmpty(belongTo)){
				//������ѯ
				TermQuery query2 = new TermQuery(new Term("belongTo", belongTo));
				query.add(query2,Occur.MUST);
			}
			//������
			if(StringUtils.isNotEmpty(queryString)){
				//����ֶν��м�����ʱ��ʹ��QueryPaser
				QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, new String[]{"fileName","comment"}, Configuration.getAnalyzer());
				Query query3 =  queryParser.parse(queryString);
				query.add(query3,Occur.MUST);
			}
			//������������������
			/**
			 * ����һ :ָ���Ĳ�ѯ����(luceneд��)
			 * ������ :����ǰ100
			 */
			TopDocs topDocs = indexSearcher.search(query, 100);
			//System.out.println("��ѯ���ܼ�¼����"+topDocs.totalHits);//��ѯ���ܼ�¼��
			//��ʾ���صĽ����
			ScoreDoc[] scoreDocs =  topDocs.scoreDocs;
			/**����������ָ��� begin*/
			//htmlҳ����ʾ�ĸ�ʽ�� ��Ĭ����<b></b>
			Formatter formatter = new SimpleHTMLFormatter("<font color='red'><b>","</b></font>");
			//ָ����ѯ��������Ϊ������ֵ���ǲ�ѯ����
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(formatter,scorer);
			//�������ֵ�ժҪ
			int fragmenterSize = 30;//��������ժҪ�ĳ���
			Fragmenter fragmenter = new SimpleFragmenter(fragmenterSize);
			highlighter.setTextFragmenter(fragmenter);
			/**����������ָ��� end*/
			
			if(scoreDocs!=null && scoreDocs.length>0){
				for(ScoreDoc scoreDoc:scoreDocs){
					//System.out.println("��ضȵ÷�:"+scoreDoc.score);//Ĭ�ϵ÷ָߵ�������ǰ��
					//��ȡ��ǰ��ѯ�ĵ���Ψһ���
					int doc = scoreDoc.doc;
					//����������
					Document document = indexSearcher.doc(doc);
					
					/**��ȡ���ָ�������Ϣbegin*/
					//��ȡ���ָ�������Ϣ��һ��ֻ�ܻ�ȡһ�������Ľ���������ȡ����������nullֵ
					String fileName = highlighter.getBestFragment(Configuration.getAnalyzer(),"fileName", document.get("fileName"));
					String comment = highlighter.getBestFragment(Configuration.getAnalyzer(),"comment", document.get("comment"));
					//��������Ľ���ŵ�document��ȥ
					document.getField("fileName").setValue(fileName);
					document.getField("comment").setValue(comment);
					/**��ȡ���ָ�������Ϣend*/
					
					//��document����ת����javabean
					ElecFileUpload elecFileUpload = FileUploadDocument.documentToFileUpload(document);
					fileUploadlist.add(elecFileUpload);
				}
			}
			indexSearcher.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUploadlist;
	}
}
