package com.wy.analysis;

import com.wy.bean.EastMoneyBeab;
import com.wy.utils.easyexcle.ReadMutilFile;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/19 11:24
 * 沪深港通持股分析
 */
public class HSHStockAnaly {
    public static void main(String[] args) {
        //计算3天内为买入的
        countUpZero(null, 3, 0);
    }

    private static void countUpZero(String code, int daySize, int sheetNum) {
        //1.读出所有文件路径
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList = ReadMutilFile.getDataDTOS(code, daySize, sheetNum);
        //2.整理出所有数据按照时间顺序排列
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap = getAllStockMap(dataDTOList);
//        dataMap.entrySet().stream().forEach(System.out::println);
        //3.在每一个代码里计算买入的天数
        for (Map.Entry<String, List<EastMoneyBeab.ResultDTO.DataDTO>> stringListEntry : dataMap.entrySet()) {
            String codeTemp = stringListEntry.getKey();
            List<EastMoneyBeab.ResultDTO.DataDTO> dtoList = stringListEntry.getValue();
            Map<String, Long> collect = dtoList.stream().filter(p ->
                    Double.parseDouble(String.valueOf(p.getAddMarketCap())) > 0.0
            ).collect(Collectors.groupingBy(p -> p.getSecurityName(), Collectors.counting()));
            if (!collect.isEmpty()) {
                System.out.println(codeTemp + "   " + collect.toString());

            }
        }
    }

    private static LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> getAllStockMap(List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList) {
        LinkedHashMap<String, List<EastMoneyBeab.ResultDTO.DataDTO>> dataMap =
                dataDTOList.stream().filter(x -> x.getAddMarketCap() != null).sorted(Comparator.comparing(EastMoneyBeab.ResultDTO.DataDTO::getTradeDate).reversed())
                        .collect(Collectors.groupingBy(x -> x.getSecurityCode(), LinkedHashMap::new, Collectors.toList()));
        return dataMap;
    }


}
