package com.laohe.excle;

import java.util.List;

/**
 * @author laohe
 * @date 2017-06-29
 *
 */
public class ExcelToDb {

    public static void main(String[] args) {

        DBeePer db=new DBeePer();

        //得到表格中所有的数据
        List<Entity> listExcel=StuService.getAllByExcel("d:/blackcompany.xls");
        System.out.println();
        System.out.println("----------------开始数数据！！！----------------------");
        System.out.println();
        System.out.println();
        for (Entity entity : listExcel) {
            if (!StuService.isExist(entity.getBlackCompany())) {
                //不存在就添加
                String sql="insert into t_black_company (blackcompany) values(?)";
                String[] str=new String[]{entity.getBlackCompany()};
                db.AddU(sql, str);
                System.out.println("----------------已经成功插入黑企业名单："+ entity.getBlackCompany()+"----------------------");
            };/*else {
                //存在就更新
                String sql="update t_bkack_company set blackcompany=?";
                String[] str=new String[]{entity.getBlackCompany()};
                db.AddU(sql, str);
            }*/
        }
        System.out.println();
        System.out.println();
        System.out.println("----------------刷数据结束！！！----------------------");
    }
}