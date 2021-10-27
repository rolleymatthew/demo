package com.wy;

import com.wy.stock.finance.FinanceDataBean;
import com.wy.utils.ClassUtil;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by yunwang on 2021/10/27 10:58
 */
public class ClassTest {
    public static void main(String[] args) {
        Class<FinanceDataBean> financeDataBeanClass = FinanceDataBean.class;
//        System.out.println(financeDataBeanClass.getName());
        Field[] declaredFields = financeDataBeanClass.getDeclaredFields();
        Arrays.stream(declaredFields).forEach(x-> System.out.println(x.getName()));
        Method[] declaredMethods = financeDataBeanClass.getDeclaredMethods();
//        Arrays.stream(declaredMethods).forEach(x-> System.out.println(x.getName()));

        String[] filedName = ClassUtil.getFiledName(financeDataBeanClass);
        Arrays.stream(filedName).forEach(s-> System.out.println(s));

        FinanceDataBean financeDataBean=new FinanceDataBean();
        ClassUtil.setFieldValueByFieldName(financeDataBean,"basePerShare","10.2");
        System.out.println(financeDataBean.getBasePerShare());

    }
}
