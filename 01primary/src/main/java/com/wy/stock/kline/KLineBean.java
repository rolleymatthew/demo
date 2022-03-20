package com.wy.stock.kline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yunwang
 * @Date 2022-03-20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KLineBean {

    /**
     * rc
     */
    private Integer rc;
    /**
     * rt
     */
    private Integer rt;
    /**
     * svr
     */
    private Integer svr;
    /**
     * lt
     */
    private Integer lt;
    /**
     * full
     */
    private Integer full;
    /**
     * data
     */
    private DataDTO data;

    /**
     * DataDTO
     */
    @NoArgsConstructor
    @Data
    public static class DataDTO {
        /**
         * 代码
         */
        private String code;
        /**
         * 股票名称
         */
        private String name;
        /**
         * klines所有数据
         */
        private List<String> klines;
    }
}
