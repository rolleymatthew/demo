package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author yunwang
 * @Date 2021-10-20
 */
@Data
public class HSZHVoBean {
    @ExcelProperty("代码")
    private String securityCode;

    @ExcelProperty("名称")
    private String securityName;

    @ExcelProperty("占流通股比")
    private Double freeSharesRatio;

    @ExcelProperty("占总股比")
    private Double totalSharesRatio;

    @ExcelProperty("持股数(万股)")
    private Double holdShares;

    @ExcelProperty("持有市值(万元)")
    private Double holdMarketCap;

    @ExcelProperty("所属板块")
    private String industryName;

    @ExcelProperty("今日增持市值(万元)")
    private Double addMarketCap;

    @ExcelProperty("持有市值变化(万元)")
    private Double changeMarketCap;

    @ExcelProperty("买入天数")
    private Double BuyDayCount;

    @ExcelProperty("卖出天数")
    private Double SellDayCount;

}
