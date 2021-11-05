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
    ResultVO FinanceDateByMonth();
}
