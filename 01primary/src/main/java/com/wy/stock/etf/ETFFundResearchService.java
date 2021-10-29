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

/**
 * Created by yunwang on 2021/10/29 13:53
 */
public class ETFFundResearchService {
    public static void main(String[] args) {
        List<ETFBean.PageHelpDTO.DataDTO> allFundData = getAllFundData();

        //分拆日期分组,规模倒序
        Map<String, List<ETFBean.PageHelpDTO.DataDTO>> collectByDate = allFundData.stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getTotVol).reversed())
                .collect(Collectors.groupingBy(x->x.getStatDate(),()->new TreeMap<>(new ComparatorDate()),Collectors.toList()));
        //按照代码分组,时间倒序
        Map<Integer, List<ETFBean.PageHelpDTO.DataDTO>> collectByCode = allFundData.stream().sorted(Comparator.comparing(ETFBean.PageHelpDTO.DataDTO::getStatDate).reversed())
                .collect(Collectors.groupingBy(ETFBean.PageHelpDTO.DataDTO::getSecCode));

        System.out.println(collectByDate.size());
        System.out.println(collectByCode.size());
    }

    private static List<ETFBean.PageHelpDTO.DataDTO> getAllFundData() {
        String dir = Contant.DIR + File.separator + ETFFundDataService.FILE_PRE + File.separator;
        List<String> filesOfDictory = FilesUtil.getFilesOfDicByExt(dir, ETFFundDataService.FILE_PRE, ETFFundDataService.FILE_EXT);
        List<ETFBean.PageHelpDTO.DataDTO> allFundDataList=new ArrayList<>();
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
