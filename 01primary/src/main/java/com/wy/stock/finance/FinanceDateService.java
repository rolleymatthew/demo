package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.chromedriver.PerfitConstant;
import com.wy.utils.AllStock;
import com.wy.utils.FilesUtil;
import com.wy.utils.OkHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yunwang on 2021/10/25 17:28
 */
public class FinanceDateService {
    //盈利能力抓取路径
    private static String URL_DOMAIN = "http://quotes.money.163.com/";
    private static String URL_YLNL = "service/zycwzb_%s.html?type=season&part=ylnl";
    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";
    //磁盘路径
    private static String PATH_MAIN = "d:\\financeStock";
    private static String PATH_YLNL = "ylnl";
    private static String PATH_ZYCWZB = "zycwzb";
    //文件名称
    private static String FILE_NAME = "finance%s.xlsx";

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(PerfitConstant.threadNum, PerfitConstant.threadNum, 5, TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("StockUtil-pool-%d").daemon(true).build());

    public static void main(String[] args) {
        List<String> allCodes = getAllCodes();

//        getYLNLContent(StringUtils.trim("000001"), String.format(FILE_NAME, StringUtils.trim("000001")));
        getZYCWZBContent(StringUtils.trim("000001"), PATH_MAIN + File.separator + String.format(FILE_NAME, StringUtils.trim("000001")));
    }

    private static void getYLNLContent(String stockCode, String fileName) {
        //1.生成URL
        String urlformat = String.format(URL_DOMAIN + URL_YLNL, stockCode);
        //2.获取数据
        String temp = getResultClasses(urlformat);
        //3.保存文件
        if (temp.isEmpty()) {
            System.out.println(stockCode + "数据库空");
            return;
        }
        String[] line = temp.split("\n");

        List<FinanceDataBean> beanList = new ArrayList<>();
        for (String s : line) {
            if (StringUtils.length(s) <= 10) {
                continue;
            }
            String[] cell = StringUtils.split(s, ",");
            FinanceDataBean financeBean = getFinanceBean(cell);
            beanList.add(financeBean);
        }
        EasyExcel.write(fileName, FinanceDataBean.class)
                .sheet("模板")
                .doWrite(beanList);

    }

    private static FinanceDataBean getFinanceBean(String[] cell) {
        FinanceDataBean financeDataBean = new FinanceDataBean();
        for (int i = 1; i < cell.length; i++) {
            String data = cell[i];
            switch (i) {
                case 1:
                    financeDataBean.setReportDate(data);
                    break;
                case 2:
                    financeDataBean.setBasePerShare(data);
                    break;
                default:
            }
        }
        return financeDataBean;
    }

    private static void getZYCWZBContent(String stockCode, String fileName) {
        //1.生成URL
        String urlformat = String.format(URL_DOMAIN + URL_ZYCWZB_REPORT, stockCode);
        //2.获取数据
        String temp = getResultClasses(urlformat);
        //3.保存文件
        if (temp.isEmpty()) {
            System.out.println(stockCode + "数据库空");
            return;
        }

        //读取行
        String[] line = temp.split("\n");
        //读取列
        TreeMap<String,FinanceDataBean> finMap=new TreeMap<>();
        for (String cells : line) {
            if (StringUtils.length(cells) <= 10) {
                continue;
            }
            String[] data = StringUtils.split(cells, ",");
//            if (finMap.containsKey()) {
//
//            }
            FinanceDataBean financeBean = getFinanceBean(data);
        }
        List<FinanceDataBean> beanList = new ArrayList<>();
        EasyExcel.write(fileName, FinanceDataBean.class)
                .sheet("finance")
                .doWrite(beanList);
    }

    private static List<String> getAllCodes() {
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

    /**
     * 获取网易数据
     *
     * @param url
     * @return
     */
    private static String getResultClasses(String url) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Cookie", "_ntes_nuid=76a91bb4dcad207df2a333e108b5d3d5; UM_distinctid=1788239a7fc1ac-0e34781bc02f84-53e356a-100200-1788239a7fd543; _ga=GA1.2.661645210.1617092526; _ntes_nnid=76a91bb4dcad207df2a333e108b5d3d5,1630396364778; _antanalysis_s_id=1631930382671; _gid=GA1.2.881378113.1631930384");
        headerMap.put("Host", "quotes.money.163.com");
        headerMap.put("Referer", "http://quotes.money.163.com/f10/zycwzb_601816,season.html");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Safari/537.36 SE 2.X MetaSr 1.0");
        String ss = OkHttpUtil.doGet(url, headerMap, "GBK");
        return ss;
    }

}
