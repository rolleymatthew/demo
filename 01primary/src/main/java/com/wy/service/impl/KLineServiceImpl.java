package com.wy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.Contant;
import com.wy.bean.ETFBean;
import com.wy.bean.ProfitDateBean;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.KLineService;
import com.wy.stock.finance.FinanceCommonService;
import com.wy.stock.kline.*;
import com.wy.utils.DateUtil;
import com.wy.utils.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
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
                    storeKLineExcle(x, "1");
                }
        );

        allCodes = stockCodeYmlBean.getSz().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        allCodes.parallelStream().forEach(
                x -> {
                    storeKLineExcle(x, "0");
                }
        );

        allCodes = stockCodeYmlBean.getKc().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        allCodes.parallelStream().forEach(
                x -> {
                    storeKLineExcle(x, "1");
                }
        );

    }

    @Override
    public void storeKLineExcle(String code, String exchange) {

        log.info("storeKLineExcle:" + code);
        List<String> errorCode = new ArrayList<String>();
        //1.爬虫爬
        KLineDataEntity kLineDataEntity = KLineSpider.getkLineDataEntity(exchange, code, "0");
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
        String fileName = PATH_MAIN + String.format(FILE_NAME, code);
        List<KLineEntity> allFundDataList = new ArrayList<KLineEntity>();
        EasyExcel.read(fileName, KLineEntity.class
                , new PageReadListener<KLineEntity>(dataList -> {
                    allFundDataList.addAll(dataList);
                })).sheet(0).doRead();
        KLineDataEntity kLineDataEntity = KLineDataEntity.builder()
                .code(code)
                .klines(allFundDataList)
                .name(stockCodeYmlBean.getAcode().get(code))
                .build();
        return kLineDataEntity;
    }

    @Override
    public KLineYBDatasDTO findLastOneQuarterKlines(String code) {
        KLineDataEntity kLineByCode = findKLineByCode(code);
        List<KLineEntity> klines = kLineByCode.getKlines();
        //1.最近一季度的K线
        Date lastQuarterEndTime = DateUtil.getLastQuarterEndTime();
        Date lastQuarterStartTime = DateUtil.getLastQuarterStartTime();
        List<KLineEntityDTO> collect = klines.stream()
                .filter(x -> lastQuarterEndTime.after(DateUtil.parseDate(x.getDate())) && lastQuarterStartTime.before(DateUtil.parseDate(x.getDate())))
                .map(ff -> {
                    return KLineEntityDTO.builder()
                            .date(ff.getDate())
                            .close(NumberUtils.toDouble(ff.getClose()))
                            .higher(NumberUtils.toDouble(ff.getHigher()))
                            .lower(NumberUtils.toDouble(ff.getLower()))
                            .build();
                })
                .collect(Collectors.toList());
        //当季最低价
        Double min = collect.stream().mapToDouble(KLineEntityDTO::getLower).min().getAsDouble();
        //当季最高价
        Double max = collect.stream().mapToDouble(KLineEntityDTO::getHigher).max().getAsDouble();
        System.out.println(min);
        System.out.println(max);
        //每周周末收盘平均价
        return null;
    }
}
