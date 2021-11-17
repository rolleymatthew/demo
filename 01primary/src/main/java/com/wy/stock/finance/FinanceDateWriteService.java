package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.ConstantBean;
import com.wy.bean.Contant;
import com.wy.bean.FinanceDataBean;
import com.wy.service.impl.StockServiceImpl;
import com.wy.utils.ClassUtil;
import com.wy.utils.FilesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/25 17:28
 */
public class FinanceDateWriteService {
    private static Logger logger = LoggerFactory.getLogger(FinanceDateWriteService.class);
    //盈利能力抓取路径
    private static String URL_DOMAIN = "http://quotes.money.163.com/";
    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";
    //磁盘路径
    public static String PATH_ZYCWZB_REPORT = "zycwzbReport";
    //文件名称
    public static String FILE_NAME_PRE = "zycwReport";
    public static String FILE_NAME_EXT = Contant.FILE_EXT;
    public static String FILE_NAME_REPORT = FILE_NAME_PRE + "%s" + FILE_NAME_EXT;
    private static final String PATH = FinanceCommonService.PATH_MAIN + File.separator + PATH_ZYCWZB_REPORT + File.separator;

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
        getZYCWZBContent(stockCode, PATH + String.format(FILE_NAME_REPORT, StringUtils.trim(stockCode)));
    }

    private static void getZYCWZBContent(String stockCode, String fileName) {
        List<FinanceDataBean> financeDataBeans = getFinanceDataBeanList(stockCode);
        if (financeDataBeans == null) return;
        outputExcle(stockCode, fileName, financeDataBeans);
    }

    public static void outputExcle(String stockCode, String fileName, List<FinanceDataBean> financeDataBeans) {
        EasyExcel.write(fileName, FinanceDataBean.class)
                .sheet(stockCode)
                .doWrite(financeDataBeans);
    }

    public static List<FinanceDataBean> getFinanceDataBeanList(String stockCode) {
        //1.生成URL
        String urlformat = String.format(URL_DOMAIN + URL_ZYCWZB_REPORT, stockCode);
        //2.获取数据
        String temp = FinanceSpider.getResultClasses(urlformat);
        //3.保存文件
        if (StringUtils.isEmpty(temp)) {
            logger.info(stockCode + "数据库空");
            return null;
        }

        //读取行
        //使用二维数组转置方法转化bean
        List<Object> beanList = FinanceCommonService.convertStringToBeans(temp, ConstantBean.ZYCWZB_DIC, FinanceDataBean.class);
        if (CollectionUtils.isEmpty(beanList)) {
            logger.info("getZYCWZBContent convertStringToBeans error : {}", stockCode);
            return null;
        }
        List<FinanceDataBean> financeDataBeans = beanList.stream().map(x -> {
                    FinanceDataBean financeDataBean = new FinanceDataBean();
                    BeanUtils.copyProperties(x, financeDataBean);
                    return financeDataBean;
                }).filter(s -> StringUtils.isNotEmpty(s.getReportDate())).sorted(Comparator.comparing(FinanceDataBean::getReportDate).reversed())
                .collect(Collectors.toList());
        return financeDataBeans;
    }
}
