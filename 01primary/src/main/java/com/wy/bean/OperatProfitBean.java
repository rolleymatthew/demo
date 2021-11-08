package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yunwang
 * @Date 2021-11-08
 */
@Data
public class OperatProfitBean {
    @ExcelProperty("代码")
    private String securityCode;
    @ExcelProperty("名称")
    private String securityName;
    @ExcelProperty("营业收入增加(同比)")
    private String AddOperatingIncomeSame;
    @ExcelProperty("营业收入增加(环比比)")
    private String AddOperatingIncomeComp;
    @ExcelProperty("净利润增加(同比)")
    private String AddNetProfitSame;
    @ExcelProperty("净利润增加(环比)")
    private String AddNetProfitComp;

}
