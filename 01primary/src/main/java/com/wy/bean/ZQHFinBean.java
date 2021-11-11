package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sun.nio.ch.Net;

/**
 * Created by yunwang on 2021/11/11 9:33
 */
@EqualsAndHashCode
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ColumnWidth(10)
@Data
public class ZQHFinBean {
    @ExcelProperty("日期/科目")
    private String reportDate;
    @ExcelProperty({"预测公司成长性指标", "营业收入(亿元)"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 21)
    @ContentFontStyle(color = 21)
    private double operatingIncome;
    @ColumnWidth(18)
    @ExcelProperty({"预测公司成长性指标", "营收增长率%"})
    @HeadFontStyle(color = 21)
    @ContentFontStyle(color = 21)
    private double revenueGrowthRate;
    @ColumnWidth(20)
    @HeadFontStyle(color = 21)
    @ContentFontStyle(color = 21)
    @ExcelProperty({"预测公司成长性指标", "净利润(亿元)"})
    private double netProfit;
    @ColumnWidth(20)
    @ExcelProperty({"预测公司成长性指标", "净利润增长率%"})
    @HeadFontStyle(color = 21)
    @ContentFontStyle(color = 21)
    private double netProfitGrowthRate;
    @ColumnWidth(20)
    @ExcelProperty({"分析公司获利性指标", "营业毛利率%"})
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double operatingGrossProfitMargin;
    @ColumnWidth(15)
    @ExcelProperty({"分析公司获利性指标", "净利率%"})
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double netInterestRate;
    @ExcelProperty({"分析公司获利性指标", "营业利润率%"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double operatingProfitMargin;
    @ExcelProperty({"分析公司获利性指标", "净资产收益率%"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double returnOnNetAssets;
    @ExcelProperty({"检视公司安全性指标", "经营现金流净额(亿元)"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 24)
    @ContentFontStyle(color = 24)
    private double netOperatingCashFlow;
    @ExcelProperty({"检视公司安全性指标", "长短期负债比"})
    @ColumnWidth(15)
    @HeadFontStyle(color = 24)
    @ContentFontStyle(color = 24,bold = BooleanEnum.TRUE)
    private double lAndLiabRatioww;
}
