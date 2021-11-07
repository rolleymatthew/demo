package com.wy.stock;

import com.wy.stock.finance.FinanceCommonService;
import com.wy.stock.finance.FinanceDateReportService;

import java.util.List;

/**
 * Created by yunwang on 2021/11/1 11:31
 */
public class ReportByFin {
    public static void main(String[] args) {
        List<String> allCodes = FinanceCommonService.getAllCodes(false);
        int[] counts = {1, 2, 3};
        FinanceDateReportService.countUpFinThreePer(counts, allCodes, null);
    }
}
