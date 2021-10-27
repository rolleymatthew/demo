package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.wy.chromedriver.PerfitConstant;
import com.wy.utils.AllStock;
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
public class FinanceDateService {
    //盈利能力抓取路径
    private static String URL_DOMAIN = "http://quotes.money.163.com/";
    private static String URL_YLNL = "service/zycwzb_%s.html?type=season&part=ylnl";
    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";
    //磁盘路径
    private static String PATH_MAIN = "d:\\financeStock";
    private static String PATH_YLNL = "ylnlSeason";
    private static String PATH_ZYCWZB_REPORT = "zycwzbReport";
    private static String PATH_ZYCWZB_SEASON = "zycwzbSeason";
    //文件名称
    private static String FILE_NAME_REPORT = "zycwReport%s.xlsx";

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(PerfitConstant.threadNum, PerfitConstant.threadNum, 5, TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("StockUtil-pool-%d").daemon(true).build());

    public static void main(String[] args) {
        List<String> allCodes = getAllCodes();

//        getYLNLContent(StringUtils.trim("000001"), String.format(FILE_NAME, StringUtils.trim("000001")));
        allCodes.parallelStream().forEach(x ->
                getZYCWZBContent(StringUtils.trim(x)
                        , PATH_MAIN + File.separator + PATH_ZYCWZB_REPORT + File.separator + String.format(FILE_NAME_REPORT, StringUtils.trim(x))));
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

    }

    private static List<FinanceDataBean> getFinaByArray(String[][] cell, int line, int column) {
        List<FinanceDataBean> ret = new ArrayList<>();
        for (int i = 0; i < line; i++) {
            FinanceDataBean financeDataBean = new FinanceDataBean();
            for (int j = 0; j < column; j++) {
                String data = cell[i][j];
                switch (j + 1) {
                    case 1:
                        financeDataBean.setReportDate(data);
                        break;
                    case 2:
                        financeDataBean.setBasePerShare(data);
                        break;
                    case 3:
                        financeDataBean.setValuePerShare(data);
                        break;
                    case 4:
                        financeDataBean.setCashPerShare(data);
                        break;
                    case 5:
                        financeDataBean.setMainBusiIncome(data);
                        break;
                    case 6:
                        financeDataBean.setMainBusiProfit(data);
                        break;
                    case 7:
                        financeDataBean.setOperatProfit(data);
                        break;
                    case 8:
                        financeDataBean.setInvestIncome(data);
                        break;
                    case 9:
                        financeDataBean.setNotOperatIncome(data);
                        break;
                    case 10:
                        financeDataBean.setTotalProfit(data);
                        break;
                    case 11:
                        financeDataBean.setNetProfit(data);
                        break;
                    case 12:
                        financeDataBean.setRecurNetProfit(data);
                        break;
                    case 13:
                        financeDataBean.setCashFlowOpet(data);
                        break;
                    case 14:
                        financeDataBean.setCashValueIncr(data);
                        break;
                    case 15:
                        financeDataBean.setTotalAssets(data);
                        break;
                    case 16:
                        financeDataBean.setCurrentAssets(data);
                        break;
                    case 17:
                        financeDataBean.setTotalLiabil(data);
                        break;
                    case 18:
                        financeDataBean.setCurrentLiabil(data);
                        break;
                    case 19:
                        financeDataBean.setShareEquity(data);
                        break;
                    case 20:
                        financeDataBean.setNetAssetsWeight(data);
                        break;
                    default:
                }
            }
            ret.add(financeDataBean);
        }
        return ret;
    }

    private static void getZYCWZBContent(String stockCode, String fileName) {
        //1.生成URL
        String urlformat = String.format(URL_DOMAIN + URL_ZYCWZB_REPORT, stockCode);
        //2.获取数据
        String temp = getResultClasses(urlformat);
        //3.保存文件
        if (StringUtils.isEmpty(temp)) {
            System.out.println(stockCode + "数据库空");
            return;
        }

        //读取行
        //使用二维数组转置方法转化bean
        List<FinanceDataBean> beanList = getFinanceDataBeans(temp);
        EasyExcel.write(fileName, FinanceDataBean.class)
                .sheet("finance")
                .doWrite(beanList);
    }

    /**
     * 读取数据并格式化成需要的bean
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

        //把数据赋值到一个二维数组orgData里
        fillString(collect, lineLen, columnLen, orgData);

        //用第二个二维数组newData实现行转列
        lineToColumn(lineLen, columnLen, orgData, newData);

        //把生成的二维数组转化成需要的bean列表，返回
        List<FinanceDataBean> beanList = getFinaByArray(newData, columnLen, lineLen);
        return beanList;
    }

    private static void lineToColumn(int lineLen, int columnLen, String[][] orgData, String[][] newData) {
        //行转列
        for (int i = 0; i < lineLen; i++) {
            for (int j = 0; j < columnLen; j++) {
                newData[j][i] = orgData[i][j];
            }

        }
    }

    private static void fillString(List<String> collect, int lineLen, int columnLen, String[][] orgData) {
        //二维数组赋值
        for (int i = 0; i < lineLen; i++) {
            String s = collect.get(i);
            String[] split = s.split(",");
            for (int j = 1; j < columnLen; j++) {
                String s2 = split[j];
                orgData[i][j - 1] = s2;
            }
        }
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
