package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.BalanceDateBean;
import com.wy.bean.CashFlowBean;
import com.wy.bean.Contant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/11/5 15:44
 */
public class FinanceCashFlowDateService {
    private static String URL_LRB_REPORT = "service/xjllb_%s.html";
    public static String FILE_NAME_PRE = "cashflow";
    public static String FILE_NAME_EXT = Contant.FILE_EXT;
    public static String FILE_NAME_REPORT = FILE_NAME_PRE + "%s" + FILE_NAME_EXT;
    private static String PATH = FinanceCommonService.PATH_MAIN + File.separator + FILE_NAME_PRE + File.separator;


    public static void main(String[] args) {
        List<String> allCodes = FinanceCommonService.getAllCodes(false);
        getFinanceData(allCodes);
    }

    public static void getFinanceData(List<String> allCodes) {
        allCodes.parallelStream().forEach(x ->
                getBeansByCode(StringUtils.trim(x),
                        PATH + String.format(FILE_NAME_REPORT, StringUtils.trim(x))));
    }

    private static void getBeansByCode(String stockCode, String fileName) {
        //1.生成URL
        String urlformat = String.format(FinanceSpider.URL_DOMAIN + URL_LRB_REPORT, stockCode);
        //2.获取数据
        String temp = FinanceSpider.getResultClasses(urlformat);
        if (StringUtils.isEmpty(temp)) {
            System.out.println("error:" + stockCode + "现金流量表数据空");
            return;
        }
        //3.转成bean
        List<Object> beanList = FinanceCommonService.convertStringToBeans(temp, FinanceCommonService.CashFlowDicMap, CashFlowBean.class);
        List<CashFlowBean> profitDateBeanList = beanList.stream().map(x -> {
            CashFlowBean profitDateBean = new CashFlowBean();
            BeanUtils.copyProperties(x, profitDateBean);
            return profitDateBean;
        }).filter(f -> StringUtils.isNotEmpty(f.getReportDate())).sorted(Comparator.comparing(CashFlowBean::getReportDate).reversed())
                .collect(Collectors.toList());

        //4.保存文件
        EasyExcel.write(fileName, CashFlowBean.class)
                .sheet(stockCode)
                .doWrite(profitDateBeanList);
    }

}