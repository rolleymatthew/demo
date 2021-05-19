package com.wy.chromedriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yunwang
 * @Date 2020-12-05
 */
public class PerfitPoolChromeDriver {
    private List<WebDriver> chromePool=new LinkedList<WebDriver>();

    private String isRemote="";

    public String getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(String isRemote) {
        this.isRemote = isRemote;
    }

    public PerfitPoolChromeDriver(){
    }

    public void initDriver() {
        for (int i = 0; i < PerfitConstant.DriverPoolSize; i++) {
            WebDriver driver =null;
            if (this.isRemote.equals("remote")){
                driver=getRemoteWebDriver();
            }else{
                driver=getWebDriver();
            }
            if (driver!=null){
                chromePool.add(driver);
            }
        }
    }

    private static WebDriver getWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        options.addArguments("blink-settings=imagesEnabled=false");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
        return new ChromeDriver(options);
    }

    private static WebDriver getRemoteWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        options.addArguments("blink-settings=imagesEnabled=false");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
        WebDriver webDriver =null;
        try {
            webDriver = new RemoteWebDriver(new URL(PerfitConstant.WEB_DRIVER_URL), options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return webDriver;
    }

    public synchronized WebDriver getWebDriverConn(){
        while (chromePool.isEmpty()){
            try {
                System.out.println("empty WebDriverConn");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WebDriver webDriver = chromePool.remove(0);
        return webDriver;
    }

    public synchronized void releaseWebDriverConn(WebDriver webDriver){
        chromePool.add(webDriver);
        this.notifyAll();
    }

    public synchronized void closeWebDriverConn(){
        for (WebDriver webDriver : chromePool) {
            webDriver.quit();
        }
    }

    /**
     * 多次获取
     * @param url
     * @param driver
     * @return
     */
    public WebDriver getWebDriverContent(String url, WebDriver driver) {
        boolean getFlag = true;
        int count = 3;
        do {
            try {
                driver.get(url);
                getFlag = false;
            } catch (Exception e) {
                driver.quit();
                driver = getRemoteWebDriver();
            }
            count--;
        } while (getFlag && (count > 0));

        if (getFlag&&(count<=0)){
            return null;
        }
        return driver;
    }

    /**
     * 截图
     * @param driver
     * @param fileName
     */
    public static void getScreenshots(TakesScreenshot driver, String fileName) {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        try {
            byte[] bytes = FileUtils.readFileToByteArray(screenshot);
//            SohuScsUtils.uploadImage(fileName, bytes, false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
