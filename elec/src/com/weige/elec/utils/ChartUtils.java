package com.weige.elec.utils;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartUtils {
	
	/**��״ͼ*/
	public static String createBarChart(List<Object[]> list){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if(list!=null && list.size()>0){
			for(Object[] o:list){
				dataset.setValue(Double.parseDouble(o[2].toString()),o[0].toString(),o[1].toString());
			}
			
		}
		
		
		/*String[] columnKeys = { "���ز�", "����","����"};
		double[][] data = new double[][] {{500, 600, 300},{200, 100, 640}};
	    String[] rowKeys = { "�й�", "����"};
	    CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);*/
		
		JFreeChart jFreeChart = ChartFactory.createBarChart3D(
				"�û�ͳ�Ʊ���������λ��",           //ͼ�ε�������
				"������λ����",                   //������ı�ǩ
				"����",                        //ֵ��ı�ǩ
				dataset, 			         //ͼ�ε����ݼ���
				PlotOrientation.VERTICAL,    //ͼ�ε���ʾ��ʽ
				true,					     //�Ƿ���ʾ�ӱ���
				true,                       //�Ƿ�����������ʾ
				true						 //�Ƿ�����url����
		); 
		//��������������
		jFreeChart.getTitle().setFont(new Font("����",Font.BOLD,18));
		//�����ӱ��������
		jFreeChart.getLegend().setItemFont(new Font("����",Font.BOLD,15));
		//��ȡͼ���������
		/**
		 * ���ڲ�ͬ���͵ķ���ֵ����һ�������Կ��������ַ�ʽ��ȡ����
		 * 	1���ϵ㷽ʽ
		 * 	2��sys��ʽ
		 * 	3���鿴API
		 */
		CategoryPlot categoryPlot = (CategoryPlot) jFreeChart.getPlot();
		//��ȡx����� 
		CategoryAxis3D categoryAxis3D = (CategoryAxis3D) categoryPlot.getDomainAxis();
		//��ȡy�����
		NumberAxis3D numberAxis3D =  (NumberAxis3D) categoryPlot.getRangeAxis();
		//����x���ϵ�����(���������ڣ��Ϻ�)
		categoryAxis3D.setTickLabelFont(new Font("����",Font.BOLD,15));
		//����x�������(������ı�ǩ��������λ����)
		categoryAxis3D.setLabelFont(new Font("����",Font.BOLD,15));
		//����y���ϵ�����(1,2,3,4��Щ�̶ȣ��������ֲ�����)
		numberAxis3D.setTickLabelFont(new Font("����",Font.BOLD,15));
		//����y�������(ֵ��ı�ǩ������)
		numberAxis3D.setLabelFont(new Font("����",Font.BOLD,15));
		
		//���ÿ̶ȵĵ�λ����Ϊ��ʱ�򣬿̶Ȳ���ΪС���������ʾ������ʱ��
		numberAxis3D.setAutoTickUnitSelection(false);//�ر��Զ����ã��ֶ�����
		NumberTickUnit unit = new NumberTickUnit(1);
		numberAxis3D.setTickUnit(unit);
		
		//��ȡ��ͼ�������
		BarRenderer3D barRenderer3D = (BarRenderer3D) categoryPlot.getRenderer();
		//������״ͼ�Ŀ��
		barRenderer3D.setMaximumBarWidth(0.06);
		//��ͼ��������������
		barRenderer3D.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		barRenderer3D.setBaseItemLabelFont(new Font("����",Font.BOLD,12));
		barRenderer3D.setBaseItemLabelsVisible(true);
		
		String filename = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+".png";
		/**��chart�ļ�����������ͼƬ��ͼƬ���Ʋ���ʱ�����Ϊ�ļ�������ʾΨһ*/
		File file = new File(ServletActionContext.getServletContext().getRealPath("/chart")+"/"+filename);
		try {
			ChartUtilities.saveChartAsPNG(file, jFreeChart, 400,400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filename;
	}
}
