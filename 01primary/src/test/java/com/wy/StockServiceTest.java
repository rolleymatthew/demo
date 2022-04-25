package com.wy;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.StockCodeBean;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.KLineService;
import com.wy.service.StockService;
import com.wy.bean.YBEpsDataDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @author yunwang
 * @Date 2021-11-06
 */
@SpringBootTest
public class StockServiceTest {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockCodeBean stock;
    @Autowired
    StockCodeYmlBean stockCodeYmlBean;
    @Autowired
    KLineService kLineService;

    @Test
    void Test() {
        System.out.println(stockService.hsshAndETFReport());
    }

    @Test
    void getCodeTest() {
        System.out.println(stock.getSzCode());
    }

    @Test
    void getaCodeTest() {
        System.out.println(stockCodeYmlBean.getAcode().size());
        stockCodeYmlBean.getAcode().entrySet().stream().forEach(System.out::println);
    }

    @Test
    void getFinanceDateAllOneTest() {
        System.out.println(stockService.FinanceDateByAllOne("600519"));
    }

    @Test
    void stockReportServiceTest() {
//        stockService.FinAllDateReport("300131,300125,300069,002978,002765,002709,002597,002469,002407,002379,002245,002237,002188");
        stockService.FinAllDateReport(null);
    }

    @Test
    void kLindServiceTest() {
//        kLineService.storeKLineExcle("601318");
//        kLineService.storeKLineExcle();
//        KLineDataEntity kLineByCode = kLineService.findKLineByCode("601318");
//        System.out.println(kLineByCode.toString());
        String templateFileName =
                "d:\\stock\\demo" + File.separator + "pe.xlsx";
        String fileName = "d:\\stock\\demo" + File.separator + "pe601318.xlsx";
        YBEpsDataDTO lastOneQuarterKlines = kLineService.findYBDateKlines("601318", "2021-09-30");
        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(lastOneQuarterKlines);


    }

    @Test
    void KLineServicez(){
                kLineService.storeKLineExcle();

    }

    @Test
    void YBServiceTest() {
//        stockService.FinYBDateReport(null,null);
        stockService.FinYBDateReport("600887",null);
    }
}
