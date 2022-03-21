package com.wy.service;

import com.wy.stock.kline.KLineDataEntity;
import com.wy.stock.kline.KLineEntity;

/**
 * @Author: yunwang
 */
public interface KLineService {

    void storeKLineExcle();
    /**
     * 保存K线数据到本地excle文件
     * @param code
     */
    void storeKLineExcle(String code);

    /**
     * 从excle文件中找指定的代码K线数据
     * @param code
     * @return
     */
    KLineDataEntity findKLineByCode(String code);
}
