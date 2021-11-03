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

}
