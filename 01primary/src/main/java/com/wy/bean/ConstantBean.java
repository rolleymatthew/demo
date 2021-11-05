package com.wy.bean;

import org.apache.commons.collections.map.AbstractMapDecorator;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunwang on 2021/10/27 15:44
 */
public class ConstantBean {
    public static final Map<String, String> ZYCWZB_DIC = new HashMap<String, String>() {
        {
            put("报告日期", "reportDate");
            put("基本每股收益(元)", "basePerShare");
            put("每股净资产(元)", "valuePerShare");
            put("每股经营活动产生的现金流量净额(元)", "cashPerShare");
            put("主营业务收入(万元)", "mainBusiIncome");
            put("主营业务利润(万元)", "mainBusiProfit");
            put("营业利润(万元)", "operatProfit");
            put("投资收益(万元)", "investIncome");
            put("营业外收支净额(万元)", "notOperatIncome");
            put("利润总额(万元)", "totalProfit");
            put("净利润(万元)", "netProfit");
            put("净利润(扣除非经常性损益后)(万元)", "recurNetProfit");
            put("经营活动产生的现金流量净额(万元)", "cashFlowOpet");
            put("现金及现金等价物净增加额(万元)", "cashValueIncr");
            put("总资产(万元)", "totalAssets");
            put("流动资产(万元)", "currentAssets");
            put("总负债(万元)", "totalLiabil");
            put("流动负债(万元)", "currentLiabil");
            put("股东权益不含少数股东权益(万元)", "shareEquity");
            put("净资产收益率加权(%)", "netAssetsWeight");
        }
    };
    public static final Map<String, String> LRB_DIC = new HashMap<String, String>() {
        {
            put("报告日期", "reportDate");
            put("营业总收入(万元)", "TotalOperatingIncome");
            put("营业收入(万元)", "OperatingIncome");
            put("利息收入(万元)", "InterestIncome");
            put("已赚保费(万元)", "PremiumEarned");
            put("手续费及佣金收入(万元)", "HandlingFeeAndCommissionIncome");
            put("房地产销售收入(万元)", "RealEstateSlesRevenue");
            put("其他业务收入(万元)", "OtherBusinessIncome");
            put("营业总成本(万元)", "TotalOperatingCost");
            put("营业成本(万元)", "OperatingCost");
            put("利息支出(万元)", "InterestExpense");
            put("手续费及佣金支出(万元)", "HandlingChargesAndCommissionExpenses");
            put("房地产销售成本(万元)", "RealEstateSalesCost");
            put("研发费用(万元)", "RDExpenses");
            put("退保金(万元)", "SurrenderMoney");
            put("赔付支出净额(万元)", "NetCompensationExpenditure");
            put("提取保险合同准备金净额(万元)", "NetAmountOfInsuranceContractReserve");
            put("保单红利支出(万元)", "PolicyDividendExpenditure");
            put("分保费用(万元)", "ReinsuranceExpense");
            put("其他业务成本(万元)", "OtherBusinessCosts");
            put("营业税金及附加(万元)", "BusinessTaxAndSurcharges");
            put("销售费用(万元)", "SalesExpenses");
            put("管理费用(万元)", "AdministrativeExpenses");
            put("财务费用(万元)", "FinancialExpenses");
            put("资产减值损失(万元)", "AssetImpairmentLoss");
            put("公允价值变动收益(万元)", "IncomeFromChangesInFairValue");
            put("投资收益(万元)", "InvestmentIncome");
            put("对联营企业和合营企业的投资收益(万元)", "InvestmentIncomeFromAssociatesAndJointVentures");
            put("汇兑收益(万元)", "ExchangeIncome");
            put("期货损益(万元)", "FuturesProfitAndLoss");
            put("托管收益(万元)", "CustodyIncome");
            put("补贴收入(万元)", "SubsidyIncome");
            put("其他业务利润(万元)", "OtherBusinessProfit");
            put("营业利润(万元)", "OperatingProfit");
            put("营业外收入(万元)", "NonOperatingIncome");
            put("营业外支出(万元)", "NonOperatingExpenses");
            put("非流动资产处置损失(万元)", "LossOnDisposalOfNonCurrentAssets");
            put("利润总额(万元)", "TotalProfit");
            put("所得税费用(万元)", "IncomeTaxExpense");
            put("未确认投资损失(万元)", "UnrecognizedInvestmentLoss");
            put("净利润(万元)", "NetProfit");
            put("归属于母公司所有者的净利润(万元)", "NetProfitAttributable");
            put("被合并方在合并前实现净利润(万元)", "NetProfitRealized");
            put("少数股东损益(万元)", "ProfitAndLossShareholders");
            put("基本每股收益", "BasicEarningsPerShare");
            put("稀释每股收益", "DilutedEarningsPerShare");
        }
    };

    public static final String balance="报告日期|ReportDate,货币资金|MonetaryFund,结算备付金|SettlementProvision,拆出资金|LendingFunds,交易性金融资产|TradingFinancialAssets,衍生金融资产|DerivativeFinancialAssets,应收票据|NotesReceivable,应收账款|AccountsReceivable,预付款项|AdvancePayment,应收保费|receivablePremium,应收分保账款|accountsReceivableReinsurance,应收分保合同准备金|ReinsuranceContractReserveReceivable,应收利息|InterestReceivable,应收股利|DividendsReceivable,其他应收款|OtherReceivables,应收出口退税|ExportTaxRebateReceivable,应收补贴款|SubsidiesReceivable,应收保证金|MarginReceivable,内部应收款|InternalReceivables,买入返售金融资产|PurchaseOfResaleFinancialAssets,存货|stock,待摊费用|DeferredExpenses,待处理流动资产损益|ProfitAndLossOfCurrentAssetsToBeDisposed,一年内到期的非流动资产|NonCurrentAssetsDueWithinOneYear,其他流动资产|OtherCurrentAssets,流动资产合计|TotalCurrentAssets,发放贷款及垫款|LoansAndAdvances,可供出售金融资产|AvailableForSaleFinancialAssets,持有至到期投资|HeldToMaturityInvestment,长期应收款|longTermReceivables,长期股权投资|LongTermEquityInvestment,其他长期投资|OtherLongTermInvestments,投资性房地产|InvestmentRealEstate,固定资产原值|OriginalValueOfFixedAssets,累计折旧|AccumulatedDepreciation,固定资产净值|NetValueOfFixedAssets,固定资产减值准备|FixedAssetsDepreciationReserves,固定资产|fixedAssets,在建工程|ConstructionInProgress,工程物资|EngineeringMaterials,固定资产清理|LiquidationOfFixedAssets,生产性生物资产|ProductiveBiologicalAssets,公益性生物资产|PublicWelfareBiologicalAssets,油气资产|OilAndGasAssets,无形资产|intangibleAssets,开发支出|DevelopmentExpenditure,商誉|goodwill,长期待摊费用|LongTermDeferredExpenses,股权分置流通权|NonTradableShareRight,递延所得税资产|deferredTaxAssets,其他非流动资产|OtherNonCurrentAssets,非流动资产合计|TotalNonCurrentAssets,资产总计|TotalAssets,短期借款|ShortTermLoan,向中央银行借款|BorrowingFromTheCentralBank,吸收存款及同业存放|DepositsAndInterbankDeposits,拆入资金|BorrowingFunds,交易性金融负债|TradingFinancialLiabilities,衍生金融负债|DerivativeFinancialLiabilities,应付票据|NotesPayable,应付账款|AccountsPayable,预收账款|AdvancesReceived,卖出回购金融资产款|FinancialAssetsSoldForRepurchase,应付手续费及佣金|FeesAndCommissionsPayable,应付职工薪酬|PayrollPayable,应交税费|TaxesPayable,应付利息|InterestPayable,应付股利|DividendsPayable,其他应交款|OtherPayables,应付保证金|MarginPayable,内部应付款|InternalAccountsPayable,其他应付款|OtherPayablesmoney,预提费用|AccruedExpenses,预计流动负债|EstimatedCurrentLiabilities,应付分保账款|AccountsPayableReinsurance,保险合同准备金|InsuranceContractReserve,代理买卖证券款|FundsFromSecuritiesTradingAgency,代理承销证券款|ActingUnderwritingSecurities,国际票证结算|InternationalBillSettlement,国内票证结算|DomesticBillSettlement,递延收益|DeferredIncome,应付短期债券|ShortTermBondsPayable,一年内到期的非流动负债|NonCurrentLiabilitiesDueWithinOneYear,其他流动负债|OtherCurrentLiabilities,流动负债合计|TotalCurrentLiabilities,长期借款|LongTermLoan,应付债券|BondsPayable,长期应付款|LongTermAccountsPayable,专项应付款|SpecialAccountsPayable,预计非流动负债|EstimatedNonCurrentLiabilities,长期递延收益|LongTermDeferredIncome,递延所得税负债|DeferredTaxLiability,其他非流动负债|OtherNonCurrentLiabilities,非流动负债合计|TotalNonCurrentLiabilities,负债合计|TotalLiabilities,实收资本(或股本)|PaidInCapitalOrShareCapital,资本公积|CapitalReserve,减:库存股|LessTreasuryShares,专项储备|SpecialReserve,盈余公积|SurplusReserve,一般风险准备|GeneralRiskReserve,未确定的投资损失|UndeterminedInvestmentLoss,未分配利润|UndistributedProfit,拟分配现金股利|CashDividendsToBeDistributed,外币报表折算差额|TranslationDifferenceOfForeignCurrencyStatements,归属于母公司股东权益合计|TotalShareholdersEquityAttributableToTheParentCompany,少数股东权益|MinorityInterests,所有者权益(或股东权益)合计|TotalOwnerEquityOrShareholderEquity,负债和所有者权益(或股东权益)总计|TotalLiabilitiesAndOwnerEquityOrShareholderEquity";

    public static final String cashFlow=" 报告日期|ReportDate,销售商品、提供劳务收到的现金|CashReceivedFromSellingGoodsAndProvidingLaborServices,客户存款和同业存放款项净增加额|NetIncreaseInCustomerDepositsAndInterbankDeposits,向中央银行借款净增加额|NetIncreaseInBorrowingsFromTheCentralBank,向其他金融机构拆入资金净增加额|NetIncreaseInFundsBorrowedFromOtherFinancialInstitutions,收到原保险合同保费取得的现金|CashReceivedFromPremiumOfOriginalInsuranceContract,收到再保险业务现金净额|NetCashReceivedFromReinsuranceBusiness,保户储金及投资款净增加额|NetIncreaseInDepositsAndInvestmentFundsOfTheInsured,处置交易性金融资产净增加额|NetIncreaseInDisposalOfTradingFinancialAssets,收取利息、手续费及佣金的现金|CashReceivingInterestHandlingChargesAndCommissions,拆入资金净增加额|NetIncreaseInBorrowings,回购业务资金净增加额|NetIncreaseInRepurchaseBusinessFunds,收到的税费返还|RefundsOfTaxes,收到的其他与经营活动有关的现金|OtherCashReceivedRelatedToOperatingActivities,经营活动现金流入小计|SubtotalOfCashInflowFromOperatingActivities,购买商品、接受劳务支付的现金|CashPaidForPurchasingGoodsAndReceivingLaborServices,客户贷款及垫款净增加额|NetIncreaseInCustomerLoansAndAdvances,存放中央银行和同业款项净增加额|NetIncreaseInDepositsWithTheCentralBankAndOtherBanks,支付原保险合同赔付款项的现金|CashPaidForCompensationOfOriginalInsuranceContract,支付利息、手续费及佣金的现金|CashPaidForInterestHandlingChargesAndCommissions,支付保单红利的现金|CashPaidForPolicyDividends,支付给职工以及为职工支付的现金|CashPaidToAndForEmployees,支付的各项税费|TaxesPaid,支付的其他与经营活动有关的现金|OtherCashPaidRelatedToOperatingActivities,经营活动现金流出小计|SubtotalOfCashOutflowFromOperatingActivities,经营活动产生的现金流量净额|NetCashFlowFromOperatingActivities,收回投资所收到的现金|CashReceivedFromInvestmentRecovery,取得投资收益所收到的现金|CashReceivedFromInvestmentIncome,处置固定资产、无形资产和其他长期资产所收回的现金净额|NetCashReceivedFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets,处置子公司及其他营业单位收到的现金净额|NetCashReceivedFromDisposalOfSubsidiariesAndOtherBusinessUnits,收到的其他与投资活动有关的现金|OtherCashReceivedRelatedToInvestmentActivities,减少质押和定期存款所收到的现金|ReduceCashReceivedFromPledgeAndTermDeposit,投资活动现金流入小计|SubtotalOfCashInflowFromInvestmentActivities,购建固定资产、无形资产和其他长期资产所支付的现金|CashPaidForThePurchaseAndConstructionOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets,投资所支付的现金|CashPaidForInvestment,质押贷款净增加额|NetIncreaseInPledgedLoans,取得子公司及其他营业单位支付的现金净额|NetCashPaidForAcquiringSubsidiariesAndOtherBusinessUnits,支付的其他与投资活动有关的现金|OtherCashPaidRelatedToInvestmentActivities,增加质押和定期存款所支付的现金|IncreaseCashPaidForPledgeAndTimeDeposit,投资活动现金流出小计|SubtotalOfCashOutflowFromInvestmentActivities,投资活动产生的现金流量净额|NetCashFlowFromInvestmentActivities,吸收投资收到的现金|CashReceivedFromInvestment,其中：子公司吸收少数股东投资收到的现金|IncludingCashReceivedBySubsidiariesFromMinorityShareholdersInvestment,取得借款收到的现金|CashReceivedFromBorrowing,发行债券收到的现金|CashReceivedFromIssuingBonds,收到其他与筹资活动有关的现金|CashReceivedFromOtherFinancingActivities,筹资活动现金流入小计|SubtotalOfCashInflowFromFinancingActivities,偿还债务支付的现金|CashPaidForDebtRepayment,分配股利、利润或偿付利息所支付的现金|CashPaidForDistributionOfDividendsProfitsOrInterestPayments,其中：子公司支付给少数股东的股利、利润|IncludingDividendsAndProfitsPaidBySubsidiariesToMinorityShareholders,支付其他与筹资活动有关的现金|OtherCashPaidRelatedToFinancingActivities,筹资活动现金流出小计|SubtotalOfCashOutflowFromFinancingActivities,筹资活动产生的现金流量净额|NetCashFlowFromFinancingActivities,汇率变动对现金及现金等价物的影响|ImpactOfExchangeRateChangesOnCashAndCashEquivalents,现金及现金等价物净增加额|NetIncreaseInCashAndCashEquivalents,加:期初现金及现金等价物余额|AddBalanceOfCashAndCashEquivalentsAtTheBeginningOfThePeriod,期末现金及现金等价物余额|BalanceOfCashAndCashEquivalentsAtTheEndOfThePeriod,净利润|NetProfit,少数股东损益|MinorityInterest,未确认的投资损失|UnrecognizedInvestmentLoss,资产减值准备|ProvisionForAssetImpairment,固定资产折旧、油气资产折耗、生产性物资折旧|DepreciationOfFixedAssetsDepletionOfOilAndGasAssetsAndDepreciationOfProductiveMaterials,无形资产摊销|AmortizationOfIntangibleAssets,长期待摊费用摊销|AmortizationOfLongTermDeferredExpenses,待摊费用的减少|DecreaseInDeferredExpenses,预提费用的增加|IncreaseInAccruedExpenses,处置固定资产、无形资产和其他长期资产的损失|LossesFromDisposalOfFixedAssetsIntangibleAssetsAndOtherLongTermAssets,固定资产报废损失|LossOnRetirementOfFixedAssets,公允价值变动损失|LossFromChangesInFairValue,递延收益增加(减：减少)|IncreaseInDeferredIncomeLessDecrease,预计负债|EstimatedLiabilities,财务费用|FinancialExpenses,投资损失|InvestmentLoss,递延所得税资产减少|DecreaseInDeferredIncomeTaxAssets,递延所得税负债增加|IncreaseInDeferredIncomeTaxLiabilities,存货的减少|DecreaseInInventories,经营性应收项目的减少|DecreaseInOperatingReceivables,经营性应付项目的增加|IncreaseInOperatingPayables,已完工尚未结算款的减少(减:增加)|DecreaseInCompletedButUnsettledAccountsLessIncrease,已结算尚未完工款的增加(减:减少)|IncreaseInSettledButNotCompletedPaymentLessDecrease,其他|other,经营活动产生现金流量净额|NetCashFlowFromOperatingActivitiesValue,债务转为资本|DebtToCapital,一年内到期的可转换公司债券|ConvertibleCorporateBondsDueWithinOneYear,融资租入固定资产|FixedAssetsUnderFinanceLease,现金的期末余额|ClosingBalanceOfCash,现金的期初余额|OpeningBalanceOfCash,现金等价物的期末余额|ClosingBalanceOfCashEquivalents,现金等价物的期初余额|OpeningBalanceOfCashEquivalents,现金及现金等价物的净增加额|NetIncreaseInCashAndCashEquivalentsValue";
}
