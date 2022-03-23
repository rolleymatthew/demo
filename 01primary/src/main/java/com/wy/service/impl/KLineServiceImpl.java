package com.wy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.Contant;
import com.wy.bean.StockCodeYmlBean;
import com.wy.service.KLineService;
import com.wy.stock.kline.*;
import com.wy.utils.DateUtil;
import com.wy.utils.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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
        KLineYBDatasDTO kLineYBDatasDTO = getkLineYBLastQuarterDatasDTO(klines);
        //2.二季度
        kLineYBDatasDTO =getkLineYBTwoQuarterDatasDTO(kLineYBDatasDTO,klines);
        //3.三季度
        kLineYBDatasDTO =getkLineYBThreeQuarterDatasDTO(kLineYBDatasDTO,klines);
        //4.四季度
        kLineYBDatasDTO =getkLineYBFourQuarterDatasDTO(kLineYBDatasDTO,klines);
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBFourQuarterDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getLastTwoQuarterEndTime(), DateUtil.getLastTwoQuarterStartTime());
        kLineYBDatasDTO.setLastFourQuarterHigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastFourQuarterLower(getLower(kLinePart));
        kLineYBDatasDTO.setLastFourQuarterAverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBThreeQuarterDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getLastTwoQuarterEndTime(), DateUtil.getLastTwoQuarterStartTime());
        kLineYBDatasDTO.setLastThreeQuarterHigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastThreeQuarterLower(getLower(kLinePart));
        kLineYBDatasDTO.setLastThreeQuarterAverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBTwoQuarterDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getLastTwoQuarterEndTime(), DateUtil.getLastTwoQuarterStartTime());
        kLineYBDatasDTO.setLastTwoQuarterHigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastTwoQuarterLower(getLower(kLinePart));
        kLineYBDatasDTO.setLastTwoQuarterAverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBLastQuarterDatasDTO(List<KLineEntity> klines) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getLastQuarterEndTime(), DateUtil.getLastQuarterStartTime());
        KLineYBDatasDTO kLineYBDatasDTO = KLineYBDatasDTO.builder()
                .LastOneQuarterHigher(getHiger(kLinePart))
                .LastOneQuarterLower(getLower(kLinePart))
                .build();
        //每周周末收盘平均价
        kLineYBDatasDTO.setLastOneQuarterAverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private BigDecimal getLower(List<KLineEntityDTO> kLinePart) {
        double asDouble = kLinePart.stream().mapToDouble(KLineEntityDTO::getLower).min().getAsDouble();
        return new BigDecimal(asDouble).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getHiger(List<KLineEntityDTO> kLinePart) {
        double asDouble = kLinePart.stream().mapToDouble(KLineEntityDTO::getHigher).max().getAsDouble();
        return new BigDecimal(asDouble).setScale(2,RoundingMode.HALF_UP);
    }

    private BigDecimal getQuartorAvarage(List<KLineEntityDTO> kLinePart) {
        Map<Integer, List<KLineEntityDTO>> weekPartMap = kLinePart.stream().sorted(Comparator.comparing(KLineEntityDTO::getDate).reversed()).collect(Collectors.groupingBy(x -> x.getWeekNumber()));
        List<KLineEntityDTO> weekendInMonth = weekPartMap.entrySet().stream().map(s -> {
            KLineEntityDTO kLineEntityDTO = s.getValue().stream().findFirst().get();
            return kLineEntityDTO;
        }).sorted(Comparator.comparing(KLineEntityDTO::getDate)).collect(Collectors.toList());
        double asDouble = weekendInMonth.stream().mapToDouble(KLineEntityDTO::getClose).average().getAsDouble();
        return new BigDecimal(asDouble).setScale(2, RoundingMode.HALF_UP);
    }

    private List<KLineEntityDTO> getkLinePartS(List<KLineEntity> klines, Date endTime, Date startTime) {
        List<KLineEntityDTO> kLinePart = klines.stream()
                .filter(x -> endTime.after(DateUtil.parseDate(x.getDate())) && startTime.before(DateUtil.parseDate(x.getDate())))
                .map(ff -> {
                    return KLineEntityDTO.builder()
                            .date(ff.getDate())
                            .close(NumberUtils.toDouble(ff.getClose()))
                            .higher(NumberUtils.toDouble(ff.getHigher()))
                            .lower(NumberUtils.toDouble(ff.getLower()))
                            .weekNumber(DateUtil.getWeekNumber(DateUtil.parseDate(ff.getDate())))
                            .monthNumber(DateUtil.getMonthNumber(DateUtil.parseDate(ff.getDate())))
                            .build();
                })
                .collect(Collectors.toList());
        return kLinePart;
    }
}
