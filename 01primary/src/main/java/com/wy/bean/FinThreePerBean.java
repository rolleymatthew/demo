package com.wy.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yunwang
 * @Date 2021-10-30
 */
@Data
public class FinThreePerBean {
    @ExcelProperty("代码")
    private String securityCode;
    @ExcelProperty("名称")
    private String securityName;
    @ExcelProperty("报告日期")
    private String ReportData;
    @ExcelProperty("毛利率")
    private Double grossProfit;
    @ExcelProperty("营业利润率")
    private Double operatProfit;
    @ExcelProperty("净利率")
    private Double netProfit;
    @ExcelProperty("毛利率增加")
    private Double addGrossProfit;
    @ExcelProperty("营业利润率增加")
    private Double addOperatProfit;
    @ExcelProperty("净利率增加")
    private Double addNetProfit;
    @ExcelProperty("净资产收益率加权")
    private String netAssetsWeight;
    @ExcelProperty("净资产收益率增加")
    private String addNetAssetsWeight;

}
