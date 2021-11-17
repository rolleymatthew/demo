package com.wy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.*;
import com.wy.service.StockService;
import com.wy.stock.etf.ETFFundDataService;
import com.wy.stock.etf.ETFFundReportService;
import com.wy.stock.finance.*;
import com.wy.stock.hszh.GetSHSZHKStockDateService;
import com.wy.stock.hszh.HSHStockReportService;
import com.wy.utils.FilesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yunwang on 2021/11/2 10:11
 */
@Service
public class StockServiceImpl implements StockService {

    private Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    private static AtomicInteger flag = new AtomicInteger(0);

    @Autowired
    StockCodeYmlBean stockCodeYmlBean;

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
            int[] days = {2, 3, 4, 5, 6, 7, 8, 9, 10};
            ETFFundReportService.analyseETF(days);

            int[] countUpZeroDays = {2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 30, 50};
            int[] ampTopDays = {3, 5, 10, 20, 30, 50};
            HSHStockReportService.getETFReport(countUpZeroDays, ampTopDays);
            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO FinanceDateByMonth(String code) {
        //获取所有公司代码
        if (flag.incrementAndGet() == 1) {
            List<String> allCodes = new ArrayList<String>();
            if (StringUtils.isEmpty(code)) {
                allCodes = stockCodeYmlBean.getAcode().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
            } else if (StringUtils.indexOf(code, ",") > -1) {
                allCodes = Stream.of(code).map(f -> f.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
            } else {
                allCodes.add(code);
            }

            logger.info("StockCode count: {}", allCodes.size());
            //获取财务数据
            allCodes.parallelStream().forEach(
                    x -> {
                        try {
                            logger.info("stock code : " + x);
                            FinanceBalanceDateService.getBeansByCode(x);
                            FinanceCashFlowDateService.getBeansByCode(x);
                            FinanceProfitDateService.getBeansByCode(x);
                            FinanceDateWriteService.getBeansByCode(x);
                        } catch (Exception e) {
                            logger.error("stock {} error : {}", x, e.toString());
                        }
                    }
            );
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
            logger.info("StockCode count: {}", allCodes.size());
            //获取财务数据
            allCodes.parallelStream().forEach(
                    x -> {
                        ExcelWriter excelWriter = null;
                        try {
                            logger.info("stock code : " + x);
                            String fileName = path + String.format(FinanceCommonService.FILE_NAME_ALL, x);
                            excelWriter = EasyExcel.write(fileName).build();
                            WriteSheet writeSheet = EasyExcel.writerSheet(0, "资产负债表").head(BalanceDateBean.class).build();
                            excelWriter.write(FinanceBalanceDateService.getBalanceDateBeanList(x), writeSheet);
                            WriteSheet writeSheet1 = EasyExcel.writerSheet(1, "现金流量表").head(CashFlowBean.class).build();
                            excelWriter.write(FinanceCashFlowDateService.getCashFlowBeanList(x), writeSheet1);
                            WriteSheet writeSheet2 = EasyExcel.writerSheet(2, "利润表").head(ProfitDateBean.class).build();
                            excelWriter.write(FinanceProfitDateService.getProfitDateBeanList(x), writeSheet2);
                            WriteSheet writeSheet3 = EasyExcel.writerSheet(3, "主要财务数据").head(FinanceDataBean.class).build();
                            excelWriter.write(FinanceDateWriteService.getFinanceDataBeanList(x), writeSheet3);
                        } catch (Exception e) {
                            logger.error("stock {} error : {}", x, e.toString());
                        } finally {
                            // 千万别忘记finish 会帮忙关闭流
                            if (excelWriter != null) {
                                excelWriter.finish();
                            }
                        }
                    }
            );

            flag.decrementAndGet();
        } else {
            return ResultVO.build(-1, "已经在运行抓取");
        }
        return ResultVO.ok();
    }

    @Override
    public ResultVO FinanceDateReport(String code) {
        int[] counts = {1, 2, 3};
        List<String> allCodes = new ArrayList<>();
        if (StringUtils.isEmpty(code)) {
            allCodes = stockCodeYmlBean.getAcode().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        } else if (StringUtils.isNotEmpty(code) && StringUtils.contains(code, ",")) {
            allCodes = Stream.of(code).map(l -> l.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
        } else {
            allCodes.add(code);
        }
        logger.info("start report {} finance .", allCodes.size());
        long start = System.currentTimeMillis();
        //读取文件三大报表
        Map<String, List<ProfitDateBean>> profitListMap = FinanceCommonService.getProfitListMap(allCodes);
        Map<String, List<BalanceDateBean>> balanceListMap = FinanceCommonService.getBalanceListMap(allCodes)
                .entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));
        Map<String, List<CashFlowBean>> cashListMap = FinanceCommonService.getCashListMap(allCodes)
                .entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));

        //计算毛利率、营业利润率、净利率
        Map<String, List<FinThreePerBean>> finPerMap = ProfitReportService.getFinPerMap(profitListMap);

        //填充三率每个报告期增加减少
        Map<String, List<FinThreePerBean>> threePerMap = ProfitReportService.fillFinPerMap(finPerMap);
        //找到三率三升的并输出文件
        ProfitReportService.outputUpFinThreePer(counts, threePerMap, stockCodeYmlBean.getAcode());

        //计算营收和利润比例
        List<OperatProfitBean> operatProfitBeans = ProfitReportService.getOperatProfitBeans(profitListMap, stockCodeYmlBean.getAcode());

        ProfitReportService.outputOpeProfitPer(operatProfitBeans);

        Map<String, List<ZQHFinBean>> zqhBeanMap = ProfitReportService.getZqhBeanMap(profitListMap, balanceListMap, cashListMap);

        //输出文件
        ProfitReportService.outPutZQHFile(zqhBeanMap, stockCodeYmlBean.getAcode());
        logger.info("end finance report {}. {}s", allCodes.size(), (System.currentTimeMillis() - start) / 1000);
        return ResultVO.ok();
    }

}
