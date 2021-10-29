package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Created by yunwang on 2021/10/26 9:46
 */
@Data
public class FinanceDataBean {
    @ExcelProperty("报告日期")
    private String reportDate;
    @ExcelProperty("基本每股收益(元)")
    private String basePerShare;
    @ExcelProperty("每股净资产(元)")
    private String valuePerShare;
    @ExcelProperty("每股经营活动产生的现金流量净额(元)")
    private String cashPerShare;
    @ExcelProperty("主营业务收入(万元)")
    private String mainBusiIncome;
    @ExcelProperty("主营业务利润(万元)")
    private String mainBusiProfit;
    @ExcelProperty("营业利润(万元)")
    private String operatProfit;
    @ExcelProperty("投资收益(万元)")
    private String investIncome;
    @ExcelProperty("营业外收支净额(万元)")
    private String notOperatIncome;
    @ExcelProperty("利润总额(万元)")
    private String totalProfit;
    @ExcelProperty("净利润(万元)")
    private String netProfit;
    @ExcelProperty("净利润(扣除非经常性损益后)(万元)")
    private String recurNetProfit;
    @ExcelProperty("经营活动产生的现金流量净额(万元)")
    private String cashFlowOpet;
    @ExcelProperty("现金及现金等价物净增加额(万元)")
    private String cashValueIncr;
    @ExcelProperty("总资产(万元)")
    private String totalAssets;
    @ExcelProperty("流动资产(万元)")
    private String currentAssets;
    @ExcelProperty("总负债(万元)")
    private String totalLiabil;
    @ExcelProperty("流动负债(万元)")
    private String currentLiabil;
    @ExcelProperty("股东权益不含少数股东权益(万元)")
    private String shareEquity;
    @ExcelProperty("净资产收益率加权(%)")
    private String netAssetsWeight;

}
