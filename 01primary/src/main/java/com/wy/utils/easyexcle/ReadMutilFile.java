package com.wy.utils.easyexcle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.EastMoneyBeab;
import com.wy.utils.FilesUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yunwang on 2021/10/19 16:35
 */
public class ReadMutilFile {
    public static void main(String[] args) {
        //读出所有文件路径
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList=new ArrayList<>();
        String dir="D:\\HSHSTOCK";
        List<String> filesOfDictory = FilesUtil.getFilesOfDicByExt(dir,".xlsx");
        for (String s : filesOfDictory) {
            String dirFile=dir+"\\"+s.trim();
//            dataDTOList.addAll(printDate(dirFile, "300750"));
            dataDTOList.addAll(printDate(dirFile, null));
        }
        System.out.println(dataDTOList.size());
    }

    private static List<EastMoneyBeab.ResultDTO.DataDTO> printDate(String fileName,String code) {
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList=new ArrayList<>();
        EasyExcel.read(fileName, EastMoneyBeab.ResultDTO.DataDTO.class, new PageReadListener<EastMoneyBeab.ResultDTO.DataDTO>(dataList -> {
            for (EastMoneyBeab.ResultDTO.DataDTO dataDTO : dataList) {
                if (StringUtils.isNotEmpty(code)&&StringUtils.equals(dataDTO.getSecurityCode(), code)) {
                    dataDTOList.add(dataDTO);
                }
                if (StringUtils.isEmpty(code)) {
                    dataDTOList.add(dataDTO);
                }
            }
        })).sheet().doRead();
        return dataDTOList;
    }
}
