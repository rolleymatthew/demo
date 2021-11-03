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
    private String TotalOperatingIncome;
    private String OperatingIncome;
    private String InterestIncome;
    private String PremiumEarned;
    private String HandlingFeeAndCommissionIncome;
    private String RealEstateSlesRevenue;
    private String OtherBusinessIncome;
    private String TotalOperatingCost;
    private String OperatingCost;
    private String InterestExpense;
    private String HandlingChargesAndCommissionExpenses;
    private String RealEstateSalesCost;
    private String RDExpenses;
    private String SurrenderMoney;
    private String NetCompensationExpenditure;
    private String NetAmountOfInsuranceContractReserve;
    private String PolicyDividendExpenditure;
    private String ReinsuranceExpense;
    private String OtherBusinessCosts;
    private String BusinessTaxAndSurcharges;
    private String SalesExpenses;
    private String AdministrativeExpenses;
    private String FinancialExpenses;
    private String AssetImpairmentLoss;
    private String IncomeFromChangesInFairValue;
    private String InvestmentIncome;
    private String InvestmentIncomeFromAssociatesAndJointVentures;
    private String ExchangeIncome;
    private String FuturesProfitAndLoss;
    private String CustodyIncome;
    private String SubsidyIncome;
    private String OtherBusinessProfit;
    private String OperatingProfit;
    private String NonOperatingIncome;
    private String NonOperatingExpenses;
    private String LossOnDisposalOfNonCurrentAssets;
    private String TotalProfit;
    private String IncomeTaxExpense;
    private String UnrecognizedInvestmentLoss;
    private String NetProfit;
    private String NetProfitAttributable;
    private String NetProfitRealized;
    private String ProfitAndLossShareholders;
    private String BasicEarningsPerShare;
    private String DilutedEarningsPerShare;


}
