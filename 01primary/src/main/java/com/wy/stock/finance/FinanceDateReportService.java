package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.EastMoneyBeab;
import com.wy.bean.FinThreePerBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.DateUtil;
import com.wy.utils.NumUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author yunwang
 * @Date 2021-10-27
 */
public class FinanceDateReportService {
    public static void main(String[] args) {
        int[] counts = {1, 2, 3};
        List<String> allCodes = FinanceDateWriteService.getAllCodes();
        //读取文件
        Map<String, List<FinanceDataBean>> dataMap = getFinanceListMap(allCodes);

        //计算三率
        Map<String, List<FinThreePerBean>> finPerMap = getFinPerMap(dataMap);

        //找到三率三升的
        Map<String, List<FinThreePerBean>> threePerMap = fillFinPerMap(finPerMap);

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(FinanceDateWriteService.PATH_MAIN + File.separator
                    + String.format(FinanceDateWriteService.FILE_NAME_REPORT, DateUtil.getCurrentDay()), FinThreePerBean.class).build();
            for (int i = 0; i < counts.length; i++) {
                int i1 = counts[i];
                WriteSheet writeSheet = EasyExcel.writerSheet(i, i1 + "天").build();
                List<FinThreePerBean> finThreePerBeans = UpFinThreePerList(threePerMap, i1);
                excelWriter.write(finThreePerBeans, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }

    private static List<FinThreePerBean> UpFinThreePerList(Map<String, List<FinThreePerBean>> threePerMap, int count) {
        List<FinThreePerBean> collect = new ArrayList<>();
        for (Map.Entry<String, List<FinThreePerBean>> stringListEntry : threePerMap.entrySet()) {
            List<FinThreePerBean> value = stringListEntry.getValue().stream().limit(count).collect(Collectors.toList());
            List<FinThreePerBean> collect1 = value.stream().filter(s -> {
                if (s.getAddGrossProfit() > 0 && s.getAddOperatProfit() > 0 && (s.getAddNetProfit() > 0)
                        && s.getGrossProfit() > 0 && s.getOperatProfit() > 0 && s.getNetProfit() > 0) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            if (collect1.size() == count) {
                collect.addAll(collect1);
            }
        }
        return collect;
    }

    private static Map<String, List<FinThreePerBean>> fillFinPerMap(Map<String, List<FinThreePerBean>> finPerMap) {
        Map<String, List<FinThreePerBean>> threePerMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, List<FinThreePerBean>> stringListEntry : finPerMap.entrySet()) {
            String code = stringListEntry.getKey();
            List<FinThreePerBean> collect = stringListEntry.getValue().stream().limit(4)
                    .map(x -> {
                        x.setSecurityCode(code);
                        return x;
                    })
                    .collect(Collectors.toList());
            //计算季度变化
            for (int i = 0; i < collect.size(); i++) {
                FinThreePerBean finThreePerBean = collect.get(i);
                if (i == collect.size() - 1) {
                    break;
                }
                FinThreePerBean finThreePerBean1 = collect.get(i + 1);
                finThreePerBean.setAddGrossProfit(finThreePerBean.getGrossProfit() - finThreePerBean1.getGrossProfit());
                finThreePerBean.setAddNetProfit(finThreePerBean.getNetProfit() - finThreePerBean1.getNetProfit());
                finThreePerBean.setAddOperatProfit(finThreePerBean.getOperatProfit() - finThreePerBean1.getOperatProfit());
            }
            threePerMap.put(code, collect);
        }
        return threePerMap;
    }

    private static Map<String, List<FinThreePerBean>> getFinPerMap(Map<String, List<FinanceDataBean>> dataMap) {
        //计算所有的三率Map
        Map<String, List<FinThreePerBean>> finPerMap = dataMap.entrySet().stream().collect(Collectors.toMap(x -> x.getKey()
                , x -> {
                    List<FinanceDataBean> collect = x.getValue().stream().collect(Collectors.toList());
                    List<FinThreePerBean> threePerBeanList = collect.stream().map(s -> {
                        FinThreePerBean finThreePerBean = new FinThreePerBean();
                        Double mainBusiIncome = NumUtils.stringToDouble(s.getMainBusiIncome());
                        finThreePerBean.setReportData(s.getReportDate());
                        finThreePerBean.setGrossProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getMainBusiProfit()) / mainBusiIncome * 100));
                        finThreePerBean.setOperatProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getOperatProfit()) / mainBusiIncome * 100));
                        finThreePerBean.setNetProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getNetProfit()) / mainBusiIncome * 100));
                        return finThreePerBean;
                    }).collect(Collectors.toList());
                    return threePerBeanList;
                }));
        return finPerMap;
    }

    /**
     * 获取所有代码的主要财务数据
     *
     * @param allCodes 代码
     * @return
     */
    private static Map<String, List<FinanceDataBean>> getFinanceListMap(List<String> allCodes) {
        Map<String, List<FinanceDataBean>> dataMap = new ConcurrentHashMap<>();
        allCodes.parallelStream().forEach(s -> {
            EasyExcel.read(FinanceDateWriteService.PATH_MAIN + File.separator
                            + FinanceDateWriteService.PATH_ZYCWZB_REPORT + File.separator
                            + String.format(FinanceDateWriteService.FILE_NAME_REPORT, StringUtils.trim(s)), FinanceDataBean.class
                    , new PageReadListener<FinanceDataBean>(dataList -> {
                        dataMap.put(s, dataList.stream()
                                .filter(x -> StringUtils.isNotEmpty(x.getReportDate()))
                                .sorted(Comparator.comparing(FinanceDataBean::getReportDate).reversed())
                                .collect(Collectors.toList()));
                    })).sheet(0).doRead();
        });
        return dataMap;
    }

}
