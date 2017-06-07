package com.join.UDisk;

import android.util.Log;

import java.io.File;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 数据转成Excel
 */

public class SaveToExcel {
    private WritableWorkbook wwb;//工作手册(1)
    private String excelPath;
    private File excelFile;

    public SaveToExcel(String excelPath) {
        this.excelPath = excelPath;
        excelFile = new File(excelPath);
        createExcel(excelFile);
    }

    // 创建excel表.
    public void createExcel(File file) {
        WritableSheet ws = null; //工作单(2)
        try {
            if (!file.exists()) {
                wwb = Workbook.createWorkbook(file);

                ws = wwb.createSheet("标题", 0);
                ws.setColumnView(0, 20);//设置单元格的宽
                ws.setColumnView(1, 20);
                ws.setColumnView(2, 20);
                ws.setColumnView(3, 15);
                ws.setColumnView(4, 20);
                ws.setColumnView(5, 20);
                ws.setColumnView(6, 22);
                ws.setColumnView(7, 20);
                ws.setColumnView(8, 15);
                ws.setColumnView(9, 15);
                ws.setColumnView(10, 15);
                ws.setColumnView(11, 15);
                ws.setColumnView(12, 15);
                ws.setColumnView(13, 18);
                ws.setColumnView(14, 18);
                ws.setColumnView(15, 15);

                ws.setRowView(0, 1000);//设置单元格的高

                //字体的设置(4)
                jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("楷书"), 15, WritableFont.NO_BOLD, false);
                wfont.setColour(jxl.format.Colour.BLACK);//设置字体的颜色

                //一个单元格的设置.(3)
                WritableCellFormat wc = new WritableCellFormat(wfont);
                wc.setWrap(true);//如果位置不够的话就自动换行,加\n手动换行
                wc.setAlignment(Alignment.CENTRE); // 设置居中两个一起用才有效果
                wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//把垂直对齐方式指定为居中
                // 设置边框线
                wc.setBorder(Border.ALL, BorderLineStyle.THIN);
                // 设置单元格的背景颜色
                wc.setBackground(jxl.format.Colour.GREEN);
                // 在指定单元格插入数据
                Label lbl1 = new Label(0, 0, "检测日期", wc);
                Label lbl2 = new Label(1, 0, "检测时间", wc);
                Label lbl3 = new Label(2, 0, "样本类型", wc);
                Label lbl4 = new Label(3, 0, "操作员", wc);
                Label lbl5 = new Label(4, 0, "采精日期", wc);
                Label lbl6 = new Label(5, 0, "采精时间", wc);
                Label lbl7 = new Label(6, 0, "公猪编号/" + "\n" + "稀释精液批号", wc);
                Label lbl8 = new Label(7, 0, "采精量" + "\n" + "(ml)", wc);
                Label lbl9 = new Label(8, 0, "颜色", wc);
                Label lbl10 = new Label(9, 0, "气味", wc);
                Label lbl11 = new Label(10, 0, "密度" + "\n" + "(亿/ml)", wc);
                Label lbl12 = new Label(11, 0, "活力", wc);
                Label lbl13 = new Label(12, 0, "活率", wc);
                Label lbl14 = new Label(13, 0, "有效精子数" + "\n" + "(亿)", wc);
                Label lbl15 = new Label(14, 0, "每剂体积" + "\n" + "(ml)", wc);
                Label lbl16 = new Label(15, 0, "结果", wc);

                ws.addCell(lbl1);
                ws.addCell(lbl2);
                ws.addCell(lbl3);
                ws.addCell(lbl4);
                ws.addCell(lbl5);
                ws.addCell(lbl6);
                ws.addCell(lbl7);
                ws.addCell(lbl8);
                ws.addCell(lbl9);
                ws.addCell(lbl10);
                ws.addCell(lbl11);
                ws.addCell(lbl12);
                ws.addCell(lbl13);
                ws.addCell(lbl14);
                ws.addCell(lbl15);
                ws.addCell(lbl16);
                // 从内存中写入文件中
                wwb.write();
                wwb.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //增加数据
    public void writeToExcel(String[] args) {

        try {
            Workbook oldWwb = Workbook.getWorkbook(excelFile);

            wwb = Workbook.createWorkbook(excelFile, oldWwb);
            WritableSheet ws = wwb.getSheet(0);//拿到第一个工作单(2)
            int row = ws.getRows();     // 当前行数
            ws.setRowView(row, 500);//设置单元格的高

            jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("楷书"), 15, WritableFont.NO_BOLD, false);
            wfont.setColour(jxl.format.Colour.BLACK);//设置字体的颜色
            WritableCellFormat wc = new WritableCellFormat(wfont);
            wc.setWrap(true);//如果位置不够的话就自动换行,加\n手动换行
            wc.setAlignment(Alignment.CENTRE); // 设置居中两个一起用才有效果
            wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//把垂直对齐方式指定为居中

            Log.e("jjj", "writeToExcel: " + row);
            Label lab1 = new Label(0, row, args[0], wc);
            Label lab2 = new Label(1, row, args[1], wc);
            Label lab3 = new Label(2, row, args[2], wc);
            Label lab4 = new Label(3, row, args[3], wc);
            Label lab5 = new Label(4, row, args[4], wc);
            Label lab6 = new Label(5, row, args[5], wc);
            Label lab7 = new Label(6, row, args[6], wc);
            Label lab8 = new Label(7, row, args[7], wc);
            Label lab9 = new Label(8, row, args[8], wc);
            Label lab10 = new Label(9, row, args[9], wc);
            Label lab11 = new Label(10, row, args[10], wc);
            Label lab12 = new Label(11, row, args[11], wc);
            Label lab13 = new Label(12, row, args[12], wc);
            Label lab14 = new Label(13, row, args[13], wc);
            Label lab15 = new Label(14, row, args[14], wc);
            Label lab16 = new Label(15, row, args[15], wc);

            ws.addCell(lab1);
            ws.addCell(lab2);
            ws.addCell(lab3);
            ws.addCell(lab4);
            ws.addCell(lab5);
            ws.addCell(lab6);
            ws.addCell(lab7);
            ws.addCell(lab8);
            ws.addCell(lab9);
            ws.addCell(lab10);
            ws.addCell(lab11);
            ws.addCell(lab12);
            ws.addCell(lab13);
            ws.addCell(lab14);
            ws.addCell(lab15);
            ws.addCell(lab16);

            // 从内存中写入文件中,只能刷一次.
            wwb.write();
            wwb.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
