package com.wy.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yunwang on 2021/10/29 10:28
 */
@NoArgsConstructor
@Data
public class ETFBean {

    @JSONField(name = "actionErrors")
    private List<?> actionErrors;
    @JSONField(name = "actionMessages")
    private List<?> actionMessages;
    @JSONField(name = "fieldErrors")
    private FieldErrorsDTO fieldErrors;
    @JSONField(name = "isPagination")
    private String isPagination;
    @JSONField(name = "jsonCallBack")
    private Object jsonCallBack;
    @JSONField(name = "locale")
    private String locale;
    @JSONField(name = "pageHelp")
    private PageHelpDTO pageHelp;
    @JSONField(name = "pageNo")
    private Object pageNo;
    @JSONField(name = "pageSize")
    private Object pageSize;
    @JSONField(name = "queryDate")
    private String queryDate;
    @JSONField(name = "result")
    private List<PageHelpDTO.DataDTO> result;
    @JSONField(name = "securityCode")
    private String securityCode;
    @JSONField(name = "sqlId")
    private String sqlId;
    @JSONField(name = "texts")
    private Object texts;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "validateCode")
    private String validateCode;

    @NoArgsConstructor
    @Data
    public static class FieldErrorsDTO {
    }

    @NoArgsConstructor
    @Data
    public static class PageHelpDTO {
        @JSONField(name = "beginPage")
        private Integer beginPage;
        @JSONField(name = "cacheSize")
        private Integer cacheSize;
        @JSONField(name = "data")
        private List<DataDTO> data;
        @JSONField(name = "endDate")
        private Object endDate;
        @JSONField(name = "endPage")
        private Object endPage;
        @JSONField(name = "objectResult")
        private Object objectResult;
        @JSONField(name = "pageCount")
        private Integer pageCount;
        @JSONField(name = "pageNo")
        private Integer pageNo;
        @JSONField(name = "pageSize")
        private Integer pageSize;
        @JSONField(name = "searchDate")
        private Object searchDate;
        @JSONField(name = "sort")
        private Object sort;
        @JSONField(name = "startDate")
        private Object startDate;
        @JSONField(name = "total")
        private Integer total;

        @NoArgsConstructor
        @Data
        public static class DataDTO {
            @ExcelProperty("??????")
            @JSONField(name = "STAT_DATE")
            private String statDate;
            @ExcelProperty("??????")
            @JSONField(name = "SEC_NAME")
            private String secName;
            @ExcelIgnore
            @JSONField(name = "ETF_TYPE")
            private String etfType;
            @ExcelProperty("??????")
            @JSONField(name = "SEC_CODE")
            private Integer secCode;
            @ExcelIgnore
            @JSONField(name = "NUM")
            private String num;
            @ExcelProperty("??????(??????)")
            @JSONField(name = "TOT_VOL")
            private Double totVol;
            @ExcelProperty("??????????????????(??????)")
            private Double addVol;
        }
    }
}
