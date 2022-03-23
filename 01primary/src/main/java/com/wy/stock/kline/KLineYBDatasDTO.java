package com.wy.stock.kline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: yunwang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KLineYBDatasDTO {
    /**
     * 往前一个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal LastOneQuarterHigher;
    private BigDecimal LastOneQuarterAverage;
    private BigDecimal LastOneQuarterLower;
    /**
     * 往前二个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal LastTwoQuarterHigher;
    private BigDecimal LastTwoQuarterAverage;
    private BigDecimal LastTwoQuarterLower;
    /**
     * 往前三个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal LastThreeQuarterHigher;
    private BigDecimal LastThreeQuarterAverage;
    private BigDecimal LastThreeQuarterLower;
    /**
     * 往前四个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal LastFourQuarterHigher;
    private BigDecimal LastFourQuarterAverage;
    private BigDecimal LastFourQuarterLower;
}
