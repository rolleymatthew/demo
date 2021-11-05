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

    public static final String balance="报告日期|ReportDate,货币资金|MonetaryFund,结算备付金|SettlementProvision,拆出资金|LendingFunds,交易性金融资产|TradingFinancialAssets,衍生金融资产|DerivativeFinancialAssets,应收票据|NotesReceivable,应收账款|AccountsReceivable,预付款项|AdvancePayment,应收保费|receivablePremium,应收分保账款|accountsReceivableReinsurance,应收分保合同准备金|ReinsuranceContractReserveReceivable,应收利息|InterestReceivable,应收股利|DividendsReceivable,其他应收款|OtherReceivables,应收出口退税|ExportTaxRebateReceivable,应收补贴款|SubsidiesReceivable,应收保证金|MarginReceivable,内部应收款|InternalReceivables,买入返售金融资产|PurchaseOfResaleFinancialAssets,存货|stock,待摊费用|DeferredExpenses,待处理流动资产损益|ProfitAndLossOfCurrentAssetsToBeDisposed,一年内到期的非流动资产|NonCurrentAssetsDueWithinOneYear,其他流动资产|OtherCurrentAssets,流动资产合计|TotalCurrentAssets,发放贷款及垫款|LoansAndAdvances,可供出售金融资产|AvailableForSaleFinancialAssets,持有至到期投资|HeldToMaturityInvestment,长期应收款|longTermReceivables,长期股权投资|LongTermEquityInvestment,其他长期投资|OtherLongTermInvestments,投资性房地产|InvestmentRealEstate,固定资产原值|OriginalValueOfFixedAssets,累计折旧|AccumulatedDepreciation,固定资产净值|NetValueOfFixedAssets,固定资产减值准备|FixedAssetsDepreciationReserves,固定资产|fixedAssets,在建工程|ConstructionInProgress,工程物资|EngineeringMaterials,固定资产清理|LiquidationOfFixedAssets,生产性生物资产|ProductiveBiologicalAssets,公益性生物资产|PublicWelfareBiologicalAssets,油气资产|OilAndGasAssets,无形资产|intangibleAssets,开发支出|DevelopmentExpenditure,商誉|goodwill,长期待摊费用|LongTermDeferredExpenses,股权分置流通权|NonTradableShareRight,递延所得税资产|deferredTaxAssets,其他非流动资产|OtherNonCurrentAssets,非流动资产合计|TotalNonCurrentAssets,资产总计|TotalAssets,短期借款|ShortTermLoan,向中央银行借款|BorrowingFromTheCentralBank,吸收存款及同业存放|DepositsAndInterbankDeposits,拆入资金|BorrowingFunds,交易性金融负债|TradingFinancialLiabilities,衍生金融负债|DerivativeFinancialLiabilities,应付票据|NotesPayable,应付账款|AccountsPayable,预收账款|AdvancesReceived,卖出回购金融资产款|FinancialAssetsSoldForRepurchase,应付手续费及佣金|FeesAndCommissionsPayable,应付职工薪酬|PayrollPayable,应交税费|TaxesPayable,应付利息|InterestPayable,应付股利|DividendsPayable,其他应交款|OtherPayables,应付保证金|MarginPayable,内部应付款|InternalAccountsPayable,其他应付款|OtherPayablesmoney,预提费用|AccruedExpenses,预计流动负债|EstimatedCurrentLiabilities,应付分保账款|AccountsPayableReinsurance,保险合同准备金|InsuranceContractReserve,代理买卖证券款|FundsFromSecuritiesTradingAgency,代理承销证券款|ActingUnderwritingSecurities,国际票证结算|InternationalBillSettlement,国内票证结算|DomesticBillSettlement,递延收益|DeferredIncome,应付短期债券|ShortTermBondsPayable,一年内到期的非流动负债|NonCurrentLiabilitiesDueWithinOneYear,其他流动负债|OtherCurrentLiabilities,流动负债合计|TotalCurrentLiabilities,长期借款|LongTermLoan,应付债券|BondsPayable,长期应付款|LongTermAccountsPayable,专项应付款|SpecialAccountsPayable,预计非流动负债|EstimatedNonCurrentLiabilities,长期递延收益|LongTermDeferredIncome,递延所得税负债|DeferredTaxLiability,其他非流动负债|OtherNonCurrentLiabilities,非流动负债合计|TotalNonCurrentLiabilities,负债合计|TotalLiabilities,实收资本(或股本)|PaidInCapitalOrShareCapital,资本公积|CapitalReserve,减:库存股|LessTreasuryShares,专项储备|SpecialReserve,盈余公积|SurplusReserve,一般风险准备|GeneralRiskReserve,未确定的投资损失|UndeterminedInvestmentLoss,未分配利润|UndistributedProfit,拟分配现金股利|CashDividendsToBeDistributed,外币报表折算差额|TranslationDifferenceOfForeignCurrencyStatements,归属于母公司股东权益合计|TotalShareholdersEquityAttributableToTheParentCompany,少数股东权益|MinorityInterests,所有者权益(或股东权益)合计|TotalOwnerEquityOrShareholderEquity,负债和所有者权益(或股东权益)总计|TotalLiabilitiesAndOwnerEquityOrShareholderEquity,            ";
}
