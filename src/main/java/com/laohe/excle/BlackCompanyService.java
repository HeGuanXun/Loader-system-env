package com.laohe.excle;

import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author blackcompany
 * @date 2017-06-29
 *
 */
public class BlackCompanyService {
    /**
     * 查询blackCompany表中所有的数据
     * @return 
     */
    public static List<Entity> getAllByDb(){
        List<Entity> list=new ArrayList<>();
        try {
            DataBaseUtils db=new DataBaseUtils();
            String sql="select * from t_black_company";
            ResultSet rs= db.Search(sql, null);
            while (rs.next()) {
                String blackCompany=rs.getString("blackCompany");
                list.add(new Entity(blackCompany));
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * 查询指定目录中电子表格中所有的数据
     * @param file 文件完整路径
     * @return
     */
    public static List<Entity> getAllByExcel(String file){
        List<Entity> list=new ArrayList<>();
        try {
            Workbook rwb=Workbook.getWorkbook(new File(file));

            Sheet rs = rwb.getSheet(0);
            //得到所有的列
            int columns=rs.getColumns();
            //得到所有的行
            int rows=rs.getRows();
            
            System.out.println(columns+" rows:"+rows);

            for (int i = 0; i < rows; i++)
            {
                //默认最左边编号也算一列 所以这里得j++
                String blackCompany = rs.getCell(0, i).getContents();
                System.out.println(" blackCompany:"+blackCompany);
                if (StringUtils.isEmpty(blackCompany))
                {
                    System.out.println("null");
                    continue;
                }
                list.add(new Entity(blackCompany));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return list;
        
    }

    /**
     * 通过blackCompany判断是否存在
     * @param blackCompany
     * @return
     */
    public static boolean isExist(String blackCompany){
        try {
            DataBaseUtils db=new DataBaseUtils();
            ResultSet rs=db.Search("select * from t_black_company where blackCompany=?", new String[]{blackCompany});
            if (rs.next()) {
                System.out.println(blackCompany+"-------------------------------->已经存在");
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public static void main(String[] args) {
        List<Entity> all=getAllByDb();
        for (Entity entity : all) {
            System.out.println(entity.toString());
        }
    }
}