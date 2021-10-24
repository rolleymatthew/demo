package com.wy.stock.hszh;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.EastMoneyBeab;
import com.wy.bean.HSZHVoBean;
import com.wy.utils.easyexcle.ReadMutilFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/19 11:24
 * 沪深港通持股分析
 */
public class HSHStockAnalyService {
    private static String path = "d:";
    private static String sheetTitle = "%s天";
    private static String fileTitle = "沪港通买卖天数.xlsx";

    public static void main(String[] args) {
        //获取所有数据
//        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap = getDataMap(null, 3, 0);
//        int[] days = {2, 3};
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap = getDataMap(null, -1, 0);
        int[] days = {2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 30, 50};
        exportExcle(dataMap, days);
    }

    public static void exportExcle(LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap, int[] days) {
        ExcelWriter excelWriter = null;
        //计算天数
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
    }


    private static List<HSZHVoBean> countUpZero(LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap, int daySize) {
        List<HSZHVoBean> hszhVoBeanList = new ArrayList<>();
        for (Map.Entry<String, List<EastMoneyBeab.ResultDTO.DataDTO>> stringListEntry : dataMap.entrySet()) {
            //1.取出要计算的天数数据
            List<EastMoneyBeab.ResultDTO.DataDTO> dtoList = stringListEntry.getValue().stream()
                    .limit(daySize).collect(Collectors.toList());

            //2计算
            HSZHVoBean hszhVoBean = getHszhVoBean(dtoList);

            //3加入返回
            hszhVoBeanList.add(hszhVoBean);

        }
        return hszhVoBeanList.stream().sorted(Comparator.comparing(HSZHVoBean::getBuyDayCount).reversed()).collect(Collectors.toList());
    }

    private static HSZHVoBean getHszhVoBean(List<EastMoneyBeab.ResultDTO.DataDTO> dtoList) {
        //计算买入卖出天数
        Long buyDayCount = dtoList.stream().filter(p ->
                p.getAddMarketCap() > 0.0
        ).collect(Collectors.counting());
        Long sellDayCount = dtoList.stream().filter(p ->
                p.getAddMarketCap() < 0.0
        ).collect(Collectors.counting());

        //计算持股数和持有市值变化
        Double changeMarketCap = dtoList.stream().collect(Collectors.summingDouble(p -> p.getAddMarketCap()));
        Double addSharesRepair = dtoList.stream().collect(Collectors.summingDouble(p -> p.getAddSharesRepair()));

        EastMoneyBeab.ResultDTO.DataDTO oneDTO = dtoList.get(0);
        HSZHVoBean hszhVoBean = new HSZHVoBean();
        hszhVoBean.setSecurityCode(oneDTO.getSecurityCode());
        hszhVoBean.setSecurityName(oneDTO.getSecurityName());
        hszhVoBean.setFreeSharesRatio(oneDTO.getFreeSharesRatio());
        hszhVoBean.setTotalSharesRatio(oneDTO.getTotalSharesRatio());
        hszhVoBean.setHoldMarketCap(oneDTO.getHoldMarketCap());
        hszhVoBean.setHoldShares(oneDTO.getHoldShares());
        hszhVoBean.setIndustryName(oneDTO.getIndustryName());
        if (buyDayCount==null) {
            hszhVoBean.setBuyDayCount(0.0);
        } else {
                hszhVoBean.setBuyDayCount(buyDayCount.doubleValue());
        }
        if (sellDayCount==null) {
            hszhVoBean.setSellDayCount(0.0);
        } else {
                hszhVoBean.setSellDayCount(sellDayCount.doubleValue());
        }
//        EastMoneyBeab.ResultDTO.DataDTO dataDTOLast = dtoList.get(dtoList.size() - 1);
        hszhVoBean.setChangeMarketCap(changeMarketCap);
        hszhVoBean.setChangeShares(addSharesRepair);

        return hszhVoBean;
    }

    private static double getaDouble(Double aDouble, Double aDouble1) {
        if (aDouble == null || aDouble1 == null || Double.isNaN(aDouble) || Double.isNaN(aDouble1)) {
            return 0;
        }
        double result = aDouble.doubleValue() - aDouble1.doubleValue();
        BigDecimal b = new BigDecimal(result);
        result = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    public static LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> getDataMap(String code, int daySize, int sheetNum) {
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
