package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Created by yunwang on 2021/11/5 17:58
 */
@Data
public class CashFlowBean {
    @ExcelProperty("报告日期")
    private String ReportDate;
    @ExcelProperty("销售商品、提供劳务收到的现金")
    private String CashReceivedFromSellingGoodsAndProvidingLaborServices;
    @ExcelProperty("客户存款和同业存放款项净增加额")
    private String NetIncreaseInCustomerDepositsAndInterbankDeposits;
    @ExcelProperty("向中央银行借款净增加额")
    private String NetIncreaseInBorrowingsFromTheCentralBank;
    @ExcelProperty("向其他金融机构拆入资金净增加额")
    private String NetIncreaseInFundsBorrowedFromOtherFinancialInstitutions;
    @ExcelProperty("收到原保险合同保费取得的现金")
    private String CashReceivedFromPremiumOfOriginalInsuranceContract;
    @ExcelProperty("收到再保险业务现金净额")
    private String NetCashReceivedFromReinsuranceBusiness;
    @ExcelProperty("保户储金及投资款净增加额")
    private String NetIncreaseInDepositsAndInvestmentFundsOfTheInsured;
    @ExcelProperty("处置交易性金融资产净增加额")
    private String NetIncreaseInDisposalOfTradingFinancialAssets;
    @ExcelProperty("收取利息、手续费及佣金的现金")
    private String CashReceivingInterestHandlingChargesAndCommissions;
    @ExcelProperty("拆入资金净增加额")
    private String NetIncreaseInBorrowings;
    @ExcelProperty("回购业务资金净增加额")
    private String NetIncreaseInRepurchaseBusinessFunds;
    @ExcelProperty("收到的税费返还")
    private String RefundsOfTaxes;
    @ExcelProperty("收到的其他与经营活动有关的现金")
    private String OtherCashReceivedRelatedToOperatingActivities;
    @ExcelProperty("经营活动现金流入小计")
    private String SubtotalOfCashInflowFromOperatingActivities;
    @ExcelProperty("购买商品、接受劳务支付的现金")
    private String CashPaidForPurchasingGoodsAndReceivingLaborServices;
    @ExcelProperty("客户贷款及垫款净增加额")
    private String NetIncreaseInCustomerLoansAndAdvances;
    @ExcelProperty("存放中央银行和同业款项净增加额")
    private String NetIncreaseInDepositsWithTheCentralBankAndOtherBanks;
    @ExcelProperty("支付原保险合同赔付款项的现金")
    private String CashPaidForCompensationOfOriginalInsuranceContract;
    @ExcelProperty("支付利息、手续费及佣金的现金")
    private String CashPaidForInterestHandlingChargesAndCommissions;
    @ExcelProperty("支付保单红利的现金")
    private String CashPaidForPolicyDividends;
    @ExcelProperty("支付给职工以及为职工支付的现金")
    private String CashPaidToAndForEmployees;
    @ExcelProperty("支付的各项税费")
    private String TaxesPaid;
    @ExcelProperty("支付的其他与经营活动有关的现金")
    private String OtherCashPaidRelatedToOperatingActivities;
    @ExcelProperty("经营活动现金流出小计")
    private String SubtotalOfCashOutflowFromOperatingActivities;
    @ExcelProperty("经营活动产生的现金流量净额")
    private String NetCashFlowFromOperatingActivities;
    @ExcelProperty("收回投资所收到的现金")
    private String CashReceivedFromInvestmentRecovery;
    @ExcelProperty("取得投资收益所收到的现金")
    private String CashReceivedFromInvestmentIncome;
    @ExcelProperty("处置固定资产、无形资产和其他长期资产所收回的现金净额")
    private String NetCashReceivedFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;
    @ExcelProperty("处置子公司及其他营业单位收到的现金净额")
    private String NetCashReceivedFromDisposalOfSubsidiariesAndOtherBusinessUnits;
    @ExcelProperty("收到的其他与投资活动有关的现金")
    private String OtherCashReceivedRelatedToInvestmentActivities;
    @ExcelProperty("减少质押和定期存款所收到的现金")
    private String ReduceCashReceivedFromPledgeAndTermDeposit;
    @ExcelProperty("投资活动现金流入小计")
    private String SubtotalOfCashInflowFromInvestmentActivities;
    @ExcelProperty("购建固定资产、无形资产和其他长期资产所支付的现金")
    private String CashPaidForThePurchaseAndConstructionOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;
    @ExcelProperty("投资所支付的现金")
    private String CashPaidForInvestment;
    @ExcelProperty("质押贷款净增加额")
    private String NetIncreaseInPledgedLoans;
    @ExcelProperty("取得子公司及其他营业单位支付的现金净额")
    private String NetCashPaidForAcquiringSubsidiariesAndOtherBusinessUnits;
    @ExcelProperty("支付的其他与投资活动有关的现金")
    private String OtherCashPaidRelatedToInvestmentActivities;
    @ExcelProperty("增加质押和定期存款所支付的现金")
    private String IncreaseCashPaidForPledgeAndTimeDeposit;
    @ExcelProperty("投资活动现金流出小计")
    private String SubtotalOfCashOutflowFromInvestmentActivities;
    @ExcelProperty("投资活动产生的现金流量净额")
    private String NetCashFlowFromInvestmentActivities;
    @ExcelProperty("吸收投资收到的现金")
    private String CashReceivedFromInvestment;
    @ExcelProperty("其中：子公司吸收少数股东投资收到的现金")
    private String IncludingCashReceivedBySubsidiariesFromMinorityShareholdersInvestment;
    @ExcelProperty("取得借款收到的现金")
    private String CashReceivedFromBorrowing;
    @ExcelProperty("发行债券收到的现金")
    private String CashReceivedFromIssuingBonds;
    @ExcelProperty("收到其他与筹资活动有关的现金")
    private String CashReceivedFromOtherFinancingActivities;
    @ExcelProperty("筹资活动现金流入小计")
    private String SubtotalOfCashInflowFromFinancingActivities;
    @ExcelProperty("偿还债务支付的现金")
    private String CashPaidForDebtRepayment;
    @ExcelProperty("分配股利、利润或偿付利息所支付的现金")
    private String CashPaidForDistributionOfDividendsProfitsOrInterestPayments;
    @ExcelProperty("其中：子公司支付给少数股东的股利、利润")
    private String IncludingDividendsAndProfitsPaidBySubsidiariesToMinorityShareholders;
    @ExcelProperty("支付其他与筹资活动有关的现金")
    private String OtherCashPaidRelatedToFinancingActivities;
    @ExcelProperty("筹资活动现金流出小计")
    private String SubtotalOfCashOutflowFromFinancingActivities;
    @ExcelProperty("筹资活动产生的现金流量净额")
    private String NetCashFlowFromFinancingActivities;
    @ExcelProperty("汇率变动对现金及现金等价物的影响")
    private String ImpactOfExchangeRateChangesOnCashAndCashEquivalents;
    @ExcelProperty("现金及现金等价物净增加额")
    private String NetIncreaseInCashAndCashEquivalents;
    @ExcelProperty("加:期初现金及现金等价物余额")
    private String AddBalanceOfCashAndCashEquivalentsAtTheBeginningOfThePeriod;
    @ExcelProperty("期末现金及现金等价物余额")
    private String BalanceOfCashAndCashEquivalentsAtTheEndOfThePeriod;
    @ExcelProperty("净利润")
    private String NetProfit;
    @ExcelProperty("少数股东损益")
    private String MinorityInterest;
    @ExcelProperty("未确认的投资损失")
    private String UnrecognizedInvestmentLoss;
    @ExcelProperty("资产减值准备")
    private String ProvisionForAssetImpairment;
    @ExcelProperty("固定资产折旧、油气资产折耗、生产性物资折旧")
    private String DepreciationOfFixedAssetsDepletionOfOilAndGasAssetsAndDepreciationOfProductiveMaterials;
    @ExcelProperty("无形资产摊销")
    private String AmortizationOfIntangibleAssets;
    @ExcelProperty("长期待摊费用摊销")
    private String AmortizationOfLongTermDeferredExpenses;
    @ExcelProperty("待摊费用的减少")
    private String DecreaseInDeferredExpenses;
    @ExcelProperty("预提费用的增加")
    private String IncreaseInAccruedExpenses;
    @ExcelProperty("处置固定资产、无形资产和其他长期资产的损失")
    private String LossesFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets;
    @ExcelProperty("固定资产报废损失")
    private String LossOnRetirementOfFixedAssets;
    @ExcelProperty("公允价值变动损失")
    private String LossFromChangesInFairValue;
    @ExcelProperty("递延收益增加(减：减少)")
    private String IncreaseInDeferredIncomeLessDecrease;
    @ExcelProperty("预计负债")
    private String EstimatedLiabilities;
    @ExcelProperty("财务费用")
    private String FinancialExpenses;
    @ExcelProperty("投资损失")
    private String InvestmentLoss;
    @ExcelProperty("递延所得税资产减少")
    private String DecreaseInDeferredIncomeTaxAssets;
    @ExcelProperty("递延所得税负债增加")
    private String IncreaseInDeferredIncomeTaxLiabilities;
    @ExcelProperty("存货的减少")
    private String DecreaseInInventories;
    @ExcelProperty("经营性应收项目的减少")
    private String DecreaseInOperatingReceivables;
    @ExcelProperty("经营性应付项目的增加")
    private String IncreaseInOperatingPayables;
    @ExcelProperty("已完工尚未结算款的减少(减:增加)")
    private String DecreaseInCompletedButUnsettledAccountsLessIncrease;
    @ExcelProperty("已结算尚未完工款的增加(减:减少)")
    private String IncreaseInSettledButNotCompletedPaymentLessDecrease;
    @ExcelProperty("其他")
    private String other;
    @ExcelProperty("经营活动产生现金流量净额")
    private String NetCashFlowFromOperatingActivitiesValue;
    @ExcelProperty("债务转为资本")
    private String DebtToCapital;
    @ExcelProperty("一年内到期的可转换公司债券")
    private String ConvertibleCorporateBondsDueWithinOneYear;
    @ExcelProperty("融资租入固定资产")
    private String FixedAssetsUnderFinanceLease;
    @ExcelProperty("现金的期末余额")
    private String ClosingBalanceOfCash;
    @ExcelProperty("现金的期初余额")
    private String OpeningBalanceOfCash;
    @ExcelProperty("现金等价物的期末余额")
    private String ClosingBalanceOfCashEquivalents;
    @ExcelProperty("现金等价物的期初余额")
    private String OpeningBalanceOfCashEquivalents;
    @ExcelProperty("现金及现金等价物的净增加额")
    private String NetIncreaseInCashAndCashEquivalentsValue;
}
