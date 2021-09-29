package com.wy.utils;

import ch.qos.logback.core.helpers.CyclicBuffer;
import com.wy.chromedriver.PerfitConstant;
import com.wy.chromedriver.PerfitDataEnter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/9/18 14:08
 */
public class StockUtil {

    //盈利能力抓取路径
    private static String URL_DOMAIN = "http://quotes.money.163.com/";
    private static String URL_YLNL = "service/zycwzb_%s.html?type=season&part=ylnl";
    private static String URL_ZYCWZB_REPORT = "service/zycwzb_%s.html?type=report";
    //磁盘路径
    private static String PATH_MAIN = "d:\\stockDetail\\";
    private static String PATH_YLNL = "ylnl\\";
    private static String PATH_ZYCWZB = "zycwzb\\";
    //文件名称
    private static String FILE_NAME = "%s.csv";

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(PerfitConstant.threadNum, PerfitConstant.threadNum, 5, TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("StockUtil-pool-%d").daemon(true).build());

    public static void main(String[] args) {
        //获取数据到文件
        spiderContent();

        //读文件计算数据
        String path = PATH_MAIN + PATH_ZYCWZB;
        List<String> filesOfDictory = FilesUtil.getFilesOfDictory(path);
//        filesOfDictory.stream().forEach(System.out::println);
        filesOfDictory.stream().forEach(file -> {
//            System.out.println(file);
            try {
                List<String> fileContent = FilesUtil.readFileAsListOfStrings(path + file, "GBK");
                //过滤不合格的行
                List<String> collect = fileContent.stream().filter(c ->c.split(",").length >= 2).collect(Collectors.toList());
//                Optional<String> first = collect.stream().findFirst();
//                if (first.isPresent()){
//                    String date = first.get();
//                    System.out.println(date);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void spiderContent() {
//        List<String> codeList = getSomeCodes();
        List<String> codeList = getAllCodes();
        List<String> errorList = new ArrayList<>();
        System.out.println("total:" + codeList.size());

        threadPoolExecutor.prestartAllCoreThreads();
        long l = System.currentTimeMillis();
        for (String code : codeList) {
            threadPoolExecutor.execute(() -> {
//                System.out.println(Thread.currentThread().getName() + ":" + code);
                //爬取数据
                try {
                    getContent(code);
                } catch (Exception e) {
                    errorList.add(code);
//                    System.out.println(code);
                }
            });
        }

        System.out.println("超时:"+errorList.size());
        for (String s : errorList) {
            getContent(s);
        }

        try {
            while (!threadPoolExecutor.awaitTermination(6, TimeUnit.SECONDS)) {

                int activeCount = threadPoolExecutor.getActiveCount();
                if (activeCount == 0) {
                    System.out.println("system terminated times.." + (System.currentTimeMillis() - l) / 1000 + "s");
                    threadPoolExecutor.shutdownNow();
                    System.exit(0);
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getContent(String code) {
        getZYCWZBContent(StringUtils.trim(code), String.format(FILE_NAME, StringUtils.trim(code)));
        getYLNLContent(StringUtils.trim(code), String.format(FILE_NAME, StringUtils.trim(code)));
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

    private static List<String> getSomeCodes() {
        List<String> codeList = new ArrayList<>();
        String[] codes = AllStock.SH_MAIN_.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }

        return codeList;
    }

    //获取单个票CVS文件
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
        try {
            FilesUtil.writeFile(PATH_MAIN + PATH_YLNL, fileName, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            FilesUtil.writeFile(PATH_MAIN + PATH_ZYCWZB, fileName, temp);
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
