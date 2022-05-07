package com.wy.service;

import com.wy.bean.ResultVO;

/**
 * Stock数据抓取产生
 * Created by yunwang on 2021/11/2 10:07
 */
public interface StockService {
    ResultVO hsshDataByDay(int dayCount);
    ResultVO ETFDataByDay(int dayCount);
    ResultVO hsshAndETFDataByDay(int dayCount);
    ResultVO hsshAndETFReport();
    ResultVO FinanceDateByAllOne(String code);
    ResultVO FinAllDateReport(String code);
    ResultVO FinYBDateReport(String code,String date);

    /**
     * 1.一个季度毛利率在80以上，净利率在60以上的
     * 2.一个季度毛利率在60以上，净利率在40以上的
     * @param code
     * @return
     */
    ResultVO FinIncomingAndPerfitReport(String code);

}
