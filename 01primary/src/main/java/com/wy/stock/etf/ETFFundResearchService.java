package com.wy.stock.etf;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.Contant;
import com.wy.bean.ETFBean;
import com.wy.bean.EastMoneyBeab;
import com.wy.stock.hszh.GetSHSZHKStockDateService;
import com.wy.utils.FilesUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yunwang on 2021/10/29 13:53
 */
public class ETFFundResearchService {
    public static void main(String[] args) {
        List<ETFBean.PageHelpDTO.DataDTO> allFundData = getAllFundData();

        //分拆日期分组,规模倒序
        TreeMap<String, List<ETFBean.PageHelpDTO.DataDTO>> collectByDate = allFundData.stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getTotVol).reversed())
                .collect(Collectors.groupingBy(x -> x.getStatDate(), () -> new TreeMap<>(new ComparatorDate()), Collectors.toList()));
        //按照代码分组,时间倒序
        Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode = allFundData.stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getStatDate).reversed())
                .collect(Collectors.groupingBy(ETFBean.PageHelpDTO.DataDTO::getSecCode));

        //连续2日新增的
        Set<Map.Entry<Integer, List<ETFBean.PageHelpDTO.DataDTO>>> collect = collectByCode.entrySet().stream()
                .filter(x -> {
                    //过滤出连续新增的
                    long count = x.getValue().stream().limit(2).filter(s -> s.getAddVol() > 0).count();
                    if (count == 2) {
                        return true;
                    } else {
                        return false;
                    }
                }
        ).collect(Collectors.toSet());
        collectByCode.entrySet().stream().forEach(x -> {
                    //截断流到想要的天数
                    Stream<ETFBean.PageHelpDTO.DataDTO> dataDTOStream = x.getValue().stream().limit(2);
                    //计算连续增加的数量
                    long count = dataDTOStream.filter(s -> s.getAddVol() > 0).count();
                    System.out.println(count);
                    if (count == 2) {
                        //计算所有变动数据
                        System.out.println(x.getKey());
                        Double addTot = dataDTOStream.collect(Collectors.summingDouble(ETFBean.PageHelpDTO.DataDTO::getAddVol));
                        System.out.println(addTot);
//                        Optional<ETFBean.PageHelpDTO.DataDTO> first = dataDTOStream.findFirst();
//                        if (first.isPresent()){
//                            ETFBean.PageHelpDTO.DataDTO dataDTO = first.get();
//                            System.out.println(dataDTO.getStatDate());
//                        }
//                        ETFBean.PageHelpDTO.DataDTO dataDTOLast = dataDTOStream.skip(2).findFirst().orElse(null);
//                        System.out.println(dataDTOLast.getStatDate());

                    }

                }
        );
    }

    private static List<ETFBean.PageHelpDTO.DataDTO> getAllFundData() {
        String dir = Contant.DIR + File.separator + ETFFundDataService.FILE_PRE + File.separator;
        List<String> filesOfDictory = FilesUtil.getFilesOfDicByExt(dir, ETFFundDataService.FILE_PRE, ETFFundDataService.FILE_EXT);
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
