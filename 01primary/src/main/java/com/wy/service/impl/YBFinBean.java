package com.wy.service.impl;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yunwang
 * @Date 2021-11-21
 */
@EqualsAndHashCode
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ColumnWidth(10)
@Data
public class YBFinBean {

    @ExcelProperty("日期/科目")
    private String reportDate;
    @ColumnWidth(20)
    @ExcelProperty({"获利能力", "营业毛利率%"})
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double operatingGrossProfitMargin;
    @ColumnWidth(20)
    @ExcelProperty({"获利能力", "营业毛利率评分"})
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private Integer operatingGrossProfitMarginScore;
    @ExcelProperty({"获利能力", "营业利润率%"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double operatingProfitMargin;
    @ExcelProperty({"获利能力", "营业利润率评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private Integer operatingProfitMarginScore;
    @ColumnWidth(15)
    @ExcelProperty({"获利能力", "净利率%"})
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double netInterestRate;
    @ColumnWidth(15)
    @ExcelProperty({"获利能力", "净利率评分"})
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private Integer netInterestRateScore;
    @ExcelProperty({"获利能力", "净资产收益率%"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private double returnOnNetAssets;
    @ExcelProperty({"获利能力", "净资产收益率评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 10)
    @ContentFontStyle(color = 10)
    private Integer returnOnNetAssetsScore;

    @ExcelProperty({"财务结构", "资产负债比率"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 18)
    @ContentFontStyle(color = 18)
    private double assetLiabilityRatio;
    @ExcelProperty({"财务结构", "资产负债比率评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 18)
    @ContentFontStyle(color = 18)
    private Integer assetLiabilityRatioScore;
    @ExcelProperty({"财务结构", "流动资产/总资产"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 18)
    @ContentFontStyle(color = 18)
    private double currentAssetsAndTotalAssets;
    @ExcelProperty({"财务结构", "流动资产/总资产评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 18)
    @ContentFontStyle(color = 18)
    private Integer currentAssetsAndTotalAssetsScore;
    @ExcelProperty({"财务结构", "存货周转率评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 18)
    @ContentFontStyle(color = 18)
    private double inventoryTurnoverScore;
    @ExcelProperty({"财务结构", "应收账款周转率评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 18)
    @ContentFontStyle(color = 18)
    private Integer accountsReceivableTurnoverScore;

    @ExcelProperty({"现金流量", "经营活动现金流净额"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 28)
    @ContentFontStyle(color = 28)
    private double cashFlowOperating;
    @ExcelProperty({"现金流量", "经营活动现金流净额评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 28)
    @ContentFontStyle(color = 28)
    private Integer cashFlowOperatingScore;
    @ExcelProperty({"现金流量", "投资活动现金流净额"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 28)
    @ContentFontStyle(color = 28)
    private double cashFlowInvent;
    @ExcelProperty({"现金流量", "投资活动现金流净额评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 28)
    @ContentFontStyle(color = 28)
    private Integer cashFlowInventScore;
    @ExcelProperty({"现金流量", "筹资活动现金流净额"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 28)
    @ContentFontStyle(color = 28)
    private double cashFlowFinance;
    @ExcelProperty({"现金流量", "筹资活动现金流净额评分"})
    @ColumnWidth(20)
    @HeadFontStyle(color = 28)
    @ContentFontStyle(color = 28)
    private Integer cashFlowFinanceScore;
    @ExcelIgnore
    private boolean sectorType;

}
