package com.wy.stock.etf;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wy.bean.Contant;
import com.wy.bean.ETFBean;
import com.wy.bean.ETFCompVoBean;
import com.wy.utils.DateUtil;
import com.wy.utils.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/29 13:53
 */
@Slf4j
public class ETFFundReportService {
    private static String PATH = Contant.REPORT_PATH;

    public static void main(String[] args) {
        int[] days = {2, 3, 4, 5, 6, 7, 8, 9, 10};

        analyseETF(days);

    }

    public static void analyseETF(int[] days) {
        List<ETFBean.PageHelpDTO.DataDTO> allFundData = getAllFundData();

        getETFFundTopByDay(days, allFundData);
        //分拆日期分组,规模倒序
        TreeMap<String, List<ETFBean.PageHelpDTO.DataDTO>> collectByDate = allFundData.stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getTotVol).reversed())
                .collect(Collectors.groupingBy(x -> x.getStatDate(), () -> new TreeMap<>(new ComparatorDate()), Collectors.toList()));
    }

    /**
     * 计算ETF每天的规模增减变化排名
     *
     * @param days
     * @param allFundData
     */
    private static void getETFFundTopByDay(int[] days, List<ETFBean.PageHelpDTO.DataDTO> allFundData) {
        //按照代码分组,时间倒序
        Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode = allFundData.stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getStatDate).reversed())
                .collect(Collectors.groupingBy(ETFBean.PageHelpDTO.DataDTO::getSecCode));

        ExcelWriter excelWriter = null;
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(PATH + "ETFReport" + DateUtil.getCurrentDay() + ".xlsx", ETFCompVoBean.class).build();
            //连续多日日新增的
            int i = 0;
            for (int day : days) {
                //连日增数据
                WriteSheet writeSheet = null;
                List<ETFCompVoBean> addETFList = addEtfDates(collectByCode, day);
                if (!CollectionUtils.isEmpty(addETFList)) {
                    i++;
                    writeSheet = EasyExcel.writerSheet(i, day + "日增").build();
                    excelWriter.write(addETFList, writeSheet);
                }

                //连日减数据
                List<ETFCompVoBean> reducETFList = reduceEtfDates(collectByCode, day);
                if (!CollectionUtils.isEmpty(reducETFList)) {
                    i++;
                    writeSheet = EasyExcel.writerSheet(i, day + "日减").build();
                    excelWriter.write(reducETFList, writeSheet);
                }
            }

        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 计算连减数据
     *
     * @param collectByCode
     * @param day
     * @return
     */
    private static List<ETFCompVoBean> reduceEtfDates(Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode, int day) {
        Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> etfDateByDownZero = getETFDateByDownZero(collectByCode, day);
        //计算增加变化
        Map<Integer, Double> reduceMap = etfDateByDownZero.entrySet().stream().collect(Collectors.toMap(x -> x.getKey()
                , x -> x.getValue().stream().collect(Collectors.summingDouble(ETFBean.PageHelpDTO.DataDTO::getAddVol))));

        //拼装返回
        List<ETFCompVoBean> reducETFList = getEtfCompVoBeans(etfDateByDownZero, reduceMap);
        return reducETFList.stream().sorted(Comparator.comparing(ETFCompVoBean::getAddTotValue)).collect(Collectors.toList());
    }

    /**
     * 计算连加数据
     *
     * @param collectByCode
     * @param day
     * @return
     */
    private static List<ETFCompVoBean> addEtfDates(Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode, int day) {
        Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> etfDateByUpZero = getETFDateByUpZero(collectByCode, day);
        //计算减少变化数量
        Map<Integer, Double> addValueMap = etfDateByUpZero.entrySet().stream().collect(Collectors.toMap(x -> x.getKey()
                , x -> x.getValue().stream().collect(Collectors.summingDouble(ETFBean.PageHelpDTO.DataDTO::getAddVol))));
        //拼装返回
        List<ETFCompVoBean> addETFList = getEtfCompVoBeans(etfDateByUpZero, addValueMap);
        return addETFList.stream().sorted(Comparator.comparing(ETFCompVoBean::getAddTotValue).reversed()).collect(Collectors.toList());
    }

    private static List<ETFCompVoBean> getEtfCompVoBeans(Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> etfDateByDownZero, Map<Integer, Double> reduceMap) {
        List<ETFCompVoBean> collect = etfDateByDownZero.entrySet().stream()
                .map(x -> {
                    ETFCompVoBean etfCompVoBean = new ETFCompVoBean();
                    BeanUtils.copyProperties(x.getValue().get(0), etfCompVoBean);
                    //期间总变化
                    etfCompVoBean.setAddTotValue(reduceMap.get(x.getKey()));
                    ETFBean.PageHelpDTO.DataDTO dataDTO = x.getValue().get(x.getValue().size() - 1);
                    etfCompVoBean.setBeforeDate(dataDTO.getStatDate());
                    etfCompVoBean.setBeforeValue(dataDTO.getTotVol());
                    return etfCompVoBean;
                }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 连续增加Map
     *
     * @param collectByCode
     * @param limit
     * @return
     */
    private static Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> getETFDateByUpZero(Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode, int limit) {
        return collectByCode.entrySet().stream()
                .filter(x -> {
                            //过滤出连续新增的
                            List<ETFBean.PageHelpDTO.DataDTO> collect = x.getValue().stream().limit(limit)
                                    .filter(s -> s.getAddVol() > 0).collect(Collectors.toList());
                            long count = Optional.ofNullable(collect).orElse(new ArrayList<>()).stream().count();
                            if (count == limit) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        //截断数据到需要的长度
                ).collect(Collectors.toMap(x -> x.getKey()
                        , x -> x.getValue().stream().limit(limit).collect(Collectors.toList())));
    }

    /**
     * 连续减少Map
     *
     * @param collectByCode
     * @param limit
     * @return
     */
    private static Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> getETFDateByDownZero(Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode, int limit) {
        return collectByCode.entrySet().stream()
                .filter(x -> {
                            //过滤出连续新增的
                            long count = x.getValue().stream().limit(limit)
                                    .filter(s -> s.getAddVol() < 0).count();
                            if (count == limit) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        //截断数据到需要的长度
                ).collect(Collectors.toMap(x -> x.getKey()
                        , x -> x.getValue().stream().limit(limit).collect(Collectors.toList())));
    }

    private static List<ETFBean.PageHelpDTO.DataDTO> getAllFundData() {
        String dir = Contant.DIR + File.separator + ETFFundDataService.FILE_PRE + File.separator;
        List<String> filesOfDictory = FilesUtil.getFilesOfDicByExt(dir, ETFFundDataService.FILE_PRE, Contant.FILE_EXT);
        List<ETFBean.PageHelpDTO.DataDTO> allFundDataList = new ArrayList<>();
        filesOfDictory.stream().forEach(x -> {
            String fileName = dir + x;
            EasyExcel.read(fileName, ETFBean.PageHelpDTO.DataDTO.class
                    , new PageReadListener<ETFBean.PageHelpDTO.DataDTO>(dataList -> {
                        allFundDataList.addAll(dataList);
                    })).sheet(0).doRead();
        });
        return allFundDataList;
    }

    private static class ComparatorDate implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareToIgnoreCase(o1);
        }
    }
}
