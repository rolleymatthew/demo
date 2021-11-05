package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Created by yunwang on 2021/11/5 15:48
 */
@Data
public class BalanceDateBean {
    @ExcelProperty("报告日期")
    private String ReportDate;
    @ExcelProperty("货币资金")
    private String MonetaryFund;
    @ExcelProperty("结算备付金")
    private String SettlementProvision;
    @ExcelProperty("拆出资金")
    private String LendingFunds;
    @ExcelProperty("交易性金融资产")
    private String TradingFinancialAssets;
    @ExcelProperty("衍生金融资产")
    private String DerivativeFinancialAssets;
    @ExcelProperty("应收票据")
    private String NotesReceivable;
    @ExcelProperty("应收账款")
    private String AccountsReceivable;
    @ExcelProperty("预付款项")
    private String AdvancePayment;
    @ExcelProperty("应收保费")
    private String receivablePremium;
    @ExcelProperty("应收分保账款")
    private String accountsReceivableReinsurance;
    @ExcelProperty("应收分保合同准备金")
    private String ReinsuranceContractReserveReceivable;
    @ExcelProperty("应收利息")
    private String InterestReceivable;
    @ExcelProperty("应收股利")
    private String DividendsReceivable;
    @ExcelProperty("其他应收款")
    private String OtherReceivables;
    @ExcelProperty("应收出口退税")
    private String ExportTaxRebateReceivable;
    @ExcelProperty("应收补贴款")
    private String SubsidiesReceivable;
    @ExcelProperty("应收保证金")
    private String MarginReceivable;
    @ExcelProperty("内部应收款")
    private String InternalReceivables;
    @ExcelProperty("买入返售金融资产")
    private String PurchaseOfResaleFinancialAssets;
    @ExcelProperty("存货")
    private String stock;
    @ExcelProperty("待摊费用")
    private String DeferredExpenses;
    @ExcelProperty("待处理流动资产损益")
    private String ProfitAndLossOfCurrentAssetsToBeDisposed;
    @ExcelProperty("一年内到期的非流动资产")
    private String NonCurrentAssetsDueWithinOneYear;
    @ExcelProperty("其他流动资产")
    private String OtherCurrentAssets;
    @ExcelProperty("流动资产合计")
    private String TotalCurrentAssets;
    @ExcelProperty("发放贷款及垫款")
    private String LoansAndAdvances;
    @ExcelProperty("可供出售金融资产")
    private String AvailableForSaleFinancialAssets;
    @ExcelProperty("持有至到期投资")
    private String HeldToMaturityInvestment;
    @ExcelProperty("长期应收款")
    private String longTermReceivables;
    @ExcelProperty("长期股权投资")
    private String LongTermEquityInvestment;
    @ExcelProperty("其他长期投资")
    private String OtherLongTermInvestments;
    @ExcelProperty("投资性房地产")
    private String InvestmentRealEstate;
    @ExcelProperty("固定资产原值")
    private String OriginalValueOfFixedAssets;
    @ExcelProperty("累计折旧")
    private String AccumulatedDepreciation;
    @ExcelProperty("固定资产净值")
    private String NetValueOfFixedAssets;
    @ExcelProperty("固定资产减值准备")
    private String FixedAssetsDepreciationReserves;
    @ExcelProperty("固定资产")
    private String fixedAssets;
    @ExcelProperty("在建工程")
    private String ConstructionInProgress;
    @ExcelProperty("工程物资")
    private String EngineeringMaterials;
    @ExcelProperty("固定资产清理")
    private String LiquidationOfFixedAssets;
    @ExcelProperty("生产性生物资产")
    private String ProductiveBiologicalAssets;
    @ExcelProperty("公益性生物资产")
    private String PublicWelfareBiologicalAssets;
    @ExcelProperty("油气资产")
    private String OilAndGasAssets;
    @ExcelProperty("无形资产")
    private String intangibleAssets;
    @ExcelProperty("开发支出")
    private String DevelopmentExpenditure;
    @ExcelProperty("商誉")
    private String goodwill;
    @ExcelProperty("长期待摊费用")
    private String LongTermDeferredExpenses;
    @ExcelProperty("股权分置流通权")
    private String NonTradableShareRight;
    @ExcelProperty("递延所得税资产")
    private String deferredTaxAssets;
    @ExcelProperty("其他非流动资产")
    private String OtherNonCurrentAssets;
    @ExcelProperty("非流动资产合计")
    private String TotalNonCurrentAssets;
    @ExcelProperty("资产总计")
    private String TotalAssets;
    @ExcelProperty("短期借款")
    private String ShortTermLoan;
    @ExcelProperty("向中央银行借款")
    private String BorrowingFromTheCentralBank;
    @ExcelProperty("吸收存款及同业存放")
    private String DepositsAndInterbankDeposits;
    @ExcelProperty("拆入资金")
    private String BorrowingFunds;
    @ExcelProperty("交易性金融负债")
    private String TradingFinancialLiabilities;
    @ExcelProperty("衍生金融负债")
    private String DerivativeFinancialLiabilities;
    @ExcelProperty("应付票据")
    private String NotesPayable;
    @ExcelProperty("应付账款")
    private String AccountsPayable;
    @ExcelProperty("预收账款")
    private String AdvancesReceived;
    @ExcelProperty("卖出回购金融资产款")
    private String FinancialAssetsSoldForRepurchase;
    @ExcelProperty("应付手续费及佣金")
    private String FeesAndCommissionsPayable;
    @ExcelProperty("应付职工薪酬")
    private String PayrollPayable;
    @ExcelProperty("应交税费")
    private String TaxesPayable;
    @ExcelProperty("应付利息")
    private String InterestPayable;
    @ExcelProperty("应付股利")
    private String DividendsPayable;
    @ExcelProperty("其他应交款")
    private String OtherPayables;
    @ExcelProperty("应付保证金")
    private String MarginPayable;
    @ExcelProperty("内部应付款")
    private String InternalAccountsPayable;
    @ExcelProperty("其他应付款")
    private String OtherPayablesmoney;
    @ExcelProperty("预提费用")
    private String AccruedExpenses;
    @ExcelProperty("预计流动负债")
    private String EstimatedCurrentLiabilities;
    @ExcelProperty("应付分保账款")
    private String AccountsPayableReinsurance;
    @ExcelProperty("保险合同准备金")
    private String InsuranceContractReserve;
    @ExcelProperty("代理买卖证券款")
    private String FundsFromSecuritiesTradingAgency;
    @ExcelProperty("代理承销证券款")
    private String ActingUnderwritingSecurities;
    @ExcelProperty("国际票证结算")
    private String InternationalBillSettlement;
    @ExcelProperty("国内票证结算")
    private String DomesticBillSettlement;
    @ExcelProperty("递延收益")
    private String DeferredIncome;
    @ExcelProperty("应付短期债券")
    private String ShortTermBondsPayable;
    @ExcelProperty("一年内到期的非流动负债")
    private String NonCurrentLiabilitiesDueWithinOneYear;
    @ExcelProperty("其他流动负债")
    private String OtherCurrentLiabilities;
    @ExcelProperty("流动负债合计")
    private String TotalCurrentLiabilities;
    @ExcelProperty("长期借款")
    private String LongTermLoan;
    @ExcelProperty("应付债券")
    private String BondsPayable;
    @ExcelProperty("长期应付款")
    private String LongTermAccountsPayable;
    @ExcelProperty("专项应付款")
    private String SpecialAccountsPayable;
    @ExcelProperty("预计非流动负债")
    private String EstimatedNonCurrentLiabilities;
    @ExcelProperty("长期递延收益")
    private String LongTermDeferredIncome;
    @ExcelProperty("递延所得税负债")
    private String DeferredTaxLiability;
    @ExcelProperty("其他非流动负债")
    private String OtherNonCurrentLiabilities;
    @ExcelProperty("非流动负债合计")
    private String TotalNonCurrentLiabilities;
    @ExcelProperty("负债合计")
    private String TotalLiabilities;
    @ExcelProperty("实收资本(或股本)")
    private String PaidInCapitalOrShareCapital;
    @ExcelProperty("资本公积")
    private String CapitalReserve;
    @ExcelProperty("减:库存股")
    private String LessTreasuryShares;
    @ExcelProperty("专项储备")
    private String SpecialReserve;
    @ExcelProperty("盈余公积")
    private String SurplusReserve;
    @ExcelProperty("一般风险准备")
    private String GeneralRiskReserve;
    @ExcelProperty("未确定的投资损失")
    private String UndeterminedInvestmentLoss;
    @ExcelProperty("未分配利润")
    private String UndistributedProfit;
    @ExcelProperty("拟分配现金股利")
    private String CashDividendsToBeDistributed;
    @ExcelProperty("外币报表折算差额")
    private String TranslationDifferenceOfForeignCurrencyStatements;
    @ExcelProperty("归属于母公司股东权益合计")
    private String TotalShareholdersEquityAttributableToTheParentCompany;
    @ExcelProperty("少数股东权益")
    private String MinorityInterests;
    @ExcelProperty("所有者权益(或股东权益)合计")
    private String TotalOwnerEquityOrShareholderEquity;
    @ExcelProperty("负债和所有者权益(或股东权益)总计")
    private String TotalLiabilitiesAndOwnerEquityOrShareholderEquity;

}
