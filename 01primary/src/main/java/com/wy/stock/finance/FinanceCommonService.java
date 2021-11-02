package com.wy.stock.finance;

import com.wy.bean.ConstantBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.ClassUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yunwang
 * @Date 2021-11-03
 */
public class FinanceCommonService {
    public static String[][] getFinanceDataBeans(String temp) {
        //得到原始的行数据,计算出行数
        String[] line = temp.split("\r\n");
        List<String> collect = Arrays.stream(line).filter(x -> x.length() > 10).collect(Collectors.toList());
        int lineLen = collect.size();

        //得到第一个行数据计算出列数
        String s1 = collect.get(0);
        String[] cell = StringUtils.split(s1, ",");
        int columnLen = cell.length;

        //定义两个做行转列用的二维数组
        String[][] orgData = new String[lineLen][columnLen];
        String[][] newData = new String[columnLen][lineLen];

        //把数据赋值到一个二维数组orgData里,header放表头
        List<String> header = new ArrayList<>();
        fillString(collect, lineLen, columnLen, orgData, header);

        //用第二个二维数组newData实现行转列
        lineToColumn(lineLen, columnLen, orgData, newData);

        return newData;
    }

    public static void lineToColumn(int lineLen, int columnLen, String[][] orgData, String[][] newData) {
        //行转列
        for (int i = 0; i < lineLen; i++) {
            for (int j = 0; j < columnLen; j++) {
                newData[j][i] = orgData[i][j];
            }

        }
    }

    public static void fillString(List<String> collect, int lineLen, int columnLen, String[][] orgData, List<String> header) {
        //二维数组赋值
        for (int i = 0; i < lineLen; i++) {
            String s = collect.get(i);
            String[] split = s.split(",");
            for (int j = 0; j < columnLen; j++) {
                String s2 = split[j];
                if (j == 0) {
                    header.add(s2);
                } else {
                    orgData[i][j - 1] = s2;
                }
            }
        }
    }



}
