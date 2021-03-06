package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.BalanceDateBean;
import com.wy.bean.CashFlowBean;
import com.wy.bean.Contant;
import com.wy.service.impl.StockServiceImpl;
import com.wy.utils.FilesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/11/5 15:44
 */
public class FinanceCashFlowDateService {
    private static Logger logger = LoggerFactory.getLogger(FinanceCashFlowDateService.class);
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
                getBeansByCode(StringUtils.trim(x)));
    }

    public static void getBeansByCode(String stockCode) {
        try {
            FilesUtil.mkdirs(PATH);
        } catch (IOException e) {
        }
        getBeansByCode(stockCode, PATH + String.format(FILE_NAME_REPORT, StringUtils.trim(stockCode)));
    }

    private static void getBeansByCode(String stockCode, String fileName) {
        List<CashFlowBean> cashFlowBeanList = getCashFlowBeanList(stockCode);
        if (CollectionUtils.isEmpty(cashFlowBeanList)) return;

        outputExcle(stockCode, fileName, cashFlowBeanList);
    }

    public static void outputExcle(String stockCode, String fileName, List<CashFlowBean> cashFlowBeanList) {
        //????????????
        EasyExcel.write(fileName, CashFlowBean.class)
                .sheet(stockCode)
                .doWrite(cashFlowBeanList);
    }

    public static List<CashFlowBean> getCashFlowBeanList(String stockCode) {
        //1.??????URL
        String urlformat = String.format(FinanceSpider.URL_DOMAIN + URL_LRB_REPORT, stockCode);
        //2.????????????
        String temp = FinanceSpider.getResultClasses(urlformat);
        if (StringUtils.isEmpty(temp)) {
            logger.info("cash flow error : {}", stockCode);
            return null;
        }
        //3.??????bean
        List<Object> beanList = FinanceCommonService.convertStringToBeans(temp, FinanceCommonService.CashFlowDicMap, CashFlowBean.class);
        if (CollectionUtils.isEmpty(beanList)) {
            logger.info("cash flow convertStringToBeans error : {}", stockCode);
            return null;
        }
        //4.??????????????????????????????????????????
        List<CashFlowBean> profitDateBeanList = beanList.stream().map(x -> {
                    CashFlowBean profitDateBean = new CashFlowBean();
                    BeanUtils.copyProperties(x, profitDateBean);
                    return profitDateBean;
                }).filter(f -> StringUtils.isNotEmpty(f.getReportDate())).sorted(Comparator.comparing(CashFlowBean::getReportDate).reversed())
                .collect(Collectors.toList());
        return profitDateBeanList;
    }

}
