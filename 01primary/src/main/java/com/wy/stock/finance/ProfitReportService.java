package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.FinThreePerBean;
import com.wy.bean.ProfitDateBean;
import com.wy.utils.DateUtil;
import com.wy.utils.NumUtils;
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
            //金融企业计算方式
        }
        return NumUtils.stringToDouble(s.getOperatingIncome());
    }

    private static Double cost(ProfitDateBean s) {
        if (StringUtils.equalsIgnoreCase(s.getOperatingCost(), "--")) {
            //金融企业计算方式
        }
        return NumUtils.stringToDouble(s.getOperatingCost());
    }

    public static void main(String[] args) {
        List<String> alCodes=new ArrayList<>();
        alCodes.add("000001");
        alCodes.add("000002");
        //获取最近一年的数据
        Map<String, List<ProfitDateBean>> financeListMap = FinanceCommonService.getFinanceListMap(alCodes)
                .entrySet().stream().filter(x->x.getValue().size()>=5).collect(Collectors.toMap(s->s.getKey(),s->s.getValue()));;
        //计算环比同比数据
        financeListMap.entrySet().stream().forEach(s->{
            String key = s.getKey();
            List<ProfitDateBean> value = s.getValue();
            value.stream().filter(x->StringUtils.equals(x.getReportDate(),DateUtil.fmtShortDate(DateUtil.getLastQuarterEndTime())))
                    .collect(Collectors.toList());
            ;
        });
    }

}
