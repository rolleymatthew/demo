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
    public static void main(String[] args) {
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap = getDataMap(null, 30, 0);
        //计算30天内买入卖出的天数
        List<HSZHVoBean> hszhVoBeans = countUpZero(dataMap, null, 30, 0);
        //当前时间连续买入和卖出的天数
        //5天之内买入卖出前50大市值
        //10天之内买入卖出前50大市值
        //20天之内买入卖出前50大市值
        //30天之内买入卖出前50大市值
        //50天之内买入卖出前50大市值
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write("d:" + File.separator + "asfsafsda.xlsx", HSZHVoBean.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0,  "买入卖出的天数").build();
            excelWriter.write(hszhVoBeans, writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private static List<HSZHVoBean> countUpZero(LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap,String code, int daySize, int sheetNum) {
        List<HSZHVoBean> ret=new ArrayList<>();
        for (Map.Entry<String, List<EastMoneyBeab.ResultDTO.DataDTO>> stringListEntry : dataMap.entrySet()) {
            List<EastMoneyBeab.ResultDTO.DataDTO> dtoList = stringListEntry.getValue();
            Map<String, Long> buyDayCount = dtoList.stream().filter(p ->
                    Double.parseDouble(String.valueOf(p.getAddMarketCap())) > 0.0
            ).collect(Collectors.groupingBy(p -> p.getSecurityCode(), Collectors.counting()));
            Map<String, Long> sellDayCount = dtoList.stream().filter(p ->
                    Double.parseDouble(String.valueOf(p.getAddMarketCap())) < 0.0
            ).collect(Collectors.groupingBy(p -> p.getSecurityCode(), Collectors.counting()));
            EastMoneyBeab.ResultDTO.DataDTO oneDTO=dtoList.get(0);
            HSZHVoBean hszhVoBean=new HSZHVoBean();
            hszhVoBean.setSecurityCode(stringListEntry.getKey());
            hszhVoBean.setSecurityName(oneDTO.getSecurityName());
            hszhVoBean.setFreeSharesRatio(oneDTO.getFreeSharesRatio());
            hszhVoBean.setTotalSharesRatio(oneDTO.getTotalSharesRatio());
            if (buyDayCount.isEmpty()) {
                hszhVoBean.setBuyDayCount(0.0);
            }else{
                if (buyDayCount.containsKey(oneDTO.getSecurityCode())) {
                    hszhVoBean.setBuyDayCount(Double.parseDouble(buyDayCount.get(oneDTO.getSecurityCode()).toString()));
                }
            }
            if (sellDayCount.isEmpty()) {
                hszhVoBean.setSellDayCount(0.0);
            }else{
                if (sellDayCount.containsKey(oneDTO.getSecurityCode())) {
                    hszhVoBean.setSellDayCount(Double.parseDouble(sellDayCount.get(oneDTO.getSecurityCode()).toString()));
                }
            }
            ret.add(hszhVoBean);
        }
        return ret;
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
