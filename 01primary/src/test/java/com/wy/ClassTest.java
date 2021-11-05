package com.wy;

import com.wy.bean.FinanceDataBean;
import com.wy.utils.ClassUtil;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by yunwang on 2021/10/27 10:58
 */
public class ClassTest {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("com.wy.bean.FinanceDataBean");
        Object o = aClass.newInstance();
        System.out.println(o);
    }
}
