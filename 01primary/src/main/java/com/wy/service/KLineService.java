package com.wy.service;

import com.wy.stock.kline.KLineDataEntity;
import com.wy.stock.kline.KLineYBDatasDTO;

/**
 * @Author: yunwang
 */
public interface KLineService {

    void storeKLineExcle();
    /**
     * 保存K线数据到本地excle文件
     * @param code
     */
    void storeKLineExcle(String code,String exchange);

    /**
     * 从excle文件中找指定的代码K线数据
     * @param code
     * @return
     */
    KLineDataEntity findKLineByCode(String code);

    /**
     * 读取需要的数据
     * @param code          股票代码
     * @param selectedDate 最新的财报日期
     * @return
     */
    KLineYBDatasDTO findYBDateKlines(String code, String selectedDate);

}
