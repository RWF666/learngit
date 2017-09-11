/**
 * ϵͳ���ݵ���Excel ������
 * @version 1.0
 */
package com.weige.elec.utils;

import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelFileGenerator {

	private final int SPLIT_COUNT = 15; //Excelÿ��������������

	private ArrayList<String> fieldName = null; //excel�������ݼ�

	private ArrayList<ArrayList<String>> fieldData = null; //excel��������	

	private HSSFWorkbook workBook = null;

	/**
	 * ������
	 * @param fieldName ��������ֶ���
	 * @param data
	 */
	public ExcelFileGenerator(ArrayList<String> fieldName, ArrayList<ArrayList<String>> fieldData) {

		this.fieldName = fieldName;
		this.fieldData = fieldData;
	}

	/**
	 * ����HSSFWorkbook����
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createWorkbook() {

		workBook = new HSSFWorkbook();//����һ������������
		int rows = fieldData.size();//�ܵļ�¼��
		int sheetNum = 0;           //ָ��sheet��ҳ��

		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}

		for (int i = 1; i <= sheetNum; i++) {//ѭ��2��sheet��ֵ
			HSSFSheet sheet = workBook.createSheet("Page " + i);//ʹ��workbook���󴴽�sheet����
			HSSFRow headRow = sheet.createRow((short) 0); //�����У�0��ʾ��һ�У�������excel�ı��⣩
			for (int j = 0; j < fieldName.size(); j++) {//ѭ��excel�ı���
				HSSFCell cell = headRow.createCell( j);//ʹ���ж��󴴽��ж���0��ʾ��1��
				/**************�Ա��������ʽbegin********************/
				
				//�����еĿ��/
				sheet.setColumnWidth(j, 6000);
				HSSFCellStyle cellStyle = workBook.createCellStyle();//�����е���ʽ����
				HSSFFont font = workBook.createFont();//�����������
				//����Ӵ�
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				//������ɫ���
				font.setColor(HSSFColor.RED.index);
				//���font�д������ú�����壬�����õ�cellStyle�����У���ʱ�õ�Ԫ���о;�������ʽ����
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//���þ���
				cellStyle.setFont(font);
				
				/**************�Ա��������ʽend********************/
				
				//�����ʽ
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if(fieldName.get(j) != null){
					//�������õ���ʽ���õ���Ӧ�ĵ�Ԫ����
					cell.setCellStyle(cellStyle);
					cell.setCellValue((String) fieldName.get(j));//Ϊ�����еĵ�Ԫ������ֵ
				}else{
					cell.setCellValue("-");
				}
			}
			//��ҳ����excel�����ݣ��������еĽ��
			for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
				if (((i - 1) * SPLIT_COUNT + k) >= rows)//������ݳ����ܵļ�¼����ʱ�򣬾��˳�ѭ��
					break;
				HSSFRow row = sheet.createRow((short) (k + 1));//����1��
				//��ҳ������ȡÿҳ�Ľ�����������������ݷ���excel��Ԫ��
				ArrayList<String> rowList = (ArrayList<String>) fieldData.get((i - 1) * SPLIT_COUNT + k);
				for (int n = 0; n < rowList.size(); n++) {//����ĳһ�еĽ��
					HSSFCell cell = row.createCell( n);//ʹ���д����ж���
					HSSFCellStyle cellStyle = workBook.createCellStyle();//�����е���ʽ����
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//���þ���
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(cellStyle);
					if(rowList.get(n) != null){
						cell.setCellValue((String) rowList.get(n).toString());
					}else{
						cell.setCellValue("");
					}
				}
			}
		}
		return workBook;
	}

	public void expordExcel(OutputStream os) throws Exception {
		workBook = createWorkbook();
		workBook.write(os);//��excel�е�����д��������У������ļ������
		os.close();
	}

}
