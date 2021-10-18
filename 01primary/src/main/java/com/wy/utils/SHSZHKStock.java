package com.wy.utils;

import com.wy.bean.EastMoneyBeab;
import org.apache.commons.collections.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yunwang on 2021/10/18 15:44
 */
public class SHSZHKStock {
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static void main(String[] args) {
        //先取30个工作日的日期
        getDate(30);

    }

    private static void getDate(int dayTotal) {
        List<String> dateList = new ArrayList<>();
        int dayCount = 1;
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_SHORT);
        List<EastMoneyBeab.ResultDTO.DataDTO> hshStockDate = null;
        do {
            date = DateUtil.getPreviousWorkingDay(date, -1);
            String ss = df.format(date);
            dateList.add(ss);
            hshStockDate = getHSHStockDate(ss);
            if (hshStockDate != null) {
                outExcle(hshStockDate,ss);
                dayCount++;
            }
        } while (hshStockDate == null || dayCount <= dayTotal);
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> getHSHStockDate(String ss) {
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSHDTOS = SHHStockConnect.getDataDTOS(ss);
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSZDTOS = SZHStockConnect.getDataDTOS(ss);
        List<EastMoneyBeab.ResultDTO.DataDTO> AllStock = new ArrayList<>();
        if (dataSHDTOS == null || dataSHDTOS == null) {
            return null;
        }
        AllStock.addAll(dataSHDTOS);
        AllStock.addAll(dataSZDTOS);
        return AllStock;
    }

    private static void outExcle(List<EastMoneyBeab.ResultDTO.DataDTO> AllStock, String ss) {
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