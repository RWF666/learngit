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

	/**向索引库中添加数据*/
	public static void addIndex(ElecFileUpload fileUpload) {
		//将javabean对象转换成document对象
		Document document = FileUploadDocument.FileUploadToDocument(fileUpload);
		/**新增，修改，查询，删除都需要使用分词器*/
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, Configuration.getAnalyzer());
			IndexWriter indexWriter = new IndexWriter(Configuration.getDirectory(), indexWriterConfig);
			//添加数据
			indexWriter.addDocument(document);
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**使用查询条件搜索索引库的数据，返回List<elelFileUpload>*/
	/**
	 * 
	 * @param projId   所属单位
	 * @param belongTo   图纸类别
	 * @param queryString   文件名和文件描述
	 * @return
	 */
	public static List<ElecFileUpload> searcherIndexByConditon(String projId,
			String belongTo, String queryString) {
		//最终结果集
		List<ElecFileUpload> fileUploadlist = new ArrayList<ElecFileUpload>();
		try {
			IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(Configuration.getDirectory()));
			//封装查询条件(使用BooleanQuery对象连接条件查询)
			BooleanQuery query = new BooleanQuery();
			//条件一
			if(StringUtils.isNotEmpty(projId)){
				//词条查询
				TermQuery query1 = new TermQuery(new Term("projId", projId));
				query.add(query1,Occur.MUST);//Occur.MUST相当于sql的and
			}
			//条件二
			if(StringUtils.isNotEmpty(belongTo)){
				//词条查询
				TermQuery query2 = new TermQuery(new Term("belongTo", belongTo));
				query.add(query2,Occur.MUST);
			}
			//条件三
			if(StringUtils.isNotEmpty(queryString)){
				//多个字段进行检索的时候，使用QueryPaser
				QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, new String[]{"fileName","comment"}, Configuration.getAnalyzer());
				Query query3 =  queryParser.parse(queryString);
				query.add(query3,Occur.MUST);
			}
			//向索引库中搜索数据
			/**
			 * 参数一 :指定的查询条件(lucene写法)
			 * 参数二 :返回前100
			 */
			TopDocs topDocs = indexSearcher.search(query, 100);
			//System.out.println("查询的总记录数："+topDocs.totalHits);//查询的总记录数
			//表示返回的结果集
			ScoreDoc[] scoreDocs =  topDocs.scoreDocs;
			/**添加设置文字高亮 begin*/
			//html页面显示的格式化 ，默认是<b></b>
			Formatter formatter = new SimpleHTMLFormatter("<font color='red'><b>","</b></font>");
			//指定查询条件，因为高亮的值就是查询条件
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(formatter,scorer);
			//设置文字的摘要
			int fragmenterSize = 30;//设置文字摘要的长度
			Fragmenter fragmenter = new SimpleFragmenter(fragmenterSize);
			highlighter.setTextFragmenter(fragmenter);
			/**添加设置文字高亮 end*/
			
			if(scoreDocs!=null && scoreDocs.length>0){
				for(ScoreDoc scoreDoc:scoreDocs){
					//System.out.println("相关度得分:"+scoreDoc.score);//默认得分高的数据在前面
					//获取当前查询文档的唯一编号
					int doc = scoreDoc.doc;
					//真正的数据
					Document document = indexSearcher.doc(doc);
					
					/**获取文字高亮的信息begin*/
					//获取文字高亮的信息，一次只能获取一个高亮的结果，如果获取不到，返回null值
					String fileName = highlighter.getBestFragment(Configuration.getAnalyzer(),"fileName", document.get("fileName"));
					String comment = highlighter.getBestFragment(Configuration.getAnalyzer(),"comment", document.get("comment"));
					//将高亮后的结果放到document中去
					document.getField("fileName").setValue(fileName);
					document.getField("comment").setValue(comment);
					/**获取文字高亮的信息end*/
					
					//将document对象转换成javabean
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
