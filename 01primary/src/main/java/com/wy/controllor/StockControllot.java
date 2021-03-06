package com.wy.controllor;

import com.wy.bean.ResultVO;
import com.wy.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yunwang on 2021/11/2 10:06
 */
@RestController
public class StockControllot {
    @Autowired
    private StockService stockService;

    @GetMapping("/etf")
    public ResultVO getETF(@RequestParam(value = "dayCount", defaultValue = "1") int dayCount) {
        ResultVO resultVO = stockService.ETFDataByDay(dayCount);
        return resultVO;
    }

    @GetMapping("/hszh")
    public ResultVO getHSZH(@RequestParam(value = "dayCount", defaultValue = "1") int dayCount) {
        ResultVO resultVO = stockService.hsshDataByDay(dayCount);
        return resultVO;
    }

    @GetMapping("/hszhAndETF")
    public ResultVO getHSZHAndETF(@RequestParam(value = "dayCount", defaultValue = "1") int dayCount) {
        ResultVO resultVO = stockService.hsshAndETFDataByDay(dayCount);
        return resultVO;
    }

    @GetMapping("/hszhETFReport")
    public ResultVO getHSZHAndETFReport(@RequestParam(value = "dayCount", defaultValue = "1") int dayCount) {
        ResultVO resultVO = stockService.hsshAndETFReport();
        return resultVO;
    }

    @GetMapping("/finall")
    public ResultVO getFinanceDataAllOne(@RequestParam(value = "code", required = false) String code) {
        ResultVO resultVO = stockService.FinanceDateByAllOne(code);
        return resultVO;
    }

    @GetMapping("/finRepert")
    public ResultVO getFinanceReportData(@RequestParam(value = "code", required = false) String code) {
        ResultVO resultVO = stockService.FinAllDateReport(code);
        return resultVO;
    }

    @GetMapping("/yb")
    public ResultVO getYBData(@RequestParam(value = "code", required = false) String code
            ,@RequestParam(value = "selectDate", required = false) String selectDate) {
        ResultVO resultVO = stockService.FinYBDateReport(code,selectDate);
        return resultVO;
    }
}
