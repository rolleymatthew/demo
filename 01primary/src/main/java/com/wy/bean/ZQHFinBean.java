package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sun.nio.ch.Net;

/**
 * Created by yunwang on 2021/11/11 9:33
 */
@EqualsAndHashCode
@ContentRowHeight(30)
@HeadRowHeight(40)
@ColumnWidth(50)
@Data
public class ZQHFinBean {
    @ExcelProperty("日期/科目")
    private String reportDate;
    @ExcelProperty({"预测公司成长性指标", "营业收入(亿元)"})
    private double operatingIncome;
    @ExcelProperty({"预测公司成长性指标", "营收增长率%"})
    private double revenueGrowthRate;
    @ExcelProperty({"预测公司成长性指标", "净利润(亿元)"})
    private double netProfit;
    @ExcelProperty({"预测公司成长性指标", "净利润增长率%"})
    private double netProfitGrowthRate;
    @ExcelProperty({"分析公司获利性指标", "营业毛利率%"})
    private double operatingGrossProfitMargin;
    @ExcelProperty({"分析公司获利性指标", "净利率%"})
    private double netInterestRate;
    @ExcelProperty({"分析公司获利性指标", "营业利润率%"})
    private double operatingProfitMargin;
    @ExcelProperty({"分析公司获利性指标", "净资产收益率%"})
    private double returnOnNetAssets;
    @ExcelProperty({"检视公司安全性指标", "经营现金流净额(万元)"})
    private double netOperatingCashFlow;
    @ExcelProperty({"检视公司安全性指标", "长短期负债比"})
    private double LongAndShortTermDebtRatio;
}
