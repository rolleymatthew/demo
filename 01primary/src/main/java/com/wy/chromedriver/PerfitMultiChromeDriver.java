package com.wy.chromedriver;

import com.wy.utils.FilesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author yunwang
 * @Date 2020-12-04
 */
public class PerfitMultiChromeDriver {
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
    }

    private static ConcurrentHashMap<String, String> codeUrl = new ConcurrentHashMap<String, String>();

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(PerfitConstant.threadNum, PerfitConstant.threadNum, 100, TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("perfit-fixed-pool-%d").daemon(true).build());

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        getAllMarket();
//        getTestCode();

        PerfitPoolChromeDriver perfitPoolChromeDriver = new PerfitPoolChromeDriver();
//        perfitPoolChromeDriver.setIsRemote("remote");
        perfitPoolChromeDriver.initDriver();

        CountDownLatch countDownLatch = new CountDownLatch(codeUrl.size());
        for (Map.Entry<String, String> stringStringEntry : codeUrl.entrySet()) {

            threadPoolExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + ":" + stringStringEntry.getValue());
                //爬取数据
                try{
                    PerfitDataEnter perfitDataList = spider(perfitPoolChromeDriver, stringStringEntry);
                    if (perfitDataList == null || perfitDataList.getPerfitDataList().isEmpty()) {
                        System.out.println("date empty:" + stringStringEntry.getValue());
                    }
                }catch (Exception e){
                    System.out.println(stringStringEntry.getKey()+","+stringStringEntry.getValue()+e.toString());
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            perfitPoolChromeDriver.closeWebDriverConn();
            long t2 = System.currentTimeMillis();
            System.out.println("succes, " + (t2 - t1) / 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getTestCode() {
        for (String s : PerfitConstant.testUrlList) {
            codeUrl.put(s, PerfitConstant.getUrl(PerfitConstant.SH, s));
        }
        System.out.println(codeUrl.size());
    }

    private static void getStringBuilder3Per(StringBuilder stringBuilder, PerfitData perfitData) {
        stringBuilder.append("," + perfitData.getPerfit3UpPercent().getMarginRate() + ",");
        stringBuilder.append(perfitData.getPerfit3UpPercent().getMarginOperatingRate() + ",");
        stringBuilder.append(perfitData.getPerfit3UpPercent().getNetProfitRate() + ",");
    }

    private static void getAllMarket() {
        for (String s : PerfitConstant.shCodeList) {
            codeUrl.put(s, PerfitConstant.getUrl(PerfitConstant.SH, s));
        }
        for (String s : PerfitConstant.szCodeList) {
            codeUrl.put(s, PerfitConstant.getUrl(PerfitConstant.SZ, s));
        }
        System.out.println(codeUrl.size());
    }

    private static PerfitDataEnter spider(PerfitPoolChromeDriver perfitPoolChromeDriver,
                                          Map.Entry<String, String> stringStringEntry) throws Exception{
        WebDriver driver = perfitPoolChromeDriver.getWebDriverConn();
//        perfitPoolChromeDriver.getWebDriverContent(stringStringEntry.getValue(),driver);
        driver.get(stringStringEntry.getValue());
        //1.按照xPath点击第一页driver
//        getFirstPage(driver, "//*[@id=\"lrb_ul\"]/li[3]/span");
        getFirstPage(driver, "//*[@id=\"lrb_a\"]");

        //2.得到第一页的页面元素
        List<WebElement> element = getElementsByTr(driver, "//*[@id=\"report_lrb_table\"]/tbody/tr");
//        List<WebElement> element = getElementsByTr(driver, "//*[@id=\"report_lrb\"]/tbody/tr");

        //3.获取详细数据到bean数组
        PerfitDataEnter perfitDataEnter = setPerfitList(element, stringStringEntry.getKey(), 0);

        //4.加工CVS文件格式
        ExportCVS(stringStringEntry, perfitDataEnter);

        //5.释放资源
        perfitPoolChromeDriver.releaseWebDriverConn(driver);
        removeUrl(stringStringEntry.getKey());
        return perfitDataEnter;
    }

    private static Perfit3UpPercent getPerfit3UpPercent(PerfitData perfitData) {

        Perfit3UpPercent perfit3UpPercent = new Perfit3UpPercent();

        double s1 = 0.0;
        double s2 = 0.0;
        double a1 = 0.0;
        double a2 = 0.0;
        double a3 = 0.0;
        try {
            //s1:营业收入
            s1 = PerfitConstant.getDouble(StringUtils.replace(perfitData.getOperateIncome(), "--", ""));
            //s2:营业总成本
            s2 = PerfitConstant.getDouble(StringUtils.replace(perfitData.getTotalExpenses(), "--", ""));
            //a1:营业成本
            a1 = PerfitConstant.getDouble(StringUtils.replace(perfitData.getOperatingCost(), "--", ""));
            //a2:营业利润
            a2 = PerfitConstant.getDouble(StringUtils.replace(perfitData.getOperatingProfit(), "--", ""));
            //a3:扣非净利润
            a3 = PerfitConstant.getDouble(StringUtils.replace(perfitData.getNetProfitNotParent(), "--", ""));
        } catch (Exception e) {
            System.out.println("double parse error:" + perfitData.getCode());
        }

        //1.毛利率=(营业收入-销售费用)/营业收入,销售费用如果没有就用营业成本
        double marginRate = (s1 - a1) / s1 * 100;
        if (a1 == 0) {
            marginRate = (s1 - s2) / s1 * 100;
        }
        perfit3UpPercent.setMarginRate(marginRate);

        //2.营业利润率=(营业收入-销售费用-营业成本)(营业利润)/营业收入
        double marginOperatingRate = a2 / s1 * 100;
        perfit3UpPercent.setMarginOperatingRate(marginOperatingRate);

        //3.净利率=(营业收入-销售成本-营业成本+-营业外收支)(扣非净利润)/营业收入
        double netProfitRate = a3 / s1 * 100;
        perfit3UpPercent.setNetProfitRate(netProfitRate);
        return perfit3UpPercent;
    }

    private static void ExportCVS(Map.Entry<String, String> stringStringEntry, PerfitDataEnter perfitDataEnter) {
        if (perfitDataEnter == null) {
            //不能是空数据
            return;
        }
        if (perfitDataEnter.getTltleNameList().isEmpty()) {
            //标题不能为空
            return;
        }
        if (perfitDataEnter.getPerfitDataList().isEmpty()) {
            //数据不能为空
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder titleStringBuilder = new StringBuilder();
        for (String s : perfitDataEnter.getTltleNameList()) {
            titleStringBuilder.append(s + ",");
        }
        String title = titleStringBuilder.toString();
        title = "代码,日期," + title.substring(0, title.length() - 1) + ",毛利率,营业利润率,净利率\n";
        stringBuilder.append(title);
        for (PerfitData perfitData : perfitDataEnter.getPerfitDataList()) {
            getStringBuilder(stringBuilder, perfitData);
            getStringBuilder3Per(stringBuilder, perfitData);
            stringBuilder.append("\n");
        }
        try {
            FilesUtil.writeFile("d:\\profit", stringStringEntry.getKey() + ".csv", stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getStringBuilder(StringBuilder stringBuilder, PerfitData perfitData) {
        stringBuilder.append("'" + perfitData.getCode()
                + getCSVText(perfitData.getReportdate())
                + getCSVText(perfitData.getTotalOperateIncome())
                + getCSVText(perfitData.getOperateIncome())
                + getCSVText(perfitData.getInterestIncome())
                + getCSVText(perfitData.getTotalExpenses())
                + getCSVText(perfitData.getOperatingCost())
                + getCSVText(perfitData.getRAndDExpenses())
                + getCSVText(perfitData.getSalesTaxExtra())
                + getCSVText(perfitData.getSellingExpenses())
                + getCSVText(perfitData.getManagerExpenses())
                + getCSVText(perfitData.getFinancialExpenses())
                + getCSVText(perfitData.getFairValue())
                + getCSVText(perfitData.getInvestmentIncome())
                + getCSVText(perfitData.getVenturesIncome())
                + getCSVText(perfitData.getOperatingProfit())
                + getCSVText(perfitData.getNotOperatingIncome())
                + getCSVText(perfitData.getNotOperatingExpenses())
                + getCSVText(perfitData.getTotalProfit())
                + getCSVText(perfitData.getIncomeTaxExpense())
                + getCSVText(perfitData.getParentNetprofit())
                + getCSVText(perfitData.getNetProfitNotParent())
        );
    }

    public static void removeUrl(String code) {
        codeUrl.remove(code);
    }

    private static void getFirstPage(WebDriver driver, String xPath) {
        WebElement searchBtn = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));;
        Actions actionProvider = new Actions(driver);

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        actionProvider.moveToElement(searchBtn).click().build().perform();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<WebElement> getElementsByTr(WebDriver driver, String xpath) throws Exception{
        return driver.findElements(By.xpath(xpath));
    }

    private static PerfitDataEnter setPerfitList(List<WebElement> element, String code, int count) {
        if (CollectionUtils.isEmpty(element)) {
            return null;
        }

        List<PerfitData> perfitDataList = new ArrayList<>();
        List<WebElement> th = element.get(0).findElements(By.tagName("th"));
        HashMap<Integer, PerfitData> perfitDataHashMap = new HashMap<>();
        List<String> titleNameList = new ArrayList<String>();
        try {
            //1.先对位置
            for (int i = 1; i < th.size(); i++) {
                PerfitData perfitData = new PerfitData();
                perfitData.setCode(code);
                perfitData.setReportdate(th.get(i).getText());
                perfitDataHashMap.put(i, perfitData);
            }
            //2.填数据,按照位置
            for (int i = 1; i < element.size(); i++) {
                WebElement trElement = element.get(i);

                List<WebElement> webElement = trElement.findElements(By.tagName("td"));

                //过滤隐藏的表头
                if (StringUtils.isNotEmpty(trElement.getAttribute("hidden")) && trElement.getAttribute("hidden").equals("true")) {
                    continue;
                }

                int locationInt = 0;
                for (int j = 0; j < webElement.size(); j++) {
                    String text = webElement.get(j).getText();
                    //获取表头对应位置
                    if (j == 0 && count == 0) {
                        if (PerfitConstant.titleNameTotalMap.containsKey(text)) {
                            locationInt = PerfitConstant.titleNameTotalMap.get(text);
                            if (PerfitConstant.titleNameIndex.indexOf(","+locationInt+",") > -1) {
                                titleNameList.add(text);
                            }
                        } else {
                            System.out.println(code + "," + text);
                        }
                    }

                    //获取数据
                    if (perfitDataHashMap.containsKey(j)) {
                        PerfitData perfitData = perfitDataHashMap.get(j);
                        getTextOnPer(perfitData, text, locationInt);
                        perfitDataHashMap.put(j, perfitData);
                    }
                }

            }

        }catch (Exception e){
            throw e;
        }


        for (int i = 0; i <= perfitDataHashMap.size(); i++) {
            if (perfitDataHashMap.containsKey(i + 1)) {
                PerfitData perfitData = perfitDataHashMap.get(i + 1);
                perfitData.setPerfit3UpPercent(getPerfit3UpPercent(perfitData));
                perfitDataList.add(perfitData);
            }
        }
        //组装带标题的返回数据
        PerfitDataEnter perfitDataEnter = new PerfitDataEnter();
        perfitDataEnter.setPerfitDataList(perfitDataList);
        perfitDataEnter.setTltleNameList(titleNameList);

        return perfitDataEnter;
    }

    private static boolean getTextOnPer(PerfitData perfitData, String text, int i) {
        boolean ret = true;
        switch (i) {
            case 0:
                perfitData.setZhName(text);
                break;
            case 1:
                perfitData.setTotalOperateIncome(text);
                break;
            case 2:
                perfitData.setOperateIncome(text);
                break;
            case 3:
                perfitData.setInterestIncome(text);
                break;
            case 7:
                perfitData.setTotalExpenses(text);
                break;
            case 8:
                perfitData.setOperatingCost(text);
                break;
            case 11:
                perfitData.setRAndDExpenses(text);
                break;
            case 18:
                perfitData.setSalesTaxExtra(text);
                break;
            case 19:
                perfitData.setSellingExpenses(text);
                break;
            case 20:
                perfitData.setManagerExpenses(text);
                break;
            case 21:
                perfitData.setFinancialExpenses(text);
                break;
            case 24:
                perfitData.setFairValue(text);
                break;
            case 25:
                perfitData.setInvestmentIncome(text);
                break;
            case 26:
                perfitData.setVenturesIncome(text);
                break;
            case 28:
                perfitData.setOperatingProfit(text);
                break;
            case 29:
                perfitData.setNotOperatingIncome(text);
                break;
            case 31:
                perfitData.setNotOperatingExpenses(text);
                break;
            case 33:
                perfitData.setTotalProfit(text);
                break;
            case 34:
                perfitData.setIncomeTaxExpense(text);
                break;
            case 36:
                perfitData.setParentNetprofit(text);
                break;
            case 39:
                perfitData.setNetProfitNotParent(text);
                break;
            default:
                ret = false;
                break;
        }

        return ret;
    }

    private static String getCSVText(String code) {
        if (StringUtils.isEmpty(code)) {
            return StringUtils.EMPTY;
        }
        return "," + code;
    }

}
