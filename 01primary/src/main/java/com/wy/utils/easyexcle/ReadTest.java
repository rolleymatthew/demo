package com.wy.utils.easyexcle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.wy.bean.EastMoneyBeab;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by yunwang on 2021/10/19 10:44
 */
public class ReadTest {
    public static void main(String[] args) {
        String fileName = "D:\\HSHSTOCK" + File.separator + "HSHStock2021-10-18.xlsx";
        for (int i = 0; i < 3; i++) {
            EasyExcel.read(fileName, EastMoneyBeab.ResultDTO.DataDTO.class, new PageReadListener<EastMoneyBeab.ResultDTO.DataDTO>(dataList -> {
                for (EastMoneyBeab.ResultDTO.DataDTO dataDTO : dataList) {
                    if (StringUtils.equals(dataDTO.getSecurityCode(), "300750")) {
                        System.out.println(dataDTO.getSecurityName() + dataDTO.getSecurityCode());
                    }
                }
            })).sheet().doRead();
        }
     }
}
