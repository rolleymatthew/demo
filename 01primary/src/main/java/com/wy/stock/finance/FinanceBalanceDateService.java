package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.BalanceDateBean;
import com.wy.bean.ConstantBean;
import com.wy.bean.Contant;
import com.wy.bean.ProfitDateBean;
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
public class FinanceBalanceDateService {
    private static Logger logger = LoggerFactory.getLogger(FinanceBalanceDateService.class);
    private static String URL_LRB_REPORT = "service/zcfzb_%s.html";
    public static String FILE_NAME_PRE = "balance";
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
        List<BalanceDateBean> profitDateBeanList = getBalanceDateBeanList(stockCode);
        if (profitDateBeanList == null) return;

        outputExcle(stockCode, fileName, profitDateBeanList);
    }

    public static void outputExcle(String stockCode, String fileName, List<BalanceDateBean> profitDateBeanList) {
        //????????????
        EasyExcel.write(fileName, BalanceDateBean.class)
                .sheet(stockCode)
                .doWrite(profitDateBeanList);
    }

    public static List<BalanceDateBean> getBalanceDateBeanList(String stockCode) {
        //1.??????URL
        String urlformat = String.format(FinanceSpider.URL_DOMAIN + URL_LRB_REPORT, stockCode);
        //2.????????????
        String temp = FinanceSpider.getResultClasses(urlformat);
        if (StringUtils.isEmpty(temp)) {
            logger.info("balance url empty error: {}", stockCode);
            return null;
        }

        //3.??????bean
        List<Object> beanList = FinanceCommonService.convertStringToBeans(temp, FinanceCommonService.BalanceDicMap, BalanceDateBean.class);
        if (CollectionUtils.isEmpty(beanList)) {
            logger.info("balance convertStringToBeans error : {}", stockCode);
        }

        //4.?????????????????????????????????????????????
        List<BalanceDateBean> profitDateBeanList = beanList.stream().map(x -> {
                    BalanceDateBean profitDateBean = new BalanceDateBean();
                    BeanUtils.copyProperties(x, profitDateBean);
                    return profitDateBean;
                }).filter(f -> StringUtils.isNotEmpty(f.getReportDate())).sorted(Comparator.comparing(BalanceDateBean::getReportDate).reversed())
                .collect(Collectors.toList());
        return profitDateBeanList;
    }

}
