package com.wy.analysis;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.EastMoneyBeab;
import com.wy.bean.HSZHVoBean;
import com.wy.utils.easyexcle.ReadMutilFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/19 11:24
 * 沪深港通持股分析
 */
public class HSHStockAnaly {
    private static String path = "d:";
    private static String sheetTitle = "%s天";
    private static String fileTitle = "沪港通买卖天数.xlsx";

    public static void main(String[] args) {
        //获取所有数据
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap = getDataMap(null, -1, 0);
        ExcelWriter excelWriter = null;
        //计算天数
        int[] days = {2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 30, 50};
        try {
            excelWriter = EasyExcel.write(path + File.separator + fileTitle, HSZHVoBean.class).build();
            //计算N天内买入卖出的天数
            for (int i = 0; i < days.length; i++) {
                int day = days[i];
                List<HSZHVoBean> hszhVoBeans = countUpZero(dataMap, day);
                WriteSheet writeSheet = EasyExcel.writerSheet(day, String.format(sheetTitle, day)).build();
                excelWriter.write(hszhVoBeans, writeSheet);
            }

        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        //当前时间连续买入和卖出的天数
        //5天之内买入卖出前50大市值
        //10天之内买入卖出前50大市值
        //20天之内买入卖出前50大市值
        //30天之内买入卖出前50大市值
        //50天之内买入卖出前50大市值
    }


    private static List<HSZHVoBean> countUpZero(LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap, int daySize) {
        List<HSZHVoBean> hszhVoBeanList = new ArrayList<>();
        for (Map.Entry<String, List<EastMoneyBeab.ResultDTO.DataDTO>> stringListEntry : dataMap.entrySet()) {
            //1.取出要计算的天数数据
            List<EastMoneyBeab.ResultDTO.DataDTO> dtoList = stringListEntry.getValue().stream().limit(daySize).collect(Collectors.toList());

            //2计算单个买入金额为正和负的
            Map<String, Long> buyDayCount = dtoList.stream().filter(p ->
                    Double.parseDouble(String.valueOf(p.getAddMarketCap())) > 0.0
            ).collect(Collectors.groupingBy(p -> p.getSecurityCode(), Collectors.counting()));
            Map<String, Long> sellDayCount = dtoList.stream().filter(p ->
                    Double.parseDouble(String.valueOf(p.getAddMarketCap())) < 0.0
            ).collect(Collectors.groupingBy(p -> p.getSecurityCode(), Collectors.counting()));

            //3加入返回
            hszhVoBeanList.add(getHszhVoBean(stringListEntry, dtoList, buyDayCount, sellDayCount));

        }
        return hszhVoBeanList.stream().sorted(Comparator.comparing(HSZHVoBean::getBuyDayCount).reversed()).collect(Collectors.toList());
    }

    private static HSZHVoBean getHszhVoBean(Map.Entry<String, List<EastMoneyBeab.ResultDTO.DataDTO>> stringListEntry, List<EastMoneyBeab.ResultDTO.DataDTO> dtoList, Map<String, Long> buyDayCount, Map<String, Long> sellDayCount) {
        EastMoneyBeab.ResultDTO.DataDTO oneDTO = dtoList.get(0);
        HSZHVoBean hszhVoBean = new HSZHVoBean();
        hszhVoBean.setSecurityCode(stringListEntry.getKey());
        hszhVoBean.setSecurityName(oneDTO.getSecurityName());
        hszhVoBean.setFreeSharesRatio(oneDTO.getFreeSharesRatio());
        hszhVoBean.setTotalSharesRatio(oneDTO.getTotalSharesRatio());
        hszhVoBean.setHoldMarketCap(oneDTO.getHoldMarketCap());
        hszhVoBean.setHoldShares(oneDTO.getHoldShares());
        hszhVoBean.setIndustryName(oneDTO.getIndustryName());
        if (buyDayCount.isEmpty()) {
            hszhVoBean.setBuyDayCount(0.0);
        } else {
            if (buyDayCount.containsKey(oneDTO.getSecurityCode())) {
                hszhVoBean.setBuyDayCount(Double.parseDouble(buyDayCount.get(oneDTO.getSecurityCode()).toString()));
            }
        }
        if (sellDayCount.isEmpty()) {
            hszhVoBean.setSellDayCount(0.0);
        } else {
            if (sellDayCount.containsKey(oneDTO.getSecurityCode())) {
                hszhVoBean.setSellDayCount(Double.parseDouble(sellDayCount.get(oneDTO.getSecurityCode()).toString()));
            }
        }
        return hszhVoBean;
    }

    private static LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> getDataMap(String code, int daySize, int sheetNum) {
        //1.读出所有文件路径
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList = ReadMutilFile.getDataDTOS(code, daySize, sheetNum);
        //2.整理出所有数据按照时间顺序排列
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap = getAllStockMap(dataDTOList);
        return dataMap;
    }

    private static LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> getAllStockMap(List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList) {
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap =
                dataDTOList.stream().filter(x -> x.getAddMarketCap() != null).sorted(Comparator.comparing(EastMoneyBeab.ResultDTO.DataDTO::getTradeDate).reversed())
                        .collect(Collectors.groupingBy(x -> x.getSecurityCode(), LinkedHashMap::new, Collectors.toList()));
        return dataMap;
    }


}
