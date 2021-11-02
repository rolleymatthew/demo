package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.ConstantBean;
import com.wy.bean.Contant;
import com.wy.bean.FinanceDataBean;
import com.wy.chromedriver.PerfitConstant;
import com.wy.utils.AllStock;
import com.wy.utils.ClassUtil;
import com.wy.utils.OkHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/25 17:28
 */
public class FinanceDateWriteService {
    //盈利能力抓取路径
    private static String URL_DOMAIN = "http://quotes.money.163.com/";
    private static String URL_YLNL = "service/zycwzb_%s.html?type=season&part=ylnl";
    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";
    //磁盘路径
    public static String PATH_MAIN = Contant.DIR+File.separator+"financeStock";
    public static String PATH_YLNL = "ylnlSeason";
    public static String PATH_ZYCWZB_REPORT = "zycwzbReport";
    public static String PATH_ZYCWZB_SEASON = "zycwzbSeason";
    //文件名称
    public static String FILE_NAME_PRE = "zycwReport";
    public static String FILE_NAME_EXT = Contant.FILE_EXT;
    public static String FILE_NAME_REPORT = FILE_NAME_PRE + "%s" + FILE_NAME_EXT;

    public static void main(String[] args) {
        List<String> allCodes = getAllCodes();

        getFinanceData(allCodes);
    }

    public static void getFinanceData(List<String> allCodes) {
        allCodes.parallelStream().forEach(x ->
                getZYCWZBContent(StringUtils.trim(x)
                        , PATH_MAIN + File.separator + PATH_ZYCWZB_REPORT + File.separator + String.format(FILE_NAME_REPORT, StringUtils.trim(x))));
    }

    private static void getZYCWZBContent(String stockCode, String fileName) {
        //1.生成URL
        String urlformat = String.format(URL_DOMAIN + URL_ZYCWZB_REPORT, stockCode);
        //2.获取数据
        String temp = FinanceSpider.getResultClasses(urlformat);
        //3.保存文件
        if (StringUtils.isEmpty(temp)) {
            System.out.println(stockCode + "数据库空");
            return;
        }

        //读取行
        //使用二维数组转置方法转化bean
        List<FinanceDataBean> beanList = getFinanceDataBeans(temp);
        EasyExcel.write(fileName, FinanceDataBean.class)
                .sheet(stockCode)
                .doWrite(beanList);
    }

    /**
     * 读取数据并格式化成需要的bean
     *
     * @param temp
     * @return
     */
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

    public static List<String> getAllCodes() {
        List<String> codeList = new ArrayList<>();
        String[] codes = AllStock.SH_MAIN.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }

        codes = AllStock.SH_KC.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }

        codes = AllStock.SZ.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }
        return codeList;
    }

}
