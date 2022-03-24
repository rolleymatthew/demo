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
    public KLineYBDatasDTO findSelectedQuarterKlines(String code, String selectedDate) {
        KLineDataEntity kLineByCode = findKLineByCode(code);
        List<KLineEntity> klines = kLineByCode.getKlines();
        //1.本季度的K线
        KLineYBDatasDTO kLineYBDatasDTO = getkLineYBOneQuarterDatasDTO(klines, DateUtil.parseDate(selectedDate));
        //2.往前推一个季度
        kLineYBDatasDTO = getkLineYBTwoQuarterDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));
        //3.往前推两个季度
        kLineYBDatasDTO = getkLineYBThreeQuarterDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));
        //4.往前推三个季度
        kLineYBDatasDTO = getkLineYBFourQuarterDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));
        //5.年度的k线
        kLineYBDatasDTO = getkLineYBOneYearDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));
        kLineYBDatasDTO = getkLineYBTwoYearDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));
        kLineYBDatasDTO = getkLineYBThreeYearDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));
        kLineYBDatasDTO = getkLineYBFourYearDatasDTO(kLineYBDatasDTO, klines, DateUtil.parseDate(selectedDate));


        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBFourYearDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getFourYearEndDate(date), DateUtil.getFourYearStartDate(date));
        kLineYBDatasDTO.setLastfouryearhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastfouryearlower(getLower(kLinePart));
        kLineYBDatasDTO.setLastfouryearaverage(getYearAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBThreeYearDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getThreeYearEndDate(date), DateUtil.getThreeYearStartDate(date));
        kLineYBDatasDTO.setLastthreeyearhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastthreeyearlower(getLower(kLinePart));
        kLineYBDatasDTO.setLastthreeyearaverage(getYearAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBTwoYearDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getTwoYearEndDate(date), DateUtil.getTwoYearStartDate(date));
        kLineYBDatasDTO.setLasttwoyearhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLasttwoyearlower(getLower(kLinePart));
        kLineYBDatasDTO.setLasttwoyearaverage(getYearAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBOneYearDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getOneYearEndTime(date), DateUtil.getOneYearStartTime(date));
        kLineYBDatasDTO.setLastoneyearhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastoneyearlower(getLower(kLinePart));
        kLineYBDatasDTO.setLastoneyearaverage(getYearAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private BigDecimal getYearAvarage(List<KLineEntityDTO> kLinePart) {
        Map<Integer, List<KLineEntityDTO>> monthPartMap = kLinePart.stream().sorted(Comparator.comparing(KLineEntityDTO::getDate).reversed()).collect(Collectors.groupingBy(x -> x.getMonthNumber()));
        return new BigDecimal(getAvarage(monthPartMap)).setScale(2, RoundingMode.HALF_UP);
    }

    private KLineYBDatasDTO getkLineYBFourQuarterDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getFourQuarterEndTime(date), DateUtil.getFourQuarterStartTime(date));
        kLineYBDatasDTO.setLastfourquarterhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastfourquarterlower(getLower(kLinePart));
        kLineYBDatasDTO.setLastfourquarteraverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBThreeQuarterDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getThreeQuarterEndTime(date), DateUtil.getThreeQuarterStartTime(date));
        kLineYBDatasDTO.setLastthreequarterhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLastthreequarterlower(getLower(kLinePart));
        kLineYBDatasDTO.setLastthreequarteraverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBTwoQuarterDatasDTO(KLineYBDatasDTO kLineYBDatasDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getTwoQuarterEndTime(date), DateUtil.getTwoQuarterStartTime(date));
        kLineYBDatasDTO.setLasttwoquarterhigher(getHiger(kLinePart));
        kLineYBDatasDTO.setLasttwoquarterlower(getLower(kLinePart));
        kLineYBDatasDTO.setLasttwoquarteraverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private KLineYBDatasDTO getkLineYBOneQuarterDatasDTO(List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getOneQuarterEndTime(date), DateUtil.getOneQuarterStartTime(date));
        KLineYBDatasDTO kLineYBDatasDTO = KLineYBDatasDTO.builder()
                .lastonequarterhigher(getHiger(kLinePart))
                .lastonequarterlower(getLower(kLinePart))
                .build();
        //每周周末收盘平均价
        kLineYBDatasDTO.setLastonequarteraverage(getQuartorAvarage(kLinePart));
        return kLineYBDatasDTO;
    }

    private BigDecimal getLower(List<KLineEntityDTO> kLinePart) {
        double asDouble = kLinePart.stream().mapToDouble(KLineEntityDTO::getLower).min().getAsDouble();
        return new BigDecimal(asDouble).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getHiger(List<KLineEntityDTO> kLinePart) {
        double asDouble = kLinePart.stream().mapToDouble(KLineEntityDTO::getHigher).max().getAsDouble();
        return new BigDecimal(asDouble).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getQuartorAvarage(List<KLineEntityDTO> kLinePart) {
        Map<Integer, List<KLineEntityDTO>> weekPartMap = kLinePart.stream().sorted(Comparator.comparing(KLineEntityDTO::getDate).reversed()).collect(Collectors.groupingBy(x -> x.getWeekNumber()));
        return new BigDecimal(getAvarage(weekPartMap)).setScale(2, RoundingMode.HALF_UP);
    }

    private double getAvarage(Map<Integer, List<KLineEntityDTO>> partMap) {
        List<KLineEntityDTO> kLineInPart = partMap.entrySet().stream().map(s -> {
            KLineEntityDTO kLineEntityDTO = s.getValue().stream().findFirst().get();
            return kLineEntityDTO;
        }).sorted(Comparator.comparing(KLineEntityDTO::getDate)).collect(Collectors.toList());
        double asDouble = kLineInPart.stream().mapToDouble(KLineEntityDTO::getClose).average().getAsDouble();
        return asDouble;
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
