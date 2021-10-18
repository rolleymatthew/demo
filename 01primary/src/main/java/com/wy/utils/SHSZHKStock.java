package com.wy.utils;

import com.wy.bean.EastMoneyBeab;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yunwang on 2021/10/18 15:44
 */
public class SHSZHKStock {
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static void main(String[] args) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_SHORT);
        Date date = new Date();
        date = DateUtil.getPreviousWorkingDay(date, -1);
        String ss = df.format(date);

        List<EastMoneyBeab.ResultDTO.DataDTO> dataSHDTOS = SHHStockConnect.getDataDTOS(ss);
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSZDTOS = SZHStockConnect.getDataDTOS(ss);
        List<EastMoneyBeab.ResultDTO.DataDTO> AllStock = new ArrayList<>();
        AllStock.addAll(dataSHDTOS);
        AllStock.addAll(dataSZDTOS);
        for (EastMoneyBeab.ResultDTO.DataDTO dataDTO : AllStock) {
            System.out.println(dataDTO.getSecurityName());
        }
        System.out.println(dataSHDTOS.size());
        System.out.println(dataSZDTOS.size());
        System.out.println(AllStock.size());
        String[] columnNames = {"名称", "日期"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        try {
            exportExcelUtil.exportExcel("HSHStock", columnNames, AllStock, new FileOutputStream("d://HSHSTOCK" + "//" + "HSHStock" + ss + ".xlsx"),
                    ExportExcelUtil.EXCEl_FILE_2007);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}