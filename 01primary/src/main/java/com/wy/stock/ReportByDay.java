package com.wy.stock;

import com.wy.stock.etf.ETFFundReportService;
import com.wy.stock.hszh.HSHStockReportService;

/**
 * @author yunwang
 * @Date 2021-10-27
 */
public class ReportByDay {
    public static void main(String[] args) {
        int[] days = {2, 3, 4, 5, 6, 7, 8, 9, 10};
        ETFFundReportService.analyseETF(days);

        int[] countUpZeroDays = {2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 30, 50};
        int[] ampTopDays = {3, 5, 10, 20, 30, 50};
        HSHStockReportService.getETFReport(countUpZeroDays, ampTopDays);
    }
}
