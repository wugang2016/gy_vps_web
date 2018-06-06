package com.bj.job.rw;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.bj.pojo.SubSystemInfo;

/**
 * Created by liqingkun on 2017-10-24.
 */
public class EcueExcelReader implements Closeable {
    private static final short COLUMN_NAME = 0;
    private static final short COLUMN_IP = 1;
    private static final short COLUMN_PORT = 2;
    private static final short COLUMN_BOX_IP = 3;
    private static final short COLUMN_C_WIDTH = 4;
    private static final short COLUMN_C_HEIGHT = 5;
    private static final short COLUMN_WIDTH = 6;
    private static final short COLUMN_HEIGHT = 7;

    private static final String NAME = "设备名称";
    private static final String IP = "IP";
    private static final String PORT = "端口";
    private static final String BOX_IP = "盒子IP";
    private static final String C_WIDTH = "源视频宽度";
    private static final String C_HEIGHT = "源视频高度";
    private static final String WIDTH = "实际视频宽度";
    private static final String HEIGHT = "实际视频高度";
    private final boolean titlesMatched;
    private final Sheet sheet;

    //HSSFWorkbook
    Workbook workbook = null;
    //当前sheet
    private int currSheet;
    //当前位置
    private int currRow;
    //sheet数量
    private int numOfSheets;
    //信息
    private String message;

    //构造函数创建一个ExcelReader
    public EcueExcelReader(InputStream input) throws IOException, InvalidFormatException, EncryptedDocumentException {
        //设置开始行为0
        currRow = 0;
        //设置当前位置为0
        currSheet = 0;
        //如果是Excel文件则创建HSSFWorkbook读取
        
        //workbook = new HSSFWorkbook(is, false); //此方法只使用office2003
        workbook = WorkbookFactory.create(input);
        //设置sheet数
        numOfSheets = workbook.getNumberOfSheets();
        if (numOfSheets > 0) {
            sheet = workbook.getSheetAt(0);
        } else {
            throw new IOException("Oops! Invalid excel file");
        }

        List<String> titleLine = getLine(sheet, 0);
        currRow = 1;
        
        // 检查列标题是否匹配
        String titleName = titleLine.get(COLUMN_NAME);
        String titleIp = titleLine.get(COLUMN_IP);
        String titlePort = titleLine.get(COLUMN_PORT);
        String titleBoxIp = titleLine.get(COLUMN_BOX_IP);
        String titleCWidth = titleLine.get(COLUMN_C_WIDTH);
        String titleCHeight = titleLine.get(COLUMN_C_HEIGHT);
        String titleWidth = titleLine.get(COLUMN_WIDTH);
        String titleHeight = titleLine.get(COLUMN_HEIGHT);

        titlesMatched = NAME.equals(titleName) &&
        		IP.equals(titleIp) &&
        		PORT.equals(titlePort) &&
        		BOX_IP.equals(titleBoxIp) &&
        		C_WIDTH.equals(titleCWidth) &&
        		C_HEIGHT.equals(titleCHeight) &&
        		WIDTH.equals(titleWidth) &&
        		HEIGHT.equals(titleHeight);
    }

    //函数readLine读取文本的一行
    public SubSystemInfo readLine() throws IOException,NumberFormatException {
        // 如果列标题不匹配，则跳过
        if (!titlesMatched) {
        	this.message = "解析失败，表头不对应";
            return null;
        }
        //根据currSheet值获得当前的sheet
        Sheet sheet = workbook.getSheetAt(currSheet);
        //判断当前行是否到当前sheet的结尾
        if (currRow > sheet.getLastRowNum()) {
            return null;
        }
        //获取当前行数
        int row = currRow;
        currRow++;
        //读取当前行数据
        List<String> line = getLine(sheet, row);
        SubSystemInfo ecue = new SubSystemInfo();
        ecue.setName(line.get(COLUMN_NAME));
        ecue.setIp(line.get(COLUMN_IP));
        ecue.setPort(Integer.parseInt(line.get(COLUMN_PORT)));
        ecue.setBoxIp(line.get(COLUMN_BOX_IP));
        ecue.setContent_width(Integer.parseInt(line.get(COLUMN_C_WIDTH)));
        ecue.setContent_height(Integer.parseInt(line.get(COLUMN_C_HEIGHT)));
        ecue.setWidth(Integer.parseInt(line.get(COLUMN_WIDTH)));
        ecue.setHeight(Integer.parseInt(line.get(COLUMN_HEIGHT)));
        return ecue;
    }

    //函数getLine返回sheet的一行数据
    private List<String> getLine(Sheet sheet, int row) {
        //根据行数取得sheet的一行
        Row rowLine = sheet.getRow(row);
        //获取挡前行的列数
        int filledColumns = rowLine.getLastCellNum();
        if (filledColumns < COLUMN_HEIGHT + 1) {
            filledColumns = COLUMN_HEIGHT + 1;
        }
        Cell cell = null;
        //循环遍历所有列
        List<String> cellValues = new ArrayList<>(filledColumns);
        for (int i = 0; i < filledColumns; i++) {
            //取得当前cell
            cell = rowLine.getCell(i);
            String cellValue = null;
            if (null != cell) {
            	cellValue = getStringCellValue(cell);
            	System.out.println("cellValue="+cellValue);
                if ((i == 2 || i >= 4) && cellValue.indexOf(".") != -1) {
                	cellValue = cellValue.split("\\.")[0];
                }
            } else {
                cellValue = "";
            }
            cellValues.add(cellValue);
        }
        //以字符串返回该行的数据
        return cellValues;
    }

    private String getStringCellValue(Cell cell) {
        String value = null;
        if (null != cell) {
            // Set the cell data value
            switch (cell.getCellTypeEnum()) {
                case BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case ERROR:
                    value = "";
                    break;
                case NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue());
                    break;
                case BLANK:
                case STRING:
                case FORMULA:
                default:
                    value = cell.getStringCellValue();
                    break;
            }
        }
        if (value == null) {
            return "";
        }
        return value;
    }
    
    @Override
    public void close() throws IOException {
        workbook.close();
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
