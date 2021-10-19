package com.wy.utils;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.EastMoneyBeab;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yunwang on 2021/10/18 15:44
 */
public class SHSZHKStock {
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static void main(String[] args) {
        //60天的
//        getDate(60);
        getDate(60);

    }

    private static void getDate(int dayTotal) {
        int dayCount = 1;
        int count = dayTotal + 30;
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_SHORT);
        List<EastMoneyBeab.ResultDTO.DataDTO> hshStockDate = null;
        do {
            date = DateUtil.getPreviousWorkingDay(date, -1);
            String ss = df.format(date);
            System.out.println(ss);

            hshStockDate = getHSHStockDate(ss);
            if (hshStockDate != null) {
                EasyExcel.write("d://HSHSTOCK" + File.separator + "HSHStock" + ss + ".xlsx", EastMoneyBeab.ResultDTO.DataDTO.class)
                        .sheet("HSHSTOCK")
                        .doWrite(hshStockDate);
//                outExcle(hshStockDate,ss);
                dayCount++;
            }
            count--;
            if (count <= 0) {
                break;
            }
        } while (hshStockDate == null || dayCount <= dayTotal);
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> getDate(List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList) {
        if (dataDTOList == null) {
            return null;
        }
        return dataDTOList;
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
        AllStock.sort(new Comparator<EastMoneyBeab.ResultDTO.DataDTO>() {
            @Override
            public int compare(EastMoneyBeab.ResultDTO.DataDTO o1, EastMoneyBeab.ResultDTO.DataDTO o2) {
                if (o1.getAddMarketCap() == null || o2.getAddMarketCap() == null) {
                    return 0;
                }
                if (o1.getAddMarketCap().doubleValue() > o2.getAddMarketCap().doubleValue()) {
                    return -1;
                } else if (o1.getAddMarketCap().doubleValue() < o2.getAddMarketCap().doubleValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return AllStock;
    }

    private static void outExcle(List<EastMoneyBeab.ResultDTO.DataDTO> AllStock, String ss) {
        String[] columnNames = {"名称", "日期"};
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        try {
            exportExcelUtil.exportExcel("HSHStock", columnNames, AllStock, new FileOutputStream("d://HSHSTOCK" + File.separator + "HSHStock" + ss + ".xlsx"),
                    ExportExcelUtil.EXCEl_FILE_2007);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}