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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KLineEntityDTO {
    /**
     * 交易日期
     */
    private String date;
    /**
     * 收盘价
     */
    private Double close;
    /**
     * 单日最高价
     */
    private Double higher;
    /**
     * 当日最低价
     */
    private Double lower;

}
