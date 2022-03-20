package com.wy.stock.kline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yunwang
 * @Date 2022-03-20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KLineDataEntity {
    /**
     * code
     */
    private String code;
    /**
     * name
     */
    private String name;
    /**
     * 所有交易数据
     */
    private List<KLineEntity> klines;

}
