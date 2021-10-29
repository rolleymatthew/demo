package com.wy.stock;

import com.wy.stock.etf.ETFFundDataService;
import com.wy.stock.hszh.GetSHSZHKStockDateService;

import java.util.Date;

/**
 * @author yunwang
 * @Date 2021-10-27
 */
public class DataByDay {
    public static void main(String[] args) {
        //抓取ETF
        ETFFundDataService.getETFFundData(2);
        //抓取沪深港通
        GetSHSZHKStockDateService.getMutilSheet(2);
    }
}
