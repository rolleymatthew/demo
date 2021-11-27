package com.wy;

import com.wy.bean.StockCodeBean;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void Test() {
        System.out.println(stockService.hsshAndETFReport());
    }

    @Test
    void getCodeTest(){
        System.out.println(stock.getSzCode());
    }

    @Test
    void getaCodeTest(){
        System.out.println(stockCodeYmlBean.getAcode().size());
        stockCodeYmlBean.getAcode().entrySet().stream().forEach(System.out::println);
    }

    @Test
    void getFinanceDateByMonthTest(){
        System.out.println(stockService.FinanceDateByMonth("002455,002456,600830,600831,603993,603995"));
    }

    @Test
    void getFinanceDateAllOneTest(){
        System.out.println(stockService.FinanceDateByAllOne("600519"));
    }

    @Test
    void stockReportServiceTest(){
//        stockService.FinanceDateReport("000678,600519");
        stockService.FinAllDateReport("000678,600519");
//        stockService.FinanceDateReport(null);
//        stockService.FinAllDateReport(null);
    }
}
