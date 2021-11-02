package com.wy.stock.finance;

import com.wy.bean.ConstantBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.ClassUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        String stockCode="000001";
        String urlformat = String.format(FinanceSpider.URL_DOMAIN + URL_ZYCWZB_REPORT, stockCode);
        //2.获取数据
        String temp = FinanceSpider.getResultClasses(urlformat);

        List<FinanceDataBean> financeDataBeans = getFinanceDataBeans(temp);
        financeDataBeans.stream().forEach(f-> System.out.println(f.getReportDate()));
    }

    private static List<FinanceDataBean> getFinanceDataBeans(String temp) {
        //得到原始的行数据,计算出行数
        String[] line = temp.split("\r\n");
        List<String> collect = Arrays.stream(line).filter(x -> x.length() > 10).collect(Collectors.toList());
        int lineLen = collect.size();

        //得到第一个行数据计算出列数
        String s1 = collect.get(0);
        String[] cell = StringUtils.split(s1, ",");
        int columnLen = cell.length;

        //定义两个做行转列用的二维数组
        String[][] orgData = new String[lineLen][columnLen];
        String[][] newData = new String[columnLen][lineLen];

        //把数据赋值到一个二维数组orgData里,header放表头
        List<String> header = new ArrayList<>();
        FinanceCommonService.fillString(collect, lineLen, columnLen, orgData, header);

        //用第二个二维数组newData实现行转列
        FinanceCommonService.lineToColumn(lineLen, columnLen, orgData, newData);

        //把生成的二维数组转化成需要的bean列表，返回
        //使用类的反射机制
        List<FinanceDataBean> beanList = getFinaClassByArray(newData, columnLen, lineLen, header);
        return beanList;
    }

    private static List<FinanceDataBean> getFinaClassByArray(String[][] newData, int columnLen, int lineLen, List<String> header) {
        List<FinanceDataBean> ret = new ArrayList<>();
        for (int i = 0; i < columnLen; i++) {
            FinanceDataBean financeDataBean = new FinanceDataBean();
            for (int h = 0; h < header.size(); h++) {
                String s = header.get(h);
                String s1 = ConstantBean.DIC.get(s);
                ClassUtil.setFieldValueByFieldName(financeDataBean, s1, newData[i][h]);
            }
            ret.add(financeDataBean);

        }

        return ret;
    }

}
