package com.wy.stock.hszh;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.Contant;
import com.wy.bean.EastMoneyBeab;
import com.wy.utils.DateUtil;
import com.wy.utils.ExportExcelUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yunwang on 2021/10/18 15:44
 */
public class GetSHSZHKStockDateService {
    public static String PATH = Contant.DIR + File.separator + "HSHSTOCK";
    public static String FILE_PRE = "HSHStock";
    public static String FILE_EXT = Contant.FILE_EXT;
    public static String SHEET_NAME = "HSHSTOCK";
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        //当天
//        getMutilSheet(2);

        //历史数据
        getMutilSheet(60);
    }

    public static void getMutilSheet(int dayTotal) {
        int dayCount = 1;
        int count = dayTotal + 30;
        Date date = new Date();
        List<EastMoneyBeab.ResultDTO.DataDTO> hshStockDate = null;
        List<EastMoneyBeab.ResultDTO.DataDTO> hszStockDate = null;
        do {
            date = DateUtil.getPreviousWorkingDay(date, -1);
            String ss = df.format(date);
            System.out.println("GetSHSZHKStockDateService:" + ss);

            hshStockDate = getDataSHDTOS(ss);
            hszStockDate = getDataSZDTOS(ss);
            if (hshStockDate != null && hszStockDate != null) {
                List<EastMoneyBeab.ResultDTO.DataDTO> allStock = new ArrayList<>();
                allStock.addAll(hshStockDate);
                allStock.addAll(hszStockDate);
                allStock.sort(getDtoComparator());
                ExcelWriter excelWriter = null;
                try {
                    excelWriter = EasyExcel.write(PATH + File.separator + FILE_PRE + ss + FILE_EXT, EastMoneyBeab.ResultDTO.DataDTO.class).build();
                    WriteSheet writeSheet = EasyExcel.writerSheet(0, SHEET_NAME + "ALL").build();
                    excelWriter.write(allStock, writeSheet);
                    writeSheet = EasyExcel.writerSheet(1, SHEET_NAME + "SH").build();
                    excelWriter.write(hshStockDate, writeSheet);
                    writeSheet = EasyExcel.writerSheet(2, SHEET_NAME + "SZ").build();
                    excelWriter.write(hszStockDate, writeSheet);
                } finally {
                    // 千万别忘记finish 会帮忙关闭流
                    if (excelWriter != null) {
                        excelWriter.finish();
                    }
                }
                dayCount++;
            }
            count--;
            if (count <= 0) {
                break;
            }
        } while (hshStockDate == null || dayCount <= dayTotal);
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> getDataSZDTOS(String ss) {
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSZDTOS = SZHStockConnect.getDataDTOS(ss);
        if (dataSZDTOS == null) {
            return null;
        }
        dataSZDTOS.sort(getDtoComparator());
        return dataSZDTOS;
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> getDataSHDTOS(String ss) {
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSHDTOS = SHHStockConnect.getDataDTOS(ss);
        if (dataSHDTOS == null) {
            return null;
        }
        dataSHDTOS.sort(getDtoComparator());
        return dataSHDTOS;
    }

    private static Comparator<EastMoneyBeab.ResultDTO.DataDTO> getDtoComparator() {
        return new Comparator<EastMoneyBeab.ResultDTO.DataDTO>() {
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
        };
    }

}