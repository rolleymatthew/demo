package com.wy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.wy.bean.Contant;
import com.wy.bean.ProfitDateBean;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.KLineService;
import com.wy.stock.finance.FinanceCommonService;
import com.wy.stock.kline.KLineDataEntity;
import com.wy.stock.kline.KLineEntity;
import com.wy.stock.kline.KLineSpider;
import com.wy.utils.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yunwang
 */
@Slf4j
@Service("kLineService")
public class KLineServiceImpl implements KLineService {

    public static String PATH_MAIN = Contant.DIR + File.separator + "kline" + File.separator;
    public static String FILE_NAME = "k%s.xlsx";

    @Autowired
    StockCodeYmlBean stockCodeYmlBean;

    @Override
    public void storeKLineExcle() {
        List<String> allCodes = stockCodeYmlBean.getSh().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());

        allCodes.parallelStream().forEach(
                x -> {
                    storeKLineExcle(x,"1");
                }
        );

        allCodes = stockCodeYmlBean.getSz().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        allCodes.parallelStream().forEach(
                x -> {
                    storeKLineExcle(x,"0");
                }
        );

        allCodes = stockCodeYmlBean.getKc().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        allCodes.parallelStream().forEach(
                x -> {
                    storeKLineExcle(x,"1");
                }
        );

    }

    @Override
    public void storeKLineExcle(String code,String exchange) {

        log.info("storeKLineExcle:" + code);
        List<String> errorCode = new ArrayList<String>();
        //1.爬虫爬
        KLineDataEntity kLineDataEntity = KLineSpider.getkLineDataEntity(exchange,code, "0");
        if (null == kLineDataEntity || CollectionUtils.isEmpty(kLineDataEntity.getKlines())) {
            errorCode.add(code);
        }
        //2.存excle
        String fileName = PATH_MAIN + String.format(FILE_NAME, code);
        try {
            FilesUtil.mkdirs(PATH_MAIN);
        } catch (IOException e) {
        }
        String sheetName = code + stockCodeYmlBean.getAcode().get(code);
        EasyExcel.write(fileName, KLineEntity.class)
                .sheet(sheetName)
                .doWrite(kLineDataEntity.getKlines());
    }

    @Override
    public KLineDataEntity findKLineByCode(String code) {
        return null;
    }
}
