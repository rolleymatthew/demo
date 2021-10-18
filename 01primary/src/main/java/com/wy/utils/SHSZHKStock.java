package com.wy.utils;

import com.wy.bean.EastMoneyBeab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwang on 2021/10/18 15:44
 */
public class SHSZHKStock {

    public static void main(String[] args) {
        String ss="";
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSHDTOS = SHHStockConnect.getDataDTOS(ss);
        List<EastMoneyBeab.ResultDTO.DataDTO> dataSZDTOS = SZHStockConnect.getDataDTOS();
        List<EastMoneyBeab.ResultDTO.DataDTO> AllStock=new ArrayList<>();
        AllStock.addAll(dataSHDTOS);
        AllStock.addAll(dataSZDTOS);
        for (EastMoneyBeab.ResultDTO.DataDTO dataDTO : AllStock) {
            System.out.println(dataDTO.getSecurityName());
        }
        System.out.println(AllStock.size());
    }
}
