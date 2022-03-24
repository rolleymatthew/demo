package com.wy.stock.kline;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private BigDecimal lastonequarterhigher;
    private BigDecimal lastonequarteraverage;
    private BigDecimal lastonequarterlower;
    /**
     * 往前二个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal lasttwoquarterhigher;
    private BigDecimal lasttwoquarteraverage;
    private BigDecimal lasttwoquarterlower;
    /**
     * 往前三个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal lastthreequarterhigher;
    private BigDecimal lastthreequarteraverage;
    private BigDecimal lastthreequarterlower;
    /**
     * 往前四个季度的最高价、最低价、每周末平均价
     */
    private BigDecimal lastfourquarterhigher;
    private BigDecimal lastfourquarteraverage;
    private BigDecimal lastfourquarterlower;

    private BigDecimal lastoneyearhigher;
    private BigDecimal lastoneyearaverage;
    private BigDecimal lastoneyearlower;

    private BigDecimal lasttwoyearhigher;
    private BigDecimal lasttwoyearaverage;
    private BigDecimal lasttwoyearlower;

    private BigDecimal lastthreeyearhigher;
    private BigDecimal lastthreeyearaverage;
    private BigDecimal lastthreeyearlower;

    private BigDecimal lastfouryearhigher;
    private BigDecimal lastfouryearaverage;
    private BigDecimal lastfouryearlower;

    /**
     * 每股基本收益
     */
    private String onequarterdate;
    private BigDecimal oneepstotal;
    private BigDecimal oneepscurrent;

    private String twoquarterdate;
    private BigDecimal twoepstotal;
    private BigDecimal twoepscurrent;

    private String threequarterdate;
    private BigDecimal threeepstotal;
    private BigDecimal threeepscurrent;

    private String fourquarterdate;
    private BigDecimal fourepstotal;
    private BigDecimal fourepscurrent;

}
