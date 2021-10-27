package com.wy.stock.finance;

import org.apache.commons.collections.map.AbstractMapDecorator;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunwang on 2021/10/27 15:44
 */
public class ConstantBean {
    public static final Map<String,String> DIC=new HashMap<String,String>(){
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

}
