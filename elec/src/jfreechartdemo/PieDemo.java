package jfreechartdemo;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryPath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;

public class PieDemo {
	public static void main(String[] args) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("�й�",8.5);
		dataset.setValue("����",11);
		dataset.setValue("�¹�",20);
		
		/*String[] columnKeys = { "���ز�", "����","����"};
		double[][] data = new double[][] {{500, 600, 300},{200, 100, 640}};
	    String[] rowKeys = { "�й�", "����"};
	    CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);*/
		
		JFreeChart jFreeChart = ChartFactory.createPieChart3D(
				"�û�ͳ�Ʊ���������λ��",           //ͼ�ε�������
				dataset, 			         //ͼ�ε����ݼ���
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
		PiePlot3D piePlot3D = (PiePlot3D) jFreeChart.getPlot();
		//����ͼ���ϵ���ʾ����
		piePlot3D.setLabelFont(new Font("����",Font.BOLD,15));
		//��ͼ������ʾ�ٷֱ�(��ʽ:����(60%))
		String labelFormat = "{0} {1} ({2})";
		piePlot3D.setLabelGenerator(new StandardPieSectionLabelGenerator(labelFormat));
		/*//ʹ��Rframe����ͼ��
		ChartFrame chartFrame = new ChartFrame("xyz", jFreeChart);
		chartFrame.setVisible(true);
		//���ͼ��
		chartFrame.pack();*/
		File file = new File("E:/javaEEѧϰ/chart.png");
		try {
			ChartUtilities.saveChartAsPNG(file, jFreeChart, 400,400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**��Ӳ������һ��ͼƬ*/
	}
}
