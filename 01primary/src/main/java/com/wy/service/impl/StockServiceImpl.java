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
import com.wy.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
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
        //读取文件
        Map<String, List<ProfitDateBean>> dataMap = FinanceCommonService.getFinanceListMap(allCodes);
        //计算毛利率、营业利润率、净利率
        Map<String, List<FinThreePerBean>> finPerMap = ProfitReportService.getFinPerMap(dataMap);

        //填充三率每个报告期增加减少
        Map<String, List<FinThreePerBean>> threePerMap = ProfitReportService.fillFinPerMap(finPerMap);
        //找到三率三升的并输出文件
        ProfitReportService.outputUpFinThreePer(counts, threePerMap, stockCodeYmlBean.getAcode());

        //利润环比上升比例>营收环比上升比例
        List<OperatProfitBean> operatProfitBeans = ProfitReportService.getOperatProfitBeans(dataMap, stockCodeYmlBean.getAcode());

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(FinanceCommonService.PATH_REPORT + File.separator
                    + String.format(FinanceCommonService.FILE_NAME_PER, DateUtil.getCurrentDay()), OperatProfitBean.class).build();
            int i = 0;
            WriteSheet writeSheet = EasyExcel.writerSheet(i, "所有").build();
            excelWriter.write(operatProfitBeans.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            List<OperatProfitBean> collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitSame() > 0 && s.getAddNetProfitComp() > 0).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润同比环比增加").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() > s.getAddNetProfitSame()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润环比大于同比").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() > s.getAddOperatingIncomeComp()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润增速大于营收增速").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
            collect = operatProfitBeans.stream().filter(s -> s.getAddNetProfitComp() >= 30 && s.getAddNetProfitSame() >= 20).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                i++;
                writeSheet = EasyExcel.writerSheet(i, "利润同比20以上,环比30以上").build();
                excelWriter.write(collect.stream().sorted(Comparator.comparing(OperatProfitBean::getAddNetProfitComp).reversed()).collect(Collectors.toList()), writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        //报告期同比上升，环比也上升
        logger.info("end finance report {}. {}s", allCodes.size(), (System.currentTimeMillis() - start) / 1000);
        return ResultVO.ok();
    }
}
