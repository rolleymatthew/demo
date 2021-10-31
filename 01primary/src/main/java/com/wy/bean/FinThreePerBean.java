package com.wy.bean;

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
    @ExcelProperty("毛利率")
    private Double grossProfit;
    @ExcelProperty("营业利润率")
    private Double operatProfit;
    @ExcelProperty("净利率")
    private Double netProfit;
}
