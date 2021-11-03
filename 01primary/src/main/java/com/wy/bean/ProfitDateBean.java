package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yunwang
 * @Date 2021-11-03
 */
@Data
public class ProfitDateBean {
    @ExcelProperty("报告日期")
    private String reportDate;
    @ExcelProperty("营业总收入(万元)")
    private String TotalOperatingIncome;
    @ExcelProperty("营业收入")
    private String OperatingIncome;
    @ExcelProperty("利息收入")
    private String InterestIncome;
    @ExcelProperty("已赚保费")
    private String PremiumEarned;
    @ExcelProperty("手续费及佣金收入")
    private String HandlingFeeAndCommissionIncome;
    @ExcelProperty("房地产销售收入")
    private String RealEstateSlesRevenue;
    @ExcelProperty("其他业务收入")
    private String OtherBusinessIncome;
    @ExcelProperty("营业总成本")
    private String TotalOperatingCost;
    @ExcelProperty("营业成本")
    private String OperatingCost;
    @ExcelProperty("利息支出")
    private String InterestExpense;
    @ExcelProperty("手续费及佣金支出")
    private String HandlingChargesAndCommissionExpenses;
    @ExcelProperty("房地产销售成本")
    private String RealEstateSalesCost;
    @ExcelProperty("研发费用")
    private String RDExpenses;
    @ExcelProperty("退保金")
    private String SurrenderMoney;
    @ExcelProperty("赔付支出净额")
    private String NetCompensationExpenditure;
    @ExcelProperty("提取保险合同准备金净额")
    private String NetAmountOfInsuranceContractReserve;
    @ExcelProperty("保单红利支出")
    private String PolicyDividendExpenditure;
    @ExcelProperty("分保费用")
    private String ReinsuranceExpense;
    @ExcelProperty("其他业务成本")
    private String OtherBusinessCosts;
    @ExcelProperty("营业税金及附加")
    private String BusinessTaxAndSurcharges;
    @ExcelProperty("销售费用")
    private String SalesExpenses;
    @ExcelProperty("管理费用")
    private String AdministrativeExpenses;
    @ExcelProperty("财务费用")
    private String FinancialExpenses;
    @ExcelProperty("资产减值损失")
    private String AssetImpairmentLoss;
    @ExcelProperty("公允价值变动收益")
    private String IncomeFromChangesInFairValue;
    @ExcelProperty("投资收益")
    private String InvestmentIncome;
    @ExcelProperty("对联营企业和合营企业的投资收益")
    private String InvestmentIncomeFromAssociatesAndJointVentures;
    @ExcelProperty("汇兑收益")
    private String ExchangeIncome;
    @ExcelProperty("期货损益")
    private String FuturesProfitAndLoss;
    @ExcelProperty("托管收益")
    private String CustodyIncome;
    @ExcelProperty("补贴收入")
    private String SubsidyIncome;
    @ExcelProperty("其他业务利润")
    private String OtherBusinessProfit;
    @ExcelProperty("营业利润")
    private String OperatingProfit;
    @ExcelProperty("营业外收入")
    private String NonOperatingIncome;
    @ExcelProperty("营业外支出")
    private String NonOperatingExpenses;
    @ExcelProperty("非流动资产处置损失")
    private String LossOnDisposalOfNonCurrentAssets;
    @ExcelProperty("利润总额")
    private String TotalProfit;
    @ExcelProperty("所得税费用")
    private String IncomeTaxExpense;
    @ExcelProperty("未确认投资损失")
    private String UnrecognizedInvestmentLoss;
    @ExcelProperty("净利润")
    private String NetProfit;
    @ExcelProperty("归属于母公司所有者的净利润")
    private String NetProfitAttributable;
    @ExcelProperty("被合并方在合并前实现净利润")
    private String NetProfitRealized;
    @ExcelProperty("少数股东损益")
    private String ProfitAndLossShareholders;
    @ExcelProperty("基本每股收益")
    private String BasicEarningsPerShare;
    @ExcelProperty("稀释每股收益")
    private String DilutedEarningsPerShare;


}
