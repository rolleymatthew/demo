package com.wy.stock;

import com.wy.stock.finance.*;
import com.wy.stock.hszh.HSHStockReportService;

import java.util.List;

/**
 * @author yunwang
 * @Date 2021-10-27
 */
public class DateByFin {
    public static void main(String[] args) {
        //获取所有公司代码
        List<String> allCodes = FinanceCommonService.getAllCodes(true);
        //获取财务数据
        FinanceBalanceDateService.getFinanceData(allCodes);
        FinanceCashFlowDateService.getFinanceData(allCodes);
        FinanceProfitDateService.getFinanceData(allCodes);
        FinanceDateWriteService.getFinanceData(allCodes);
    }
}
