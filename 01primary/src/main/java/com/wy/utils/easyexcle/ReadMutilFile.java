package com.wy.utils.easyexcle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.EastMoneyBeab;
import com.wy.utils.FilesUtil;
import com.wy.utils.SHSZHKStock;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yunwang on 2021/10/19 16:35
 */
public class ReadMutilFile {
    public static void main(String[] args) {
        //读出所有文件路径
//        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList = getDataDTOS(null,5,0);
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList = getDataDTOS("300750",-1,0);
        System.out.println(dataDTOList.size());
    }

    /**
     * 获取原始数据
     * @param code  单个代码
     * @param daySize   -1所有数据，大于等于0获取的天数
     * @param sheetNum  读取的数据来源sheet，一共有三个sheet
     * @return  所有需要的原始数据列表
     */
    public static List<EastMoneyBeab.ResultDTO.DataDTO> getDataDTOS(String code,int daySize,int sheetNum) {
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList = new ArrayList<>();
        String dir = SHSZHKStock.PATH;
        List<String> filesOfDictory = FilesUtil.getFilesOfDicByExt(dir, SHSZHKStock.FILE_PRE, SHSZHKStock.FILE_EXT);
        for (String s : filesOfDictory) {
            if (daySize==0){
                break;
            }
            String dirFile = dir + File.separator + s.trim();
            dataDTOList.addAll(printDate(dirFile, code, sheetNum));
            daySize--;
        }
        return dataDTOList;
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> printDate(String fileName, String code, int sheetNum) {
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList = new ArrayList<>();
        EasyExcel.read(fileName, EastMoneyBeab.ResultDTO.DataDTO.class, new PageReadListener<EastMoneyBeab.ResultDTO.DataDTO>(dataList -> {
            for (EastMoneyBeab.ResultDTO.DataDTO dataDTO : dataList) {
                if (StringUtils.isNotEmpty(code) && StringUtils.equals(dataDTO.getSecurityCode(), code)) {
                    dataDTOList.add(dataDTO);
                }
                if (StringUtils.isEmpty(code)) {
                    dataDTOList.add(dataDTO);
                }
            }
        })).sheet(sheetNum).doRead();
        return dataDTOList;
    }
}
