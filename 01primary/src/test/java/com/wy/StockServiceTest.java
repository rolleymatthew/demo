package com.wy;

import com.wy.bean.StockCodeBean;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.KLineService;
import com.wy.service.StockService;
import com.wy.stock.kline.KLineDataEntity;
import com.wy.stock.kline.KLineYBDatasDTO;
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
//        stockService.FinanceDateReport("000678,600519");
        stockService.FinAllDateReport("000678,600519");
//        stockService.FinanceDateReport(null);
//        stockService.FinAllDateReport(null);
    }

    @Test
    void kLindServiceTest() {
//        kLineService.storeKLineExcle("601318");
//        kLineService.storeKLineExcle();
//        KLineDataEntity kLineByCode = kLineService.findKLineByCode("601318");
//        System.out.println(kLineByCode.toString());
        KLineYBDatasDTO lastOneQuarterKlines = kLineService.findLastOneQuarterKlines("601318");

    }
}
