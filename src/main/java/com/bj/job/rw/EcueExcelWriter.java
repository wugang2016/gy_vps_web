package com.bj.job.rw;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.bj.pojo.SubSystemInfo;

/**
 * 写入数据到Excel
 */
public class EcueExcelWriter {
    private final File outputFile;
    private HSSFWorkbook workbook;
    private Sheet sheet;
    private int rowNum;

    public EcueExcelWriter(File file) {
        this.outputFile = file;
    }

    public void write(List<SubSystemInfo> ecues) throws IOException {
        if (workbook == null) {
            init();
        }
        for (SubSystemInfo ecue : ecues) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0, CellType.STRING).setCellValue(ecue.getName());
            row.createCell(1, CellType.STRING).setCellValue(ecue.getIp());
            row.createCell(2, CellType.STRING).setCellValue(ecue.getPort());
            row.createCell(3, CellType.STRING).setCellValue(ecue.getBoxIp());
            row.createCell(4, CellType.STRING).setCellValue(ecue.getContent_width()==null?0:ecue.getContent_width());
            row.createCell(5, CellType.STRING).setCellValue(ecue.getContent_height()==null?0:ecue.getContent_height());
            row.createCell(6, CellType.STRING).setCellValue(ecue.getWidth());
            row.createCell(7, CellType.STRING).setCellValue(ecue.getHeight());
        }
    }

    private void init() throws IOException {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();

        // 创建表头（第一行）
        rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        // 序号										联行行号
        row.createCell(0, CellType.STRING).setCellValue("设备名称");
        row.createCell(1, CellType.STRING).setCellValue("IP");
        row.createCell(2, CellType.STRING).setCellValue("端口");
        row.createCell(3, CellType.STRING).setCellValue("盒子IP");
        row.createCell(4, CellType.STRING).setCellValue("源视频宽度");
        row.createCell(5, CellType.STRING).setCellValue("源视频高度");
        row.createCell(6, CellType.STRING).setCellValue("实际视频宽度");
        row.createCell(7, CellType.STRING).setCellValue("实际视频高度");
        //sheet.createFreezePane(4, 1);
    }

    public void close() throws IOException {
        if (workbook != null) {
            workbook.write(outputFile);
            workbook.close();
            workbook = null;
        }
    }
}
