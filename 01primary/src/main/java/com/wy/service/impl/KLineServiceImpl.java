package com.wy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.Contant;
import com.wy.bean.StockCodeYmlBean;
import com.wy.bean.YBEpsDataDTO;
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
    public YBEpsDataDTO findYBDateKlines(String code, String selectedDate) {
        KLineDataEntity kLineByCode = findKLineByCode(code);
        List<KLineEntity> klines = kLineByCode.getKlines();
        //1.本季度的K线
        YBEpsDataDTO yBEpsDataDTO = getkLineYBOneQuarterDatasDTO(klines, DateUtil.parseDate(selectedDate));
        //2.往前推一个季度
        yBEpsDataDTO = getkLineYBTwoQuarterDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));
        //3.往前推两个季度
        yBEpsDataDTO = getkLineYBThreeQuarterDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));
        //4.往前推三个季度
        yBEpsDataDTO = getkLineYBFourQuarterDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));
        //5.年度的k线
        yBEpsDataDTO = getkLineYBOneYearDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));
        yBEpsDataDTO = getkLineYBTwoYearDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));
        yBEpsDataDTO = getkLineYBThreeYearDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));
        yBEpsDataDTO = getkLineYBFourYearDatasDTO(yBEpsDataDTO, klines, DateUtil.parseDate(selectedDate));


        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBFourYearDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getFourYearEndDate(date), DateUtil.getFourYearStartDate(date));
        yBEpsDataDTO.setLastfouryearhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLastfouryearlower(getLower(kLinePart));
        yBEpsDataDTO.setLastfouryearaverage(getYearAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBThreeYearDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getThreeYearEndDate(date), DateUtil.getThreeYearStartDate(date));
        yBEpsDataDTO.setLastthreeyearhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLastthreeyearlower(getLower(kLinePart));
        yBEpsDataDTO.setLastthreeyearaverage(getYearAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBTwoYearDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getTwoYearEndDate(date), DateUtil.getTwoYearStartDate(date));
        yBEpsDataDTO.setLasttwoyearhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLasttwoyearlower(getLower(kLinePart));
        yBEpsDataDTO.setLasttwoyearaverage(getYearAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBOneYearDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getOneYearEndTime(date), DateUtil.getOneYearStartTime(date));
        yBEpsDataDTO.setLastoneyearhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLastoneyearlower(getLower(kLinePart));
        yBEpsDataDTO.setLastoneyearaverage(getYearAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private BigDecimal getYearAvarage(List<KLineEntityDTO> kLinePart) {
        Map<Integer, List<KLineEntityDTO>> monthPartMap = kLinePart.stream().sorted(Comparator.comparing(KLineEntityDTO::getDate).reversed()).collect(Collectors.groupingBy(x -> x.getMonthNumber()));
        return new BigDecimal(getAvarage(monthPartMap)).setScale(2, RoundingMode.HALF_UP);
    }

    private YBEpsDataDTO getkLineYBFourQuarterDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getFourQuarterEndTime(date), DateUtil.getFourQuarterStartTime(date));
        yBEpsDataDTO.setLastfourquarterhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLastfourquarterlower(getLower(kLinePart));
        yBEpsDataDTO.setLastfourquarteraverage(getQuartorAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBThreeQuarterDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getThreeQuarterEndTime(date), DateUtil.getThreeQuarterStartTime(date));
        yBEpsDataDTO.setLastthreequarterhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLastthreequarterlower(getLower(kLinePart));
        yBEpsDataDTO.setLastthreequarteraverage(getQuartorAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBTwoQuarterDatasDTO(YBEpsDataDTO yBEpsDataDTO, List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getTwoQuarterEndTime(date), DateUtil.getTwoQuarterStartTime(date));
        yBEpsDataDTO.setLasttwoquarterhigher(getHiger(kLinePart));
        yBEpsDataDTO.setLasttwoquarterlower(getLower(kLinePart));
        yBEpsDataDTO.setLasttwoquarteraverage(getQuartorAvarage(kLinePart));
        return yBEpsDataDTO;
    }

    private YBEpsDataDTO getkLineYBOneQuarterDatasDTO(List<KLineEntity> klines, Date date) {
        List<KLineEntityDTO> kLinePart = getkLinePartS(klines, DateUtil.getOneQuarterEndTime(date), DateUtil.getOneQuarterStartTime(date));
        YBEpsDataDTO yBEpsDataDTO = YBEpsDataDTO.builder()
                .lastonequarterhigher(getHiger(kLinePart))
                .lastonequarterlower(getLower(kLinePart))
                .build();
        //每周周末收盘平均价
        yBEpsDataDTO.setLastonequarteraverage(getQuartorAvarage(kLinePart));
        return yBEpsDataDTO;
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
