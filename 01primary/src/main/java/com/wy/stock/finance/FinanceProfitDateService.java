package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.ConstantBean;
import com.wy.bean.Contant;
import com.wy.bean.FinanceDataBean;
import com.wy.bean.ProfitDateBean;
import com.wy.utils.ClassUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yunwang
 * @Date 2021-11-02
 */
public class FinanceProfitDateService {
        private static String URL_LRB_REPORT = "service/lrb_%s.html";
//    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";
    public static String FILE_NAME_PRE = "profit";
    public static String FILE_NAME_EXT = Contant.FILE_EXT;
    public static String FILE_NAME_REPORT = FILE_NAME_PRE + "%s" + FILE_NAME_EXT;
    private static String PATH = FinanceCommonService.PATH_MAIN + File.separator + FILE_NAME_PRE + File.separator;

    public static void main(String[] args) {
        List<String> allCodes = FinanceCommonService.getAllCodes(true);
        getFinanceData(allCodes);
    }

    public static void getFinanceData(List<String> allCodes) {
        allCodes.parallelStream().forEach(x ->
                getBeansByCode(StringUtils.trim(x),
                        PATH + String.format(FILE_NAME_REPORT, StringUtils.trim(x))));
    }

    private static void getBeansByCode(String stockCode,String fileName) {
        //1.生成URL
        String urlformat = String.format(FinanceSpider.URL_DOMAIN + URL_LRB_REPORT, stockCode);
        //2.获取数据
        String temp = FinanceSpider.getResultClasses(urlformat);
        if (StringUtils.isEmpty(temp)) {
            System.out.println(stockCode + "数据库空");
            return;
        }

        //3.转成bean
        List<ProfitDateBean> beanList = getFinanceDataBeans(temp,ConstantBean.LRB_DIC)
                .stream().filter(f-> StringUtils.isNotEmpty(f.getReportDate())).collect(Collectors.toList());
        //4.保存文件
        EasyExcel.write(fileName, ProfitDateBean.class)
                .sheet(stockCode)
                .doWrite(beanList);
    }

    private static List<ProfitDateBean> getFinanceDataBeans(String temp, Map<String,String> dicMap) {
        List<ProfitDateBean> ret = new ArrayList<>();
        //1.拆分出行，抽取表头数据,计算出行数，对应BEAN的属性
        List<String> stringList = FinanceCommonService.getStringList(temp);
        if (CollectionUtils.isEmpty(stringList)) return null;
        List<String> header = FinanceCommonService.getHeader(stringList);
        if (CollectionUtils.isEmpty(header)) return null;

        //2.产生二维数组
        String[][] newData = FinanceCommonService.getArrayDates(temp);

        //3.使用类的反射机制把生成的二维数组转化成需要的bean列表，返回
        int columnLen = FinanceCommonService.getColumnLen(stringList);
        for (int line = 0; line < columnLen; line++) {
            ProfitDateBean financeDataBean = new ProfitDateBean();
            for (int col = 0; col < header.size(); col++) {
                ClassUtil.setFieldValueByFieldName(financeDataBean, dicMap.get(header.get(col)), newData[line][col]);
            }
            ret.add(financeDataBean);

        }
        return ret;
    }
}
