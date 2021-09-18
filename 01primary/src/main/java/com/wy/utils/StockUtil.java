package com.wy.utils;

import com.wy.chromedriver.PerfitConstant;
import com.wy.chromedriver.PerfitDataEnter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yunwang on 2021/9/18 14:08
 */
public class StockUtil {

    //盈利能力抓取路径
    private static String URL_YLNL = "http://quotes.money.163.com/service/zycwzb_%s.html?type=season&part=ylnl";
    //磁盘路径
    private static String PATH = "d:\\stockDetail\\";
    //文件名称
    private static String FILE_NAME_YLNL = "%sylnl.csv";

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(PerfitConstant.threadNum, PerfitConstant.threadNum, 5, TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("StockUtil-pool-%d").daemon(true).build());

    public static void main(String[] args) {
        String stockCode = "600519";
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

        System.out.println("total:" + codeList.size());

        threadPoolExecutor.prestartAllCoreThreads();
        for (String code : codeList) {
            threadPoolExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + ":" + code);
                //爬取数据
                try {
                    getStockContent(StringUtils.trim(code), String.format(FILE_NAME_YLNL, StringUtils.trim(code)));
//                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println(code);
                }
            });
        }

        try {
            System.in.read(); //阻塞主线程
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Thread.sleep(600000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    //获取单个票CVS文件
    private static void getStockContent(String stockCode, String fileName) {
        //1.生成URL
        String urlformat = String.format(URL_YLNL, stockCode);
        //2.获取数据
        String temp = getResultClasses(urlformat);
        //3.保存文件
        if (temp.isEmpty()) {
            System.out.println(stockCode + "数据库空");
            return;
        }
        try {
            FilesUtil.writeFile(PATH, fileName, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
