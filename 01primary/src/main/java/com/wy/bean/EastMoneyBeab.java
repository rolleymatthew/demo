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
            @JSONField(name = "PARTICIPANT_NUM")
            private Integer participantNum;
            @JSONField(name = "EFFECTIVE_DATE")
            private String effectiveDate;
            @JSONField(name = "A_SHARES_RATIO")
            private Double aSharesRatio;
            @JSONField(name = "HOLD_SHARES_RATIO")
            private Double holdSharesRatio;
            @JSONField(name = "HOLD_SHARES")
            private Double holdShares;
            @JSONField(name = "HOLD_MARKET_CAP")
            private Double holdMarketCap;
            @JSONField(name = "FREE_SHARES_RATIO")
            private Double freeSharesRatio;
            @JSONField(name = "TOTAL_SHARES_RATIO")
            private Double totalSharesRatio;
            @JSONField(name = "CLOSE_PRICE")
            private Double closePrice;
            @JSONField(name = "CHANGE_RATE")
            private Double changeRate;
            @JSONField(name = "INDUSTRY_CODE")
            private String industryCode;
            @JSONField(name = "INDUSTRY_NAME")
            private String industryName;
            @JSONField(name = "ORIG_INDUSTRY_CODE")
            private String origIndustryCode;
            @JSONField(name = "CONCEPT_CODE")
            private String conceptCode;
            @JSONField(name = "CONCEPT_NAME")
            private String conceptName;
            @JSONField(name = "AREA_CODE")
            private String areaCode;
            @JSONField(name = "AREA_NAME")
            private String areaName;
            @JSONField(name = "ORIG_AREA_CODE")
            private String origAreaCode;
            @JSONField(name = "FREECAP")
            private Double freecap;
            @JSONField(name = "TOTAL_MARKET_CAP")
            private Double totalMarketCap;
            @JSONField(name = "FREECAP_HOLD_RATIO")
            private Double freecapHoldRatio;
            @JSONField(name = "TOTAL_MARKETCAP_HOLD_RATIO")
            private Double totalMarketcapHoldRatio;
            @JSONField(name = "ADD_MARKET_CAP")
            private Double addMarketCap;
            @JSONField(name = "ADD_SHARES_REPAIR")
            private Double addSharesRepair;
            @JSONField(name = "ADD_SHARES_AMP")
            private Double addSharesAmp;
            @JSONField(name = "FREECAP_RATIO_CHG")
            private Double freecapRatioChg;
            @JSONField(name = "TOTAL_RATIO_CHG")
            private Double totalRatioChg;
            @JSONField(name = "HOLD_MARKETCAP_BEFORECHG")
            private Double holdMarketcapBeforechg;
            @JSONField(name = "HOLD_SHARES_BEFORECHG")
            private Double holdSharesBeforechg;
            @JSONField(name = "HOLD_MARKETCAP_CHG1")
            private Double holdMarketcapChg1;
            @JSONField(name = "HOLD_MARKETCAP_CHG5")
            private Double holdMarketcapChg5;
            @JSONField(name = "HOLD_MARKETCAP_CHG10")
            private Double holdMarketcapChg10;
            @JSONField(name = "INDUSTRY_CODE_NEW")
            private String industryCodeNew;
            @JSONField(name = "AREA_CODE_NEW")
            private String areaCodeNew;
        }
    }
}
