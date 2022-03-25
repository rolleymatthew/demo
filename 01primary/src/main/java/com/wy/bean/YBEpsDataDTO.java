package com.wy.bean;


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
public class YBEpsDataDTO {
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

    /**
     * 年度eps累计
     */
    private String oneyeardate;
    private BigDecimal oneyearepstotal;
    private String twoyeardate;
    private BigDecimal twoyearepstotal;
    private String threeyeardate;
    private BigDecimal threeyearepstotal;
    private String fouryeardate;
    private BigDecimal fouryearepstotal;
    private BigDecimal fouryearavarageepstotal;

    /**
     * PE
     */
    private String oneyearpedate;
    private BigDecimal oneyearpehigher;
    private BigDecimal oneyearpeaverage;
    private BigDecimal oneyearpelower;

    private String twoyearpedate;
    private BigDecimal twoyearpehigher;
    private BigDecimal twoyearpeaverage;
    private BigDecimal twoyearpelower;

    private String threeyearpedate;
    private BigDecimal threeyearpehigher;
    private BigDecimal threeyearpeaverage;
    private BigDecimal threeyearpelower;

    private String fouryearpedate;
    private BigDecimal fouryearpehigher;
    private BigDecimal fouryearpeaverage;
    private BigDecimal fouryearpelower;

    private BigDecimal fouryearavagpehigher;
    private BigDecimal fouryearavagpemiddle;
    private BigDecimal fouryearavagpelower;

    private BigDecimal fourquarteravagpehigher;
    private BigDecimal fourquarteravagpemiddle;
    private BigDecimal fourquarteravagpelower;
    /**
     * 高档价、合理价、低档价
     */
    private BigDecimal pricehigher;
    private BigDecimal pricemiddle;
    private BigDecimal pricelower;

    private BigDecimal pricequarterhigher;
    private BigDecimal pricequartermiddle;
    private BigDecimal pricequarterlower;
}
