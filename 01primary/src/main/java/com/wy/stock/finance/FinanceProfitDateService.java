package com.wy.stock.finance;

import com.wy.bean.ConstantBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.ClassUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yunwang
 * @Date 2021-11-02
 */
public class FinanceProfitDateService {
    //    private static String URL_ZYCWZB_REPORT = "service/lrb_%s.html";
    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";

    public static void main(String[] args) {
        //1.生成URL
        String stockCode = "000001";
        String urlformat = String.format(FinanceSpider.URL_DOMAIN + URL_ZYCWZB_REPORT, stockCode);
        //2.获取数据
        String temp = FinanceSpider.getResultClasses(urlformat);

        //3.转成bean
        List<FinanceDataBean> financeDataBeans = getFinanceDataBeans(temp,ConstantBean.ZYCWZB_DIC)
                .stream().filter(f-> StringUtils.isNotEmpty(f.getReportDate())).collect(Collectors.toList());
        financeDataBeans.stream().forEach(f -> System.out.println(f.getReportDate()));
    }

    private static List<FinanceDataBean> getFinanceDataBeans(String temp, Map<String,String> dicMap) {
        List<FinanceDataBean> ret = new ArrayList<>();
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
            FinanceDataBean financeDataBean = new FinanceDataBean();
            for (int col = 0; col < header.size(); col++) {
                ClassUtil.setFieldValueByFieldName(financeDataBean, dicMap.get(header.get(col)), newData[line][col]);
            }
            ret.add(financeDataBean);

        }
        return ret;
    }
}
