package com.wy.stock.finance;

import com.wy.bean.ConstantBean;
import com.wy.bean.FinanceDataBean;
import com.wy.utils.ClassUtil;
import org.apache.commons.collections.CollectionUtils;
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

    public static String[][] getArrayDates(String temp) {
        List<String> collect = getStringList(temp);
        if (CollectionUtils.isEmpty(collect)) return null;

        //定义两个做行转列用的二维数组,把数据赋值到一个二维数组orgData里,用第二个二维数组newData实现行转列
        String[][] orgData = fillStringArray(collect);
        String[][] newData = lineToColumnArray(orgData, collect);

        return newData;
    }

    /**
     * 行数据
     *
     * @param temp
     * @return
     */
    public static List<String> getStringList(String temp) {
        String[] line = temp.split("\r\n");
        List<String> collect = Arrays.stream(line).filter(x -> x.length() > 10).collect(Collectors.toList());
        return collect;
    }

    /**
     * 行转列
     *
     * @param orgData
     * @param collect
     * @return
     */
    private static String[][] lineToColumnArray(String[][] orgData, List<String> collect) {
        int lineLen = getHeader(collect).size();
        int columnLen = getColumnLen(collect);
        String[][] newData = new String[columnLen][lineLen];
        for (int i = 0; i < lineLen; i++) {
            for (int j = 0; j < columnLen; j++) {
                newData[j][i] = orgData[i][j];
            }
        }
        return newData;
    }

    public static List<String> getHeader(List<String> collect) {
        List<String> header = collect.stream()
                .filter(x -> StringUtils.indexOf(x, ",") > 0)
                .map(s -> StringUtils.substring(s, 0, StringUtils.indexOf(s, ",")))
                .collect(Collectors.toList());
        return header;
    }

    private static String[][] fillStringArray(List<String> collect) {
        //二维数组赋值
        int columnLen = getColumnLen(collect);
        int lineLen = collect.size();
        String[][] orgData = new String[lineLen][columnLen];
        for (int i = 0; i < lineLen; i++) {
            String s = collect.get(i);
            String[] split = s.split(",");
            for (int j = 1; j < columnLen; j++) {
                String s2 = split[j];
                orgData[i][j - 1] = s2;

            }
        }
        return orgData;
    }

    public static int getColumnLen(List<String> collect) {
        String s1 = collect.get(0);
        String[] cell = StringUtils.split(s1, ",");
        int columnLen = cell.length;
        return columnLen;
    }


}
