package com.wy.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;

/**
 * @author yunwang
 * @Date 2021-10-30
 */
public class NumUtils {
    public static void main(String[] args) {
//        String num = "34";
//        System.out.println(stringToDouble(num));
        double d=1252.2563;
        Double st = roundDouble(d);
        System.out.println(st);
    }

    public static Double roundDouble(double d) {
        DecimalFormat df=new DecimalFormat(".##");
        Double st=Double.parseDouble(df.format(d));
        return st;
    }

    public static Double stringToDouble(String num) {
        double v = NumberUtils.toDouble(num, 0);
        return v;
    }
}
