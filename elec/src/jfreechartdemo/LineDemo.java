package jfreechartdemo;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;

import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryPath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class LineDemo {
	public static void main(String[] args) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(8.5,"�й�", "���ز�");
		dataset.setValue(11,"�й�", "����");
		dataset.setValue(6.5,"�й�", "����");
		
		/*String[] columnKeys = { "���ز�", "����","����"};
		double[][] data = new double[][] {{500, 600, 300},{200, 100, 640}};
	    String[] rowKeys = { "�й�", "����"};
	    CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);*/
		
		JFreeChart jFreeChart = ChartFactory.createLineChart(
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
		CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
		//��ȡy�����
		NumberAxis numberAxis =  (NumberAxis) categoryPlot.getRangeAxis();
		//����x���ϵ�����(���������ڣ��Ϻ�)
		categoryAxis.setTickLabelFont(new Font("����",Font.BOLD,15));
		//����x�������(������ı�ǩ��������λ����)
		categoryAxis.setLabelFont(new Font("����",Font.BOLD,15));
		//����y���ϵ�����(1,2,3,4��Щ�̶ȣ��������ֲ�����)
		numberAxis.setTickLabelFont(new Font("����",Font.BOLD,15));
		//����y�������(ֵ��ı�ǩ������)
		numberAxis.setLabelFont(new Font("����",Font.BOLD,15));
		
		//���ÿ̶ȵĵ�λ����Ϊ��ʱ�򣬿̶Ȳ���ΪС���������ʾ������ʱ��
		numberAxis.setAutoTickUnitSelection(false);//�ر��Զ����ã��ֶ�����
		NumberTickUnit unit = new NumberTickUnit(1);
		numberAxis.setTickUnit(unit);
		System.out.println(categoryPlot.getRenderer());
		//��ȡ��ͼ�������
		LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer) categoryPlot.getRenderer();
		//��ͼ��������������
		lineAndShapeRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineAndShapeRenderer.setBaseItemLabelFont(new Font("����",Font.BOLD,12));
		lineAndShapeRenderer.setBaseItemLabelsVisible(true);
		//����ת�۵��ͼ��,����1,��ʾ�ڼ����ߣ�0��ʾ��һ���ߣ��ڶ���������ʾͼ��
		Shape shape = new Rectangle(10, 10);
		lineAndShapeRenderer.setSeriesShape(0, shape);
		lineAndShapeRenderer.setSeriesShapesVisible(0,true);
		//ʹ��Rframe����ͼ��
		ChartFrame chartFrame = new ChartFrame("xyz", jFreeChart);
		chartFrame.setVisible(true);
		//���ͼ��
		chartFrame.pack();
	}
}
