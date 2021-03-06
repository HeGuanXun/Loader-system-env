package com.laohe.excle;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.util.List;

/**
 * @author hegx
 */
public class DbToExcel {

    public static void main(String[] args) {
        try {
               // 创建可写入的Excel工作簿
               String fileName = "d://blackCompany.xls";
               File file=new File(fileName);
               if (!file.exists()) {
                   file.createNewFile();
               }
               //以fileName为文件名来创建一个Workbook
               WritableWorkbook wwb = Workbook.createWorkbook(file);

               // 创建工作表
               WritableSheet ws = wwb.createSheet("企业黑名单", 0);
               
               //查询数据库中所有的数据
               List<Entity> list = BlackCompanyService.getAllByDb();
               //要插入到的Excel表格的行号，默认从0开始
               Label labelId= new Label(0, 0, "编号(id)");
               Label blackCompany= new Label(1, 0, "企业黑户");

               ws.addCell(labelId);
               ws.addCell(blackCompany);

               for (int i = 0; i < list.size(); i++) {
                   Label labelName_i= new Label(1, i+1, list.get(i).getBlackCompany());
                   Label index = new Label(0,i+1, list.get(i).getId().toString());
                   ws.addCell(index);
                   ws.addCell(labelName_i);
               }
             
              //写进文档
               wwb.write();
              // 关闭Excel工作簿对象
               wwb.close();
             
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}