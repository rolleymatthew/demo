package com.wy.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by yunwang on 2021/10/18 14:08
 */
@NoArgsConstructor
@Data
public class EastMoneyBeab {

    @JSONField(name = "version")
    private String version;
    @JSONField(name = "result")
    private ResultDTO result;
    @JSONField(name = "success")
    private Boolean success;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "code")
    private Integer code;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @JSONField(name = "pages")
        private Integer pages;
        @JSONField(name = "data")
        private List<DataDTO> data;
        @JSONField(name = "count")
        private Integer count;

        @NoArgsConstructor
        @Data
        public static class DataDTO {
            @ExcelProperty("代码")
            @JSONField(name = "SECURITY_CODE")
            private String securityCode;

            @ExcelIgnore
            @JSONField(name = "SECUCODE")
            private String secucode;

            @ExcelIgnore
            @JSONField(name = "MUTUAL_TYPE")
            private String mutualType;

            @ExcelProperty("日期")
            @JSONField(name = "TRADE_DATE")
            private String tradeDate;

            @ExcelIgnore
            @JSONField(name = "INTERVAL_TYPE")
            private String intervalType;

            @ExcelProperty("名称")
            @JSONField(name = "SECURITY_NAME")
            private String securityName;

            @ExcelIgnore
            @JSONField(name = "SECURITY_INNER_CODE")
            private String securityInnerCode;

            @ExcelIgnore
            @JSONField(name = "ORG_CODE")
            private String orgCode;

            @ExcelIgnore
            @JSONField(name = "PARTICIPANT_NUM")
            private Integer participantNum;

            @ExcelProperty("生效日期")
            @JSONField(name = "EFFECTIVE_DATE")
            private String effectiveDate;

            @ExcelProperty("持有占A股比")
            @JSONField(name = "A_SHARES_RATIO")
            private Double aSharesRatio;

            @ExcelProperty("持有比")
            @JSONField(name = "HOLD_SHARES_RATIO")
            private Double holdSharesRatio;

            @ExcelProperty("持股数(万股)")
            @JSONField(name = "HOLD_SHARES")
            private Double holdShares;

            @ExcelProperty("持有市值(万元)")
            @JSONField(name = "HOLD_MARKET_CAP")
            private Double holdMarketCap;

            @ExcelProperty("占流通股比")
            @JSONField(name = "FREE_SHARES_RATIO")
            private Double freeSharesRatio;

            @ExcelProperty("占总股比")
            @JSONField(name = "TOTAL_SHARES_RATIO")
            private Double totalSharesRatio;

            @ExcelIgnore
            @JSONField(name = "CLOSE_PRICE")
            private Double closePrice;

            @ExcelIgnore
            @JSONField(name = "CHANGE_RATE")
            private Double changeRate;

            @ExcelProperty("所属板块代码")
            @JSONField(name = "INDUSTRY_CODE")
            private String industryCode;

            @ExcelProperty("所属板块")
            @JSONField(name = "INDUSTRY_NAME")
            private String industryName;

            @ExcelIgnore
            @JSONField(name = "ORIG_INDUSTRY_CODE")
            private String origIndustryCode;

            @ExcelIgnore
            @JSONField(name = "CONCEPT_CODE")
            private String conceptCode;

            @ExcelIgnore
            @JSONField(name = "CONCEPT_NAME")
            private String conceptName;

            @ExcelIgnore
            @JSONField(name = "AREA_CODE")
            private String areaCode;

            @ExcelProperty("所属地区板块")
            @JSONField(name = "AREA_NAME")
            private String areaName;

            @ExcelIgnore
            @JSONField(name = "ORIG_AREA_CODE")
            private String origAreaCode;

            @ExcelIgnore
            @JSONField(name = "FREECAP")
            private Double freecap;

            @ExcelIgnore
            @JSONField(name = "TOTAL_MARKET_CAP")
            private Double totalMarketCap;

            @ExcelIgnore
            @JSONField(name = "FREECAP_HOLD_RATIO")
            private Double freecapHoldRatio;

            @ExcelIgnore
            @JSONField(name = "TOTAL_MARKETCAP_HOLD_RATIO")
            private Double totalMarketcapHoldRatio;

            @ExcelProperty("今日增持市值(万元)")
            @JSONField(name = "ADD_MARKET_CAP")
            private Double addMarketCap;

            @ExcelProperty("今日增持股数(万)")
            @JSONField(name = "ADD_SHARES_REPAIR")
            private Double addSharesRepair;

            @ExcelProperty("今日市值增幅比")
            @JSONField(name = "ADD_SHARES_AMP")
            private Double addSharesAmp;

            @ExcelProperty("今日增持占流通股比")
            @JSONField(name = "FREECAP_RATIO_CHG")
            private Double freecapRatioChg;

            @ExcelProperty("今日增持占总股本比")
            @JSONField(name = "TOTAL_RATIO_CHG")
            private Double totalRatioChg;

            @ExcelProperty("昨日市值(万元)")
            @JSONField(name = "HOLD_MARKETCAP_BEFORECHG")
            private Double holdMarketcapBeforechg;

            @ExcelProperty("昨日股数(万股)")
            @JSONField(name = "HOLD_SHARES_BEFORECHG")
            private Double holdSharesBeforechg;

            @ExcelIgnore
            @JSONField(name = "HOLD_MARKETCAP_CHG1")
            private Double holdMarketcapChg1;

            @ExcelIgnore
            @JSONField(name = "HOLD_MARKETCAP_CHG5")
            private Double holdMarketcapChg5;

            @ExcelIgnore
            @JSONField(name = "HOLD_MARKETCAP_CHG10")
            private Double holdMarketcapChg10;

            @ExcelIgnore
            @JSONField(name = "INDUSTRY_CODE_NEW")
            private String industryCodeNew;

            @ExcelIgnore
            @JSONField(name = "AREA_CODE_NEW")
            private String areaCodeNew;
        }
    }
}
