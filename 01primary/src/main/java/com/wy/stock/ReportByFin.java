package com.wy.stock;

import com.wy.stock.finance.FinanceDateReportService;
import com.wy.stock.finance.FinanceDateWriteService;

import java.util.List;

/**
 * Created by yunwang on 2021/11/1 11:31
 */
public class ReportByFin {
    public static void main(String[] args) {
        List<String> allCodes = FinanceDateWriteService.getAllCodes();
        int[] counts = {1, 2, 3};
        FinanceDateReportService.countUpFinThreePer(counts, allCodes);
    }
}