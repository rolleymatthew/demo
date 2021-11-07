package com.wy;

import com.wy.bean.StockCodeBean;
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

    @Test
    @Ignore
    void Test() {
        System.out.println(stockService.hsshAndETFReport());
    }

    @Test
    @Ignore
    void getCodeTest(){
        System.out.println(stock.getShCode());
    }

    @Test
//    @Ignore
    void getFinanceDateByMonthTest(){
        System.out.println(stockService.FinanceDateByMonth("600519"));
    }

}
