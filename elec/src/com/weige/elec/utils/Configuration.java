package com.weige.elec.utils;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Configuration {

	//�������Ŀ¼λ��
		private static Directory directory;
		//�ִ���
		private static Analyzer analyzer;
		
		static{
			try {
				/**������Ŀ¼ΪD��indexDir*/
				directory = FSDirectory.open(new File("E:/javaEEѧϰ/indexDir/"));
				/**�ʿ�ִ�*/
				analyzer = new IKAnalyzer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static Directory getDirectory() {
			return directory;
		}
		public static Analyzer getAnalyzer() {
			return analyzer;
		}
	
}
