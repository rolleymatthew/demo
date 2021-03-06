package com.wy.stock.kline;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yunwang
 * @Date 2022-03-20
 */
@Data
public class KLineEntity {
    /**
     * 交易日期
     */
    @ExcelProperty("交易日期")
    private String date;
    /**
     * 开盘价
     */
    @ExcelProperty("开盘价")
    private String open;
    /**
     * 收盘价
     */
    @ExcelProperty("收盘价")
    private String close;
    /**
     * 单日最高价
     */
    @ExcelProperty("单日最高价")
    private String higher;
    /**
     * 当日最低价
     */
    @ExcelProperty("当日最低价")
    private String lower;
    /**
     * 成交量
     */
    @ExcelProperty("成交量")
    private String vol;
    /**
     * 成交金额
     */
    @ExcelProperty("成交金额")
    private String amount;
    /**
     * 涨幅
     */
    @ExcelProperty("涨幅")
    private String amplitude;
    /**
     * 涨跌金额
     */
    @ExcelProperty("涨跌金额")
    private String amountOfIncrease;
    /**
     * 振幅
     */
    @ExcelProperty("振幅")
    private String UpDownAmount;
    /**
     * 换手率
     */
    @ExcelProperty("换手率")
    private String turnOver;

}
