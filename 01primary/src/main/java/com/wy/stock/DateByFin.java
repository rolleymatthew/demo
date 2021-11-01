package com.wy.stock;

import com.wy.stock.finance.FinanceDateWriteService;
import com.wy.stock.hszh.HSHStockReportService;

import java.util.List;

/**
 * @author yunwang
 * @Date 2021-10-27
 */
public class DateByFin {
    public static void main(String[] args) {
        //获取所有公司代码
        List<String> allCodes = FinanceDateWriteService.getAllCodes();
        //获取主要财务指标
        FinanceDateWriteService.getFinanceData(allCodes);

    }
}
