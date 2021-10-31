package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.FinThreePerBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.NumUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
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
        List<String> allCodes = FinanceDateWriteService.getAllCodes();
        //读取文件
        Map<String, List<FinanceDataBean>> dataMap = getFinanceListMap(allCodes);

        //计算所有的三率Map
        Map<String, List<FinThreePerBean>> finPerMap = dataMap.entrySet().stream().collect(Collectors.toMap(x -> x.getKey()
                , x -> {
                    List<FinanceDataBean> collect = x.getValue().stream().collect(Collectors.toList());
                    List<FinThreePerBean> threePerBeanList = collect.stream().map(s -> {
                        FinThreePerBean finThreePerBean = new FinThreePerBean();
                        Double mainBusiIncome = NumUtils.stringToDouble(s.getMainBusiIncome());
                        finThreePerBean.setGrossProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getMainBusiProfit()) / mainBusiIncome * 100));
                        finThreePerBean.setOperatProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getOperatProfit()) / mainBusiIncome * 100));
                        finThreePerBean.setNetProfit(NumUtils.roundDouble(NumUtils.stringToDouble(s.getNetProfit()) / mainBusiIncome * 100));
                        return finThreePerBean;
                    }).collect(Collectors.toList());
                    return threePerBeanList;
                }));

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
                        dataMap.put(s, dataList);
                    })).sheet(0).doRead();
        });
        return dataMap;
    }

}
