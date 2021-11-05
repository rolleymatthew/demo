package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.ConstantBean;
import com.wy.bean.Contant;
import com.wy.bean.ProfitDateBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yunwang
 * @Date 2021-11-02
 */
public class FinanceProfitDateService {
    private static String URL_LRB_REPORT = "service/lrb_%s.html";
    public static String FILE_NAME_PRE = "profit";
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
            System.out.println("error:" + stockCode + "利润表数据空");
            return;
        }

        //3.转成bean
        List<Object> beanList = FinanceCommonService.convertStringToBeans(temp, ConstantBean.LRB_DIC, ProfitDateBean.class);
        List<ProfitDateBean> profitDateBeanList = beanList.stream().map(x -> {
                    ProfitDateBean profitDateBean = new ProfitDateBean();
                    BeanUtils.copyProperties(x, profitDateBean);
                    return profitDateBean;
                }).filter(f -> StringUtils.isNotEmpty(f.getReportDate())).sorted(Comparator.comparing(ProfitDateBean::getReportDate).reversed())
                .collect(Collectors.toList());

        //4.保存文件
        EasyExcel.write(fileName, ProfitDateBean.class)
                .sheet(stockCode)
                .doWrite(profitDateBeanList);
    }

}
