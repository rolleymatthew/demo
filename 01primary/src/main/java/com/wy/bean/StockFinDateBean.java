package com.wy.bean;

import lombok.Data;

import java.util.List;

/**
 * Created by yunwang on 2021/11/17 16:14
 */
@Data
public class StockFinDateBean {
    private List<BalanceDateBean> balanceDateBean;
    private List<ProfitDateBean> profitDateBean;
    private List<CashFlowBean> cashFlowBean;
    private List<FinanceDataBean> financeDataBean;
}
