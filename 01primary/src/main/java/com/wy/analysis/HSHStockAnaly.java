package com.wy.analysis;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.wy.bean.EastMoneyBeab;
import com.wy.utils.FilesUtil;
import com.wy.utils.easyexcle.ReadMutilFile;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwang on 2021/10/19 11:24
 * 沪深港通持股分析
 */
public class HSHStockAnaly {
    public static void main(String[] args) {
        //读出所有文件路径
        List<EastMoneyBeab.ResultDTO.DataDTO> dataDTOList= ReadMutilFile.getDataDTOS(null);
        for (EastMoneyBeab.ResultDTO.DataDTO dataDTO : dataDTOList) {
            System.out.println(dataDTO.getSecurityName());
        }
        System.out.println(dataDTOList.size());
    }

}
