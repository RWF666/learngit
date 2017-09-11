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
		dataset.setValue("中国",8.5);
		dataset.setValue("美国",11);
		dataset.setValue("德国",20);
		
		/*String[] columnKeys = { "房地产", "汽车","旅游"};
		double[][] data = new double[][] {{500, 600, 300},{200, 100, 640}};
	    String[] rowKeys = { "中国", "美国"};
	    CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);*/
		
		JFreeChart jFreeChart = ChartFactory.createPieChart3D(
				"用户统计报表（所属单位）",           //图形的主标题
				dataset, 			         //图形的数据集合
				true,					     //是否显示子标题
				true,                       //是否生成数据提示
				true						 //是否生成url连接
		); 
		//处理主标题乱码
		jFreeChart.getTitle().setFont(new Font("宋体",Font.BOLD,18));
		//处理子标题的乱码
		jFreeChart.getLegend().setItemFont(new Font("宋体",Font.BOLD,15));
		
		//获取图标区域对象
		/**
		 * 由于不同类型的返回值对象不一样，所以可以用三种方式获取对象
		 * 	1、断点方式
		 * 	2、sys方式
		 * 	3、查看API
		 */
		PiePlot3D piePlot3D = (PiePlot3D) jFreeChart.getPlot();
		//处理图形上的显示乱码
		piePlot3D.setLabelFont(new Font("宋体",Font.BOLD,15));
		//在图形上显示百分比(格式:北京(60%))
		String labelFormat = "{0} {1} ({2})";
		piePlot3D.setLabelGenerator(new StandardPieSectionLabelGenerator(labelFormat));
		/*//使用Rframe加载图形
		ChartFrame chartFrame = new ChartFrame("xyz", jFreeChart);
		chartFrame.setVisible(true);
		//输出图形
		chartFrame.pack();*/
		File file = new File("E:/javaEE学习/chart.png");
		try {
			ChartUtilities.saveChartAsPNG(file, jFreeChart, 400,400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**在硬盘生成一张图片*/
	}
}
