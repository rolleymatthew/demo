package com.wy;

import com.wy.bean.StockCodeBean;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.StockService;
import jdk.nashorn.internal.ir.annotations.Ignore;
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
    @Ignore
    void Test() {
        System.out.println(stockService.hsshAndETFReport());
    }

    @Test
    @Ignore
    void getCodeTest(){
        System.out.println(stock.getSzCode());
    }

    @Test
    @Ignore
    void getaCodeTest(){
        System.out.println(stockCodeYmlBean.getAcode().size());
        stockCodeYmlBean.getAcode().entrySet().stream().forEach(System.out::println);
    }

    @Test
    @Ignore
    void getFinanceDateByMonthTest(){
        System.out.println(stockService.FinanceDateByMonth("600519"));
    }

    @Test
//    @Ignore
    void stockServiceTest(){
        stockService.FinanceDateByMonth(null);
    }
}
