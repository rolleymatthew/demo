package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.FinThreePerBean;
import com.wy.bean.OperatProfitBean;
import com.wy.bean.ProfitDateBean;
import com.wy.utils.DateUtil;
import com.wy.utils.NumUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ProfitReportService {
    private static Logger logger = LoggerFactory.getLogger(ProfitReportService.class);

    public static void outputOpeProfitPer(List<OperatProfitBean> operatProfitBeans) {
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(FinanceCommonService.PATH_REPORT + File.separator
                    + String.format(FinanceCommonService.FILE_NAME_PER, DateUtil.getCurrentDay()), OperatProfitBean.class).build();
            int i = 0;
            WriteSheet writeSheet = EasyExcel.writerSheet(i, "所有").build();
            excelWriter.write(operatProfitBeans.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            List<OperatProfitBean> collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitSame() > 0 && s.getAddNetProfitComp() > 0).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润同比环比增加").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() > s.getAddNetProfitSame()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润环比大于同比").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() > s.getAddOperatingIncomeComp()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润增速大于营收增速").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() >= 50 && s.getAddNetProfitSame() >= 30).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润同比30以上,环比50以上").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() >= 70 && s.getAddNetProfitSame() >= 30).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润同比30以上,环比70以上").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() >= 80 && s.getAddNetProfitSame() >= 50).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润同比50以上,环比80以上").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    public static void outputUpFinThreePer(int[] counts, Map<String, List<FinThreePerBean>> threePerMap, Map<String, String> acode) {
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(FinanceCommonService.PATH_REPORT + File.separator
                    + String.format(FinanceCommonService.FILE_NAME_REPORT, DateUtil.getCurrentDay()), FinThreePerBean.class).build();
            for (int i = 0; i < counts.length; i++) {
                int i1 = counts[i];
                WriteSheet writeSheet = EasyExcel.writerSheet(i, i1 + "报告期").build();
                List<FinThreePerBean> finThreePerBeans = UpFinThreePerList(threePerMap, i1, acode);
                excelWriter.write(finThreePerBeans, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    public static List<FinThreePerBean> UpFinThreePerList(Map<String, List<FinThreePerBean>> threePerMap, int count, Map<String, String> acode) {
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
                collect.stream().forEach(x -> x.setSecurityName(acode.get(x.getSecurityCode())));
                collect.addAll(collect1);
            }
        }
        return collect;
    }

    public static Map<String, List<FinThreePerBean>> fillFinPerMap(Map<String, List<FinThreePerBean>> finPerMap) {
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

    public static Map<String, List<FinThreePerBean>> getFinPerMap(Map<String, List<ProfitDateBean>> dataMap) {
        //计算所有的三率Map
        Map<String, List<FinThreePerBean>> finPerMap = dataMap.entrySet().stream().collect(Collectors.toMap(x -> x.getKey()
                , x -> {
                    List<ProfitDateBean> collect = x.getValue().stream().collect(Collectors.toList());
                    List<FinThreePerBean> threePerBeanList = collect.stream().map(s -> {
                        FinThreePerBean finThreePerBean = new FinThreePerBean();
                        Double mainBusiIncome = income(s);
                        Double cost = cost(s);
                        finThreePerBean.setReportData(s.getReportDate());
                        finThreePerBean.setGrossProfit(NumUtils.roundDouble((mainBusiIncome - cost) / mainBusiIncome * 100));
                        finThreePerBean.setOperatProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getOperatingProfit()) / mainBusiIncome * 100));
                        finThreePerBean.setNetProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getNetProfit()) / mainBusiIncome * 100));
                        return finThreePerBean;
                    }).collect(Collectors.toList());
                    return threePerBeanList;
                }));
        return finPerMap;
    }

    private static Double income(ProfitDateBean s) {
        if (StringUtils.equalsIgnoreCase(s.getOperatingIncome(), "--")) {
            //金融企业计算方式：营业收入=营业总收入-其他收入
            return  NumUtils.stringToDouble(s.getTotalOperatingIncome())-NumUtils.stringToDouble(s.getOtherBusinessIncome());
        }
        return NumUtils.stringToDouble(s.getOperatingIncome());
    }

    private static Double cost(ProfitDateBean s) {
        if (StringUtils.equalsIgnoreCase(s.getOperatingCost(), "--")) {
            //金融企业计算方式
            return NumUtils.stringToDouble(s.getTotalOperatingCost());
        }
        return NumUtils.stringToDouble(s.getOperatingCost());
    }

    public static void main(String[] args) {
        List<String> alCodes = new ArrayList<>();
        alCodes.add("688981");
        //获取最近一年的数据
        Map<String, List<ProfitDateBean>> financeListMap = FinanceCommonService.getFinanceListMap(alCodes)
                .entrySet().stream().filter(x -> x.getValue().size() >= 5).collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));
        ;
        List<OperatProfitBean> opeProlist = getOperatProfitBeans(financeListMap, null);
        System.out.println(opeProlist.size());
    }

    public static List<OperatProfitBean> getOperatProfitBeans(Map<String, List<ProfitDateBean>> financeListMap, Map<String, String> acode) {
        //计算环比同比数据
        List<OperatProfitBean> opeProlist = new ArrayList<>();
        financeListMap.entrySet().stream().forEach(s -> {
            List<ProfitDateBean> value = s.getValue();
            //找到上一季度的报告
            List<ProfitDateBean> lastQuarter = value.stream().filter(x -> StringUtils.equals(x.getReportDate(), DateUtil.fmtShortDate(DateUtil.getLastQuarterEndTime())))
                    .collect(Collectors.toList());
            //找到上两个季度的报告
            List<ProfitDateBean> lastTwoQuarter = value.stream().filter(x -> StringUtils.equals(x.getReportDate(), DateUtil.fmtShortDate(DateUtil.getLastTwoQuarterEndTime())))
                    .collect(Collectors.toList());
            //找到去年同一个季度的报告
            List<ProfitDateBean> lastYearQuarter = value.stream().filter(x -> StringUtils.equals(x.getReportDate(), DateUtil.fmtShortDate(DateUtil.getLastYearSameQuarterEndTime())))
                    .collect(Collectors.toList());
            //计算同比
            OperatProfitBean operatProfitBean = new OperatProfitBean();
            operatProfitBean.setSecurityCode(s.getKey());
            if (!acode.isEmpty() && acode.containsKey(s.getKey())) {
                operatProfitBean.setSecurityName(acode.get(s.getKey()));
            }
            if (CollectionUtils.isNotEmpty(lastQuarter) && CollectionUtils.isNotEmpty(lastYearQuarter)) {
                ProfitDateBean profitLastDateBean = lastQuarter.get(0);
                ProfitDateBean profitLastYearDateBean = lastYearQuarter.get(0);
                //营收同比
                double adTemp = (income(profitLastDateBean) - income(profitLastYearDateBean)) / income(profitLastYearDateBean) * 100;
                operatProfitBean.setAddOperatingIncomeSame(NumUtils.roundDouble(adTemp));
                //净利润同比
                adTemp = (NumUtils.stringToDouble(profitLastDateBean.getNetProfit()) - NumUtils.stringToDouble(profitLastYearDateBean.getNetProfit())) / NumUtils.stringToDouble(profitLastYearDateBean.getNetProfit()) * 100;
                operatProfitBean.setAddNetProfitSame(NumUtils.roundDouble(adTemp));
            }
            //计算环比
            if (CollectionUtils.isNotEmpty(lastQuarter) && CollectionUtils.isNotEmpty(lastTwoQuarter)) {
                ProfitDateBean profitLastDateBean = lastQuarter.get(0);
                ProfitDateBean profitLastTwoDateBean = lastTwoQuarter.get(0);
                double adTemp = (income(profitLastDateBean) - income(profitLastTwoDateBean)) / income(profitLastTwoDateBean) * 100;
                operatProfitBean.setAddOperatingIncomeComp(NumUtils.roundDouble(adTemp));
                adTemp = (NumUtils.stringToDouble(profitLastDateBean.getNetProfit()) - NumUtils.stringToDouble(profitLastTwoDateBean.getNetProfit()))
                        / NumUtils.stringToDouble(profitLastTwoDateBean.getNetProfit()) * 100;
                operatProfitBean.setAddNetProfitComp(NumUtils.roundDouble(adTemp));
            }
            if (CollectionUtils.isNotEmpty(lastQuarter)){
                opeProlist.add(operatProfitBean);
            }
        });
        return opeProlist;
    }

}
