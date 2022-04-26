package com.wy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.wy.bean.*;
import com.wy.service.KLineService;
import com.wy.service.StockService;
import com.wy.stock.etf.ETFFundDataService;
import com.wy.stock.etf.ETFFundReportService;
import com.wy.stock.finance.*;
import com.wy.stock.hszh.GetSHSZHKStockDateService;
import com.wy.stock.hszh.HSHStockReportService;
import com.wy.stock.kline.KLineDataEntity;
import com.wy.stock.kline.KLineEntity;
import com.wy.bean.YBEpsDataDTO;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yunwang on 2021/11/2 10:11
 */
@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private static AtomicInteger flag = new AtomicInteger(0);

    @Autowired
    StockCodeYmlBean stockCodeYmlBean;

    @Autowired
    KLineService kLineService;

    public static String YB_PATH = "d:\\stock\\ybFin\\pe";
    public static String YB_PATH_TEMPLETE = "d:\\stock\\ybFin\\templete";
    public static String YB_TEMPLETE_FILENAME = YB_PATH_TEMPLETE + File.separator + "pe.xlsx";
    public static String YB_DATA_FILENAME = YB_PATH + File.separator + "pe%s.xlsx";

    @Override
    public ResultVO hsshDataByDay(int dayCount) {
        if (flag.incrementAndGet() == 1) {
            GetSHSZHKStockDateService.getMutilSheet(dayCount);
            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO ETFDataByDay(int dayCount) {
        if (flag.incrementAndGet() == 1) {
            ETFFundDataService.getETFFundData(dayCount);
            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO hsshAndETFDataByDay(int dayCount) {
        if (flag.incrementAndGet() == 1) {
            ETFFundDataService.getETFFundData(dayCount);
            GetSHSZHKStockDateService.getMutilSheet(dayCount);
            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO hsshAndETFReport() {
        if (flag.incrementAndGet() == 1) {
            log.info("start hsshAndETFReport.");
            long start = System.currentTimeMillis();
            int[] days = {2, 3, 4, 5, 6, 7, 8, 9, 10};
            ETFFundReportService.analyseETF(days);

            int[] countUpZeroDays = {2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 30, 50};
            int[] ampTopDays = {3, 5, 10, 20, 30, 50};
            HSHStockReportService.getETFReport(countUpZeroDays, ampTopDays);
            log.info("end hsshAndETFReport tims:{}ms", System.currentTimeMillis() - start);
            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO FinanceDateByAllOne(String code) {
        if (flag.incrementAndGet() == 1) {
            List<String> allCodes = new ArrayList<String>();
            if (StringUtils.isEmpty(code)) {
                allCodes = stockCodeYmlBean.getAcode().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
            } else if (StringUtils.indexOf(code, ",") > -1) {
                allCodes = Stream.of(code).map(f -> f.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
            } else {
                allCodes.add(code);
            }

            String path = FinanceCommonService.PATH_ALL + File.separator;
            try {
                FilesUtil.mkdirs(path);
            } catch (IOException e) {
            }
            log.info("StockCode count: {}", allCodes.size());
            List<String> errorCodes = new ArrayList<>();
            //获取财务数据
            allCodes.parallelStream().forEach(
                    x -> {
                        ExcelWriter excelWriter = null;
                        try {
                            log.info("stock code : " + x);
                            String fileName = path + String.format(FinanceCommonService.FILE_NAME_ALL, x);
                            excelWriter = EasyExcel.write(fileName).build();
                            WriteSheet writeSheet2 = EasyExcel.writerSheet(0, "利润表").head(ProfitDateBean.class).build();
                            excelWriter.write(FinanceProfitDateService.getProfitDateBeanList(x), writeSheet2);
                            WriteSheet writeSheet = EasyExcel.writerSheet(1, "资产负债表").head(BalanceDateBean.class).build();
                            excelWriter.write(FinanceBalanceDateService.getBalanceDateBeanList(x), writeSheet);
                            WriteSheet writeSheet1 = EasyExcel.writerSheet(2, "现金流量表").head(CashFlowBean.class).build();
                            excelWriter.write(FinanceCashFlowDateService.getCashFlowBeanList(x), writeSheet1);
                            WriteSheet writeSheet3 = EasyExcel.writerSheet(3, "主要财务数据").head(FinanceDataBean.class).build();
                            excelWriter.write(FinanceDateWriteService.getFinanceDataBeanList(x), writeSheet3);
                        } catch (Exception e) {
                            errorCodes.add(x);
                            log.error("stock {} error : {}", x, e.toString());
                        } finally {
                            // 千万别忘记finish 会帮忙关闭流
                            if (excelWriter != null) {
                                excelWriter.finish();
                            }
                        }
                    }
            );

            //输出获取失败代码到文件
            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO FinAllDateReport(String code) {
        int[] counts = {1, 2, 3};
        List<String> allCodes = new ArrayList<>();
        if (StringUtils.isEmpty(code)) {
            allCodes = stockCodeYmlBean.getAcode().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        } else if (StringUtils.isNotEmpty(code) && StringUtils.contains(code, ",")) {
            allCodes = Stream.of(code).map(l -> l.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
        } else {
            allCodes.add(code);
        }
        log.info("start report {} finance .", allCodes.size());
        long start = System.currentTimeMillis();
        //读取文件三大报表
        Map<String, StockFinDateBean> stockFinDateMap = FinanceCommonService.getStockFinDateMap(allCodes);

        Map<String, List<ProfitDateBean>> profitListMap = stockFinDateMap.entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue().getProfitDateBean()));
        Map<String, List<BalanceDateBean>> balanceListMap = stockFinDateMap.entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue().getBalanceDateBean()));
        Map<String, List<CashFlowBean>> cashListMap = stockFinDateMap.entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue().getCashFlowBean()));
        Map<String, List<FinanceDataBean>> finListMap = stockFinDateMap.entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue().getFinanceDataBean()));

        //计算毛利率、营业利润率、净利率
        Map<String, List<FinThreePerBean>> finPerMap = ProfitReportService.getFinPerMap(profitListMap);

        //填充三率每个报告期增加减少
        Map<String, List<FinThreePerBean>> threePerMap = ProfitReportService.fillFinPerMap(finPerMap);
        //找到三率三升的并输出文件
        ProfitReportService.outputUpFinThreePer(counts, threePerMap, stockCodeYmlBean.getAcode());

        //计算营收和利润比例
        List<OperatProfitBean> operatProfitBeans = ProfitReportService.getOperatProfitBeans(profitListMap, stockCodeYmlBean.getAcode());

        ProfitReportService.outputOpeProfitPer(operatProfitBeans);

        Map<String, List<ZQHFinBean>> zqhBeanMap = ProfitReportService.getZqhBeanMap(profitListMap, balanceListMap, cashListMap, finListMap);

        //输出文件
        ProfitReportService.outPutZQHFile(zqhBeanMap, stockCodeYmlBean.getAcode());

        log.info("end finance report {}. {}s", allCodes.size(), (System.currentTimeMillis() - start) / 1000);
        return ResultVO.ok();
    }

    @Override
    public ResultVO FinYBDateReport(String sigleCode, String selectDate) {
        List<String> allCodes = new ArrayList<>();
        if (StringUtils.isEmpty(sigleCode)) {
            allCodes = stockCodeYmlBean.getAcode().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        } else if (StringUtils.isNotEmpty(sigleCode) && StringUtils.contains(sigleCode, ",")) {
            allCodes = Stream.of(sigleCode).map(l -> l.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
        } else {
            allCodes.add(sigleCode);
        }
        log.info("start report {} finance .", allCodes.size());

        allCodes.stream().forEach(
                x -> {
                    log.info(JSON.toJSONString(getYBExcleFile(x, selectDate)));
                }
        );

        return ResultVO.ok();
    }

    private ResultVO getYBExcleFile(String sigleCode, String selectDate) {
        Map<String, StockFinDateBean> stockFinDateMap = FinanceCommonService.getStockFinDateMap(Arrays.asList(sigleCode));
        //检查是否上市满4年,具有完整的四年财务数据，不满四年无法计算
        if (!stockFinDateMap.containsKey(sigleCode)
                || hasAllDate(stockFinDateMap.get(sigleCode))
                || isFullTime(stockFinDateMap.get(sigleCode))) {
            return ResultVO.build(-1, "没有财务数据");
        }
        //1.爬虫爬
        kLineService.storeKLineExcle(sigleCode, getExchange(sigleCode));
        KLineDataEntity kLineByCode = kLineService.findKLineByCode(sigleCode);
        KLineEntity kLineEntity = kLineByCode.getKlines().stream().findFirst().orElse(null);
        if (kLineEntity == null) {
            return ResultVO.build(-1, "没有K线数据");
        }
        if (isNotFourYearKLine(kLineByCode.getKlines(), stockFinDateMap.get(sigleCode))) {
            return ResultVO.build(-1, "K线数据不足四年");
        }
        StockFinDateBean stockFinDateBean = stockFinDateMap.get(sigleCode);
        ProfitDateBean profitDateBean = stockFinDateBean.getProfitDateBean().stream().findFirst().orElse(null);
        if (StringUtils.isEmpty(selectDate)) {
            selectDate = profitDateBean.getReportDate();
        }
        //1.计算股价
        YBEpsDataDTO yBEpsDataDTO = kLineService.findYBDateKlines(sigleCode, selectDate);
        //2.计算每股收益
        countEPS(sigleCode, yBEpsDataDTO, stockFinDateMap, selectDate);
        //3.计算本益比和价格
        countPE(yBEpsDataDTO, selectDate);
        //4.输出模板
        outputExcle(yBEpsDataDTO, sigleCode);
        return ResultVO.ok(sigleCode);
    }

    private boolean isNotFourYearKLine(List<KLineEntity> kLineEntity, StockFinDateBean stockFinDateBean) {
        if (CollectionUtils.isEmpty(kLineEntity)){
            return true;
        }
        ProfitDateBean profitDateBean = stockFinDateBean.getProfitDateBean().stream().findFirst().orElse(null);
        if (profitDateBean == null) {
            return true;
        }
        String reportDate = profitDateBean.getReportDate();
        Date fourYearEndDate = DateUtil.getFourYearEndDate(DateUtil.parseDate(reportDate));

        List<KLineEntity> collect = kLineEntity.stream().filter(x ->
                x.getDate().equals(DateUtil.fmtShortDate(fourYearEndDate))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            return true;
        }
        return false;
    }

    private String getExchange(String sigleCode) {
        if (stockCodeYmlBean.getSz().containsKey(sigleCode)) {
            return "0";
        }
        return "1";
    }

    private boolean isFullTime(StockFinDateBean stockFinDateBean) {
        ProfitDateBean profitDateBean = stockFinDateBean.getProfitDateBean().stream().findFirst().orElse(null);
        if (profitDateBean == null) {
            return true;
        }
        String reportDate = profitDateBean.getReportDate();
        Date fourYearEndDate = DateUtil.getFourYearEndDate(DateUtil.parseDate(reportDate));
        List<ProfitDateBean> collect = stockFinDateBean.getProfitDateBean().stream()
                .filter(s -> s.getReportDate().equals(DateUtil.fmtShortDate(fourYearEndDate))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return true;
        }
        return false;
    }

    private boolean hasAllDate(StockFinDateBean stockFinDateBean) {
        if (CollectionUtils.isEmpty(stockFinDateBean.getProfitDateBean())
                || CollectionUtils.isEmpty(stockFinDateBean.getFinanceDataBean())) {
            return true;
        }
        return false;
    }


    private void countPE(YBEpsDataDTO yBEpsDataDTO, String date) {
        //年度计算
        yBEpsDataDTO.setOneyearpedate(DateUtil.fmtShortDate(DateUtil.getOneYearEndTime(DateUtil.parseDate(date))));
        yBEpsDataDTO.setOneyearpehigher(yBEpsDataDTO.getLastoneyearhigher().divide(yBEpsDataDTO.getOneyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setOneyearpeaverage(yBEpsDataDTO.getLastoneyearaverage().divide(yBEpsDataDTO.getOneyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setOneyearpelower(yBEpsDataDTO.getLastoneyearlower().divide(yBEpsDataDTO.getOneyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setTwoyearpedate(DateUtil.fmtShortDate(DateUtil.getTwoYearEndDate(DateUtil.parseDate(date))));
        yBEpsDataDTO.setTwoyearpehigher(yBEpsDataDTO.getLasttwoyearhigher().divide(yBEpsDataDTO.getTwoyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setTwoyearpeaverage(yBEpsDataDTO.getLasttwoyearaverage().divide(yBEpsDataDTO.getTwoyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setTwoyearpelower(yBEpsDataDTO.getLasttwoyearlower().divide(yBEpsDataDTO.getTwoyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setThreeyearpedate(DateUtil.fmtShortDate(DateUtil.getThreeYearEndDate(DateUtil.parseDate(date))));
        yBEpsDataDTO.setThreeyearpehigher(yBEpsDataDTO.getLastthreeyearhigher().divide(yBEpsDataDTO.getThreeyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setThreeyearpeaverage(yBEpsDataDTO.getLastthreeyearaverage().divide(yBEpsDataDTO.getThreeyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setThreeyearpelower(yBEpsDataDTO.getLastthreeyearlower().divide(yBEpsDataDTO.getThreeyearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFouryearpedate(DateUtil.fmtShortDate(DateUtil.getFourYearEndDate(DateUtil.parseDate(date))));
        yBEpsDataDTO.setFouryearpehigher(yBEpsDataDTO.getLastfouryearhigher().divide(yBEpsDataDTO.getFouryearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFouryearpeaverage(yBEpsDataDTO.getLastfouryearaverage().divide(yBEpsDataDTO.getFouryearepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFouryearpelower(yBEpsDataDTO.getLastfouryearlower().divide(yBEpsDataDTO.getFouryearepstotal(), 2, RoundingMode.HALF_UP));

        yBEpsDataDTO.setFouryearavagpehigher(add(yBEpsDataDTO.getOneyearpehigher(), yBEpsDataDTO.getTwoyearpehigher()
                , yBEpsDataDTO.getThreeyearpehigher(), yBEpsDataDTO.getFouryearpehigher()).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFouryearavagpemiddle(add(yBEpsDataDTO.getOneyearpeaverage(), yBEpsDataDTO.getTwoyearpeaverage()
                , yBEpsDataDTO.getThreeyearpeaverage(), yBEpsDataDTO.getFouryearpeaverage()).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFouryearavagpelower(add(yBEpsDataDTO.getOneyearpelower(), yBEpsDataDTO.getTwoyearpelower()
                , yBEpsDataDTO.getThreeyearpelower(), yBEpsDataDTO.getFouryearpelower()).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));

        yBEpsDataDTO.setPricehigher(getPrice(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getFouryearavarageepstotal(), yBEpsDataDTO.getFouryearavagpehigher()));
        yBEpsDataDTO.setPricemiddle(getPrice(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getFouryearavarageepstotal(), yBEpsDataDTO.getFouryearavagpemiddle()));
        yBEpsDataDTO.setPricelower(getPrice(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getFouryearavarageepstotal(), yBEpsDataDTO.getFouryearavagpelower()));

        //季度计算
        yBEpsDataDTO.setOnequarterpedate(DateUtil.fmtShortDate(DateUtil.getOneQuarterEndTime(DateUtil.parseDate(date))));
        yBEpsDataDTO.setOnequarterpehigher(yBEpsDataDTO.getLastonequarterhigher().divide(yBEpsDataDTO.getOneepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setOnequarterpeaverage(yBEpsDataDTO.getLastonequarteraverage().divide(yBEpsDataDTO.getOneepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setOnequarterpelower(yBEpsDataDTO.getLastonequarterlower().divide(yBEpsDataDTO.getOneepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setTwoquarterpedate(DateUtil.fmtShortDate(DateUtil.getTwoQuarterEndTime(DateUtil.parseDate(date))));
        yBEpsDataDTO.setTwoquarterpehigher(yBEpsDataDTO.getLasttwoquarterhigher().divide(yBEpsDataDTO.getTwoepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setTwoquarterpeaverage(yBEpsDataDTO.getLasttwoquarteraverage().divide(yBEpsDataDTO.getTwoepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setTwoquarterpelower(yBEpsDataDTO.getLasttwoquarterlower().divide(yBEpsDataDTO.getTwoepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setThreequarterpedate(DateUtil.fmtShortDate(DateUtil.getThreeQuarterEndTime(DateUtil.parseDate(date))));
        yBEpsDataDTO.setThreequarterpehigher(yBEpsDataDTO.getLastthreequarterhigher().divide(yBEpsDataDTO.getThreeepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setThreequarterpeaverage(yBEpsDataDTO.getLastthreequarteraverage().divide(yBEpsDataDTO.getThreeepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setThreequarterpelower(yBEpsDataDTO.getLastthreequarterlower().divide(yBEpsDataDTO.getThreeepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFourquarterpedate(DateUtil.fmtShortDate(DateUtil.getFourQuarterEndTime(DateUtil.parseDate(date))));
        yBEpsDataDTO.setFourquarterpehigher(yBEpsDataDTO.getLastfourquarterhigher().divide(yBEpsDataDTO.getFourepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFourquarterpeaverage(yBEpsDataDTO.getLastfourquarteraverage().divide(yBEpsDataDTO.getFourepstotal(), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFourquarterpelower(yBEpsDataDTO.getLastfourquarterlower().divide(yBEpsDataDTO.getFourepstotal(), 2, RoundingMode.HALF_UP));

        yBEpsDataDTO.setFourquarteravagpehigher(add(yBEpsDataDTO.getOnequarterpehigher(), yBEpsDataDTO.getTwoquarterpehigher()
                , yBEpsDataDTO.getTwoquarterpehigher(), yBEpsDataDTO.getTwoquarterpehigher()).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFourquarteravagpemiddle(add(yBEpsDataDTO.getOnequarterpeaverage(), yBEpsDataDTO.getTwoquarterpeaverage()
                , yBEpsDataDTO.getTwoquarterpeaverage(), yBEpsDataDTO.getTwoquarterpeaverage()).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));
        yBEpsDataDTO.setFourquarteravagpelower(add(yBEpsDataDTO.getOnequarterpelower(), yBEpsDataDTO.getTwoquarterpelower()
                , yBEpsDataDTO.getTwoquarterpelower(), yBEpsDataDTO.getTwoquarterpelower()).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));

        yBEpsDataDTO.setPricequarterhigher(getPrice(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getFourquarteravarageepstotal(), yBEpsDataDTO.getFourquarteravagpehigher()));
        yBEpsDataDTO.setPricequartermiddle(getPrice(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getFourquarteravarageepstotal(), yBEpsDataDTO.getFourquarteravagpemiddle()));
        yBEpsDataDTO.setPricequarterlower(getPrice(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getFourquarteravarageepstotal(), yBEpsDataDTO.getFourquarteravagpelower()));
    }

    private BigDecimal getPrice(BigDecimal epstotal, BigDecimal avarageepstotal, BigDecimal avagpelower) {
        BigDecimal bigDecimal = epstotal.add(avarageepstotal).divide(new BigDecimal(2)).multiply(avagpelower).setScale(2, RoundingMode.HALF_UP);
        return bigDecimal;
    }

    private void countEPS(String code, YBEpsDataDTO yBEpsDataDTO, Map<String, StockFinDateBean> stockFinDateMap, String date) {
        if (!stockFinDateMap.containsKey(code)) {
            return;
        }
        StockFinDateBean stockFinDateBean = stockFinDateMap.get(code);
        List<Date> quarterList = new ArrayList<Date>();
        //得到季度EPS
        quarterList.add(DateUtil.getOneQuarterEndTime(DateUtil.parseDate(date)));
        quarterList.add(DateUtil.getTwoQuarterEndTime(DateUtil.parseDate(date)));
        quarterList.add(DateUtil.getThreeQuarterEndTime(DateUtil.parseDate(date)));
        quarterList.add(DateUtil.getFourQuarterEndTime(DateUtil.parseDate(date)));
        quarterList.add(DateUtil.getFiveQuarterEndTime(DateUtil.parseDate(date)));
        List<EpsBean> quarterEpsList = getEpsBeans(stockFinDateBean, quarterList);
        if (quarterEpsList == null) return;
        if (quarterEpsList.size() == 5) {
            yBEpsDataDTO.setOnequarterdate(quarterEpsList.get(0).getDate());
            yBEpsDataDTO.setTwoquarterdate(quarterEpsList.get(1).getDate());
            yBEpsDataDTO.setThreequarterdate(quarterEpsList.get(2).getDate());
            yBEpsDataDTO.setFourquarterdate(quarterEpsList.get(3).getDate());
            yBEpsDataDTO.setOneepscurrent(getEps(quarterEpsList, 1));
            yBEpsDataDTO.setTwoepscurrent(getEps(quarterEpsList, 2));
            yBEpsDataDTO.setThreeepscurrent(getEps(quarterEpsList, 3));
            yBEpsDataDTO.setFourepscurrent(getEps(quarterEpsList, 4));
            yBEpsDataDTO.setFourepstotal(getEps(quarterEpsList, 4));
            yBEpsDataDTO.setThreeepstotal(yBEpsDataDTO.getFourepstotal().add(yBEpsDataDTO.getFourepscurrent()));
            yBEpsDataDTO.setTwoepstotal(yBEpsDataDTO.getThreeepstotal().add(yBEpsDataDTO.getTwoepscurrent()));
            yBEpsDataDTO.setOneepstotal(yBEpsDataDTO.getTwoepstotal().add(yBEpsDataDTO.getOneepscurrent()));
            yBEpsDataDTO.setFourquarteravarageepstotal(getQuarterAvarage(yBEpsDataDTO));

        }
        //得到年度EPS
        List<Date> yearList = new ArrayList<Date>();
        yearList.add(DateUtil.getOneYearEndTime(DateUtil.parseDate(date)));
        yearList.add(DateUtil.getTwoYearEndDate(DateUtil.parseDate(date)));
        yearList.add(DateUtil.getThreeYearEndDate(DateUtil.parseDate(date)));
        yearList.add(DateUtil.getFourYearEndDate(DateUtil.parseDate(date)));
        List<EpsBean> yearEpsList = getEpsBeans(stockFinDateBean, yearList);
        if (yearList == null) return;
        if (yearList.size() == 4) {
            yBEpsDataDTO.setOneyeardate(yearEpsList.get(0).getDate());
            yBEpsDataDTO.setTwoyeardate(yearEpsList.get(1).getDate());
            yBEpsDataDTO.setThreeyeardate(yearEpsList.get(2).getDate());
            yBEpsDataDTO.setFouryeardate(yearEpsList.get(3).getDate());
            yBEpsDataDTO.setOneyearepstotal(yearEpsList.get(0).getEps());
            yBEpsDataDTO.setTwoyearepstotal(yearEpsList.get(1).getEps());
            yBEpsDataDTO.setThreeyearepstotal(yearEpsList.get(2).getEps());
            yBEpsDataDTO.setFouryearepstotal(yearEpsList.get(3).getEps());
            yBEpsDataDTO.setFouryearavarageepstotal(getYearAvarage(yBEpsDataDTO));
        }

    }

    private BigDecimal getYearAvarage(YBEpsDataDTO yBEpsDataDTO) {
        BigDecimal add = add(yBEpsDataDTO.getOneyearepstotal(), yBEpsDataDTO.getTwoyearepstotal()
                , yBEpsDataDTO.getThreeyearepstotal(), yBEpsDataDTO.getFouryearepstotal());
        return add.divide(new BigDecimal(4));
    }

    private BigDecimal getQuarterAvarage(YBEpsDataDTO yBEpsDataDTO) {
        BigDecimal add = add(yBEpsDataDTO.getOneepstotal(), yBEpsDataDTO.getTwoepstotal()
                , yBEpsDataDTO.getThreeepstotal(), yBEpsDataDTO.getFourepstotal());
        return add.divide(new BigDecimal(4));
    }

    public static BigDecimal add(BigDecimal... param) {

        BigDecimal sumAdd = BigDecimal.valueOf(0);
        for (int i = 0; i < param.length; i++) {

            BigDecimal bigDecimal = param[i] == null ? new BigDecimal(0) : param[i];
            sumAdd = sumAdd.add(bigDecimal);
        }
        return sumAdd;
    }

    private List<EpsBean> getEpsBeans(StockFinDateBean stockFinDateBean, List<Date> dateList) {
        List<EpsBean> epsList = dateList.stream().map(
                s -> {
                    List<EpsBean> collect = stockFinDateBean.getProfitDateBean().stream().filter(x -> {
                        //过滤需要的日期数据
                        return x.getReportDate().equals(DateUtil.fmtShortDate(s));
                    }).map(x -> {
                        return EpsBean.builder()
                                .date(x.getReportDate())
                                .eps(new BigDecimal(NumberUtils.toDouble(x.getBasicEarningsPerShare())))
                                .build();
                    }).collect(Collectors.toList());
                    return collect.get(0);
                }
        ).collect(Collectors.toList());
        return epsList;
    }

    private BigDecimal getEps(List<EpsBean> epsList, int i) {
        BigDecimal ret = new BigDecimal(0);
        switch (i) {
            case 1:
                ret = getCurrentEps(epsList, 0, 1);
                break;
            case 2:
                ret = getCurrentEps(epsList, 1, 2);
                break;
            case 3:
                ret = getCurrentEps(epsList, 2, 3);
                break;
            case 4:
                ret = getCurrentEps(epsList, 3, 4);
                break;
            default:
        }
        return ret;
    }

    private BigDecimal getCurrentEps(List<EpsBean> epsList, int i, int i1) {
        BigDecimal ret;
        EpsBean epsBean = epsList.get(i);
        if (StringUtils.contains(epsBean.getDate(), "03-31")) {
            //第一季度只要当季的eps
            ret = epsBean.getEps();
        } else {
            //不是第一季需要减去上一季的
            ret = epsBean.getEps().subtract(epsList.get(i1).getEps());
        }
        return ret;
    }

    public void outputExcle(YBEpsDataDTO yBEpsDataDTO, String code) {
        String fileName = String.format(YB_DATA_FILENAME, code + stockCodeYmlBean.getAcode().get(code));
        EasyExcel.write(fileName).withTemplate(YB_TEMPLETE_FILENAME).sheet().doFill(yBEpsDataDTO);

    }

}
