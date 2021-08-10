package com.zero.common.util;


import java.io.IOException;
import java.util.List;

/**
 * Excel工具类：到处Excel表
 */
@Component
public class ExcelUtil {
    public void exportExcel(HttpServletResponse response,
                                   String [] header,
                                   List<List<String>> excelData,
                                   String sheetName,
                                   String fileName,
                                   int columnWidth) throws IOException {
        //声明一个工作簿
        HSSFWorkbook workbook=new HSSFWorkbook();
        //生成一个表格，设置表格名称
        HSSFSheet sheet=workbook.createSheet(sheetName);
        //设置表格列宽度
        sheet.setDefaultColumnWidth(columnWidth);
        //写入List<List<String>>中的数据
        //1.表头数据
            //创建第一行表头
            HSSFRow headerow=sheet.createRow(0);
            //遍历添加表头
            for (int i = 0; i < header.length; i++) {
                //创建一个单元格
                HSSFCell cell=headerow.createCell(i);
                //创建一个内容对象
                HSSFRichTextString text=new HSSFRichTextString(header[i]);
                //将内容对象的文字内容写道单元格中
                cell.setCellValue(text);
            }
        //2.表中数据
        int rowIndex=1;
        for (List<String> data:excelData){
            //创建一个row行，然后自增1
            HSSFRow row = sheet.createRow(rowIndex++);
            //遍历添加本行数据
            for (int i=0;i<data.size();i++){
                //创建一个单元格
                HSSFCell cell=row.createCell(i);
                //创建一个内容对象
                HSSFRichTextString text=new HSSFRichTextString(data.get(i));
                //将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }
        }
        //准备将Excel的输入流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");
        //设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + fileName+".xls");
        //刷新缓冲
        response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载该Excel文件
        workbook.write(response.getOutputStream());
        //关闭workbook
        workbook.close();
    }
}
