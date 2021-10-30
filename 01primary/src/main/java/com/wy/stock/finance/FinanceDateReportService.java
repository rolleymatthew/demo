package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.FinanceDataBean;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yunwang
 * @Date 2021-10-27
 */
public class FinanceDateReportService {
    public static void main(String[] args) {
        List<String> allCodes = FinanceDateWriteService.getAllCodes();
        //读取文件
        Map<String, List<FinanceDataBean>> dataMap = getFinanceListMap(allCodes);

        dataMap.entrySet().forEach(x -> System.out.println(x));

    }

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
