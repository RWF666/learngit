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
		dataset.setValue(8.5,"中国", "房地产");
		dataset.setValue(11,"中国", "汽车");
		dataset.setValue(6.5,"中国", "电器");
		
		/*String[] columnKeys = { "房地产", "汽车","旅游"};
		double[][] data = new double[][] {{500, 600, 300},{200, 100, 640}};
	    String[] rowKeys = { "中国", "美国"};
	    CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);*/
		
		JFreeChart jFreeChart = ChartFactory.createLineChart(
				"用户统计报表（所属单位）",           //图形的主标题
				"所属单位名称",                   //种类轴的标签
				"数量",                        //值轴的标签
				dataset, 			         //图形的数据集合
				PlotOrientation.VERTICAL,    //图形的显示形式
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
		CategoryPlot categoryPlot = (CategoryPlot) jFreeChart.getPlot();
		//获取x轴对象 
		CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
		//获取y轴对象
		NumberAxis numberAxis =  (NumberAxis) categoryPlot.getRangeAxis();
		//处理x轴上的乱码(北京，深圳，上海)
		categoryAxis.setTickLabelFont(new Font("宋体",Font.BOLD,15));
		//处理x轴外的乱(种类轴的标签：所属单位名称)
		categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,15));
		//处理y轴上的乱码(1,2,3,4这些刻度，这里体现不出来)
		numberAxis.setTickLabelFont(new Font("宋体",Font.BOLD,15));
		//处理y轴外的乱(值轴的标签：数量)
		numberAxis.setLabelFont(new Font("宋体",Font.BOLD,15));
		
		//设置刻度的单位，因为有时候，刻度不能为小数，比如表示人数的时候
		numberAxis.setAutoTickUnitSelection(false);//关闭自动设置，手动设置
		NumberTickUnit unit = new NumberTickUnit(1);
		numberAxis.setTickUnit(unit);
		System.out.println(categoryPlot.getRenderer());
		//获取绘图区域对象
		LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer) categoryPlot.getRenderer();
		//在图形上面生成数字
		lineAndShapeRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineAndShapeRenderer.setBaseItemLabelFont(new Font("宋体",Font.BOLD,12));
		lineAndShapeRenderer.setBaseItemLabelsVisible(true);
		//设置转折点的图形,参数1,表示第几条线，0表示第一条线，第二个参数表示图形
		Shape shape = new Rectangle(10, 10);
		lineAndShapeRenderer.setSeriesShape(0, shape);
		lineAndShapeRenderer.setSeriesShapesVisible(0,true);
		//使用Rframe加载图形
		ChartFrame chartFrame = new ChartFrame("xyz", jFreeChart);
		chartFrame.setVisible(true);
		//输出图形
		chartFrame.pack();
	}
}
