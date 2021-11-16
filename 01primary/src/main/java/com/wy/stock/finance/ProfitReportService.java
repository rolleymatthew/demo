package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.*;
import com.wy.utils.DateUtil;
import com.wy.utils.FilesUtil;
import com.wy.utils.NumUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
    private static List<String> errorCode = new ArrayList<>();

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
                        finThreePerBean.setReportData(s.getReportDate());
                        finThreePerBean.setGrossProfit(getGrossProfit(s));
                        finThreePerBean.setOperatProfit(getOperatProfit(s));
                        finThreePerBean.setNetProfit(getNetProfit(s));
                        return finThreePerBean;
                    }).collect(Collectors.toList());
                    return threePerBeanList;
                }));
        return finPerMap;
    }

    /**
     * 净利率
     *
     * @param s
     * @return
     */
    private static Double getNetProfit(ProfitDateBean s) {
        return NumUtils.roundDouble(NumUtils.stringToDouble(s.getNetProfit()) / income(s) * 100);
    }

    /**
     * 毛利率
     *
     * @param s
     * @return
     */
    private static Double getGrossProfit(ProfitDateBean s) {
        return NumUtils.roundDouble((income(s) - cost(s)) / income(s) * 100);
    }

    /**
     * 营业利润率
     *
     * @param s
     * @return
     */
    private static Double getOperatProfit(ProfitDateBean s) {
        return NumUtils.roundDouble(NumUtils.stringToDouble(s.getOperatingProfit()) / income(s) * 100);
    }

    /**
     * 营业收入
     *
     * @param s
     * @return
     */
    private static Double income(ProfitDateBean s) {
        if (StringUtils.equalsIgnoreCase(s.getOperatingIncome(), "--")) {
            //金融企业计算方式：营业收入=营业总收入-其他收入
            return NumUtils.stringToDouble(s.getTotalOperatingIncome()) - NumUtils.stringToDouble(s.getOtherBusinessIncome());
        }
        return NumUtils.stringToDouble(s.getOperatingIncome());
    }

    private static Double incomeZQH(ProfitDateBean s) {
        if (StringUtils.equalsIgnoreCase(s.getOperatingIncome(), "--")) {
            //金融企业计算方式：营业收入=营业总收入-其他收入
            return NumUtils.stringToDouble(s.getTotalOperatingIncome()) - NumUtils.stringToDouble(s.getOtherBusinessIncome());
        }
        return NumUtils.stringToDouble(s.getTotalOperatingIncome());
    }

    /**
     * 营业成本
     *
     * @param s
     * @return
     */
    private static Double cost(ProfitDateBean s) {
        if (StringUtils.equalsIgnoreCase(s.getOperatingCost(), "--")) {
            //金融企业计算方式
            return NumUtils.stringToDouble(s.getTotalOperatingCost());
        }
        return NumUtils.stringToDouble(s.getOperatingCost());
    }

    public static List<OperatProfitBean> getOperatProfitBeans(Map<String, List<ProfitDateBean>> financeListMap, Map<String, String> acode) {
        //计算环比同比数据
        List<OperatProfitBean> opeProlist = new ArrayList<>();
        financeListMap.entrySet().stream().forEach(s -> {
            List<ProfitDateBean> value = s.getValue();
            OperatProfitBean operatProfitBean = new OperatProfitBean();
            operatProfitBean.setSecurityCode(s.getKey());
            if (!acode.isEmpty() && acode.containsKey(s.getKey())) {
                operatProfitBean.setSecurityName(acode.get(s.getKey()));
            }
            //找到上一季度的报告
            List<ProfitDateBean> lastQuarter = value.stream().filter(x -> StringUtils.equals(x.getReportDate(), DateUtil.fmtShortDate(DateUtil.getLastQuarterEndTime())))
                    .collect(Collectors.toList());
            //找到去年同一个季度的报告
            List<ProfitDateBean> lastYearQuarter = value.stream().filter(x -> StringUtils.equals(x.getReportDate(), DateUtil.fmtShortDate(DateUtil.getLastYearSameQuarterEndTime())))
                    .collect(Collectors.toList());
            //计算同比
            if (CollectionUtils.isNotEmpty(lastQuarter) && CollectionUtils.isNotEmpty(lastYearQuarter)) {
                ProfitDateBean profitLastDateBean = lastQuarter.get(0);
                ProfitDateBean profitLastYearDateBean = lastYearQuarter.get(0);
                //营收同比
                double adTemp = (income(profitLastDateBean) - income(profitLastYearDateBean)) / income(profitLastYearDateBean) * 100;
                operatProfitBean.setAddOperatingIncomeSame(NumUtils.roundDouble(adTemp));
                //净利润同比
                adTemp = (NumUtils.stringToDouble(profitLastDateBean.getNetProfitAttributable()) - NumUtils.stringToDouble(profitLastYearDateBean.getNetProfitAttributable())) / NumUtils.stringToDouble(profitLastYearDateBean.getNetProfitAttributable()) * 100;
                operatProfitBean.setAddNetProfitSame(NumUtils.roundDouble(adTemp));
            }

            //计算环比
            //找到上两个季度的报告
            List<ProfitDateBean> lastTwoQuarter = value.stream().filter(x -> StringUtils.equals(x.getReportDate(), DateUtil.fmtShortDate(DateUtil.getLastTwoQuarterEndTime())))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(lastQuarter) && CollectionUtils.isNotEmpty(lastTwoQuarter)) {
                ProfitDateBean profitLastDateBean = lastQuarter.get(0);
                ProfitDateBean profitLastTwoDateBean = lastTwoQuarter.get(0);
                double adTemp = (income(profitLastDateBean) - income(profitLastTwoDateBean)) / income(profitLastTwoDateBean) * 100;
                operatProfitBean.setAddOperatingIncomeComp(NumUtils.roundDouble(adTemp));
                adTemp = (NumUtils.stringToDouble(profitLastDateBean.getNetProfit()) - NumUtils.stringToDouble(profitLastTwoDateBean.getNetProfit()))
                        / NumUtils.stringToDouble(profitLastTwoDateBean.getNetProfit()) * 100;
                operatProfitBean.setAddNetProfitComp(NumUtils.roundDouble(adTemp));
            }
            if (CollectionUtils.isNotEmpty(lastQuarter)) {
                opeProlist.add(operatProfitBean);
            }
        });
        return opeProlist;
    }

    public static void main(String[] args) {
        List<String> alCodes = new ArrayList<>();
        alCodes.add("688981");
        alCodes.add("600519");
        Map<String, List<ProfitDateBean>> profitListMap = FinanceCommonService.getProfitListMap(alCodes)
                .entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));
        Map<String, List<BalanceDateBean>> balanceListMap = FinanceCommonService.getBalanceListMap(alCodes)
                .entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));
        Map<String, List<CashFlowBean>> cashListMap = FinanceCommonService.getCashListMap(alCodes)
                .entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));
        Map<String, List<ZQHFinBean>> zqhBeanMap = getZqhBeanMap(profitListMap, balanceListMap, cashListMap);

        //输出文件
        outPutZQHFile(zqhBeanMap, null);
    }

    public static void outPutZQHFile(Map<String, List<ZQHFinBean>> zqhBeanMap, Map<String, String> acode) {
        try {
            FilesUtil.mkdirs(FinanceCommonService.PATH_ZQH);
        } catch (IOException e) {
        }
        zqhBeanMap.entrySet().stream().forEach(s -> {
            String fi = "";
            if (!acode.isEmpty() && acode.containsKey(s.getKey())) {
                fi = acode.get(s.getKey());
            }
            String fileName = FinanceCommonService.PATH_ZQH + File.separator + String.format(FinanceCommonService.FILE_NAME_ZQH, s.getKey() + fi);
            try {
                EasyExcel.write(fileName, ZQHFinBean.class)
                        .sheet(s.getKey())
                        .doWrite(s.getValue().stream().sorted(Comparator.comparing(ZQHFinBean::getReportDate).reversed()).collect(Collectors.toList()));
            } catch (Exception e) {
                errorCode.add(fileName);
            }
        });
        if (CollectionUtils.isNotEmpty(errorCode)) {
            errorCode.stream().forEach(s -> logger.info("outPutZQHFile error : {}", s));
        }
    }

    public static Map<String, List<ZQHFinBean>> getZqhBeanMap(Map<String, List<ProfitDateBean>> financeListMap, Map<String, List<BalanceDateBean>> balanceListMap, Map<String, List<CashFlowBean>> cashListMap) {
        Map<String, List<ZQHFinBean>> zqhBeanMap = financeListMap.entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s ->
                {
                    List<ProfitDateBean> value = s.getValue();
                    List<ZQHFinBean> collect = value.stream().map(x -> {
                        ZQHFinBean zqhFinBean = new ZQHFinBean();
                        zqhFinBean.setReportDate(x.getReportDate());
                        zqhFinBean.setOperatingIncome(NumUtils.roundDouble(incomeZQH(x) / 10000));
                        zqhFinBean.setNetProfit(NumUtils.roundDouble(NumUtils.stringToDouble(x.getNetProfitAttributable()) / 10000));
                        zqhFinBean.setNetInterestRate(getNetProfit(x));
                        zqhFinBean.setOperatingGrossProfitMargin(getGrossProfit(x));
                        zqhFinBean.setOperatingProfitMargin(getOperatProfit(x));
                        zqhFinBean.setNetOperatingCashFlow(getCashFlow(x, cashListMap.get(s.getKey())));
                        zqhFinBean.setLAndLiabRatioww(getDebRatio(x, balanceListMap.get(s.getKey())));
                        zqhFinBean.setNetProfitGrowthRate(getNetProfitGrowRate(x, s.getValue()));
                        zqhFinBean.setRevenueGrowthRate(getRevenueGrowthRate(x, s.getValue()));
                        return zqhFinBean;
                    }).collect(Collectors.toList());
                    return collect;
                }
        ));
        return zqhBeanMap;
    }

    /**
     * 收入同比增长率
     * @param curBean
     * @param beanList
     * @return
     */
    private static double getRevenueGrowthRate(ProfitDateBean curBean, List<ProfitDateBean> beanList) {
        //找到同比的数据
        String lastYearSameQuarter = DateUtil.getLastYearSameQuarter(curBean.getReportDate());
        //计算同比数据，归母净利润同比增长
        List<ProfitDateBean> collect = beanList.stream().filter(s -> StringUtils.equals(lastYearSameQuarter, s.getReportDate())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)){
            ProfitDateBean profitDateBean = collect.get(0);
            double v = (NumUtils.stringToDouble(curBean.getOperatingIncome()) - NumUtils.stringToDouble(profitDateBean.getOperatingIncome())) / NumUtils.stringToDouble(profitDateBean.getOperatingIncome()) * 100;
            return NumUtils.roundDouble(v);
        }
        return 0;
    }

    /**
     * 净利润增长率(同比)
     *
     * @param curBean
     * @param beanList
     * @return
     */
    private static double getNetProfitGrowRate(ProfitDateBean curBean, List<ProfitDateBean> beanList) {
        //找到同比的数据
        String lastYearSameQuarter = DateUtil.getLastYearSameQuarter(curBean.getReportDate());
        //计算同比数据，归母净利润同比增长
        List<ProfitDateBean> collect = beanList.stream().filter(s -> StringUtils.equals(lastYearSameQuarter, s.getReportDate())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)){
            ProfitDateBean profitDateBean = collect.get(0);
            double v = (NumUtils.stringToDouble(curBean.getNetProfitAttributable()) - NumUtils.stringToDouble(profitDateBean.getNetProfitAttributable())) / NumUtils.stringToDouble(profitDateBean.getNetProfitAttributable()) * 100;
            return NumUtils.roundDouble(v);
        }
        return 0;
    }

    private static double getDebRatio(ProfitDateBean profitDateBean, List<BalanceDateBean> balanceDateBeans) {
        List<BalanceDateBean> balanceDateBeanStream = balanceDateBeans.stream().filter(x -> StringUtils.equals(x.getReportDate(), profitDateBean.getReportDate())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(balanceDateBeanStream)) {
            BalanceDateBean balanceDateBean = balanceDateBeanStream.get(0);
            Double aDouble = NumUtils.stringToDouble(balanceDateBean.getTotalCurrentLiabilities());
            Double aDouble1 = NumUtils.stringToDouble(balanceDateBean.getTotalNonCurrentLiabilities());
            return NumUtils.roundDouble(aDouble1 / aDouble * 100);
        }
        return 0;
    }

    private static double getCashFlow(ProfitDateBean profitDateBean, List<CashFlowBean> cashFlowBeans) {
        List<CashFlowBean> collect = cashFlowBeans.stream().filter(x -> StringUtils.equals(x.getReportDate(), profitDateBean.getReportDate())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            CashFlowBean cashFlowBean = collect.get(0);
            return NumUtils.roundDouble(NumUtils.stringToDouble(cashFlowBean.getNetCashFlowFromOperatingActivities()) / 10000);
        }
        return 0;
    }

}
