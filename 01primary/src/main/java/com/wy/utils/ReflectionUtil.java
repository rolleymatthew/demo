package com.wy.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author yunwang
 * @Date 2021-11-04
 */
public class ReflectionUtil {

    /**
     * 通过type获取className
     */

    public static String getClassName(Type type){
        if(type==null){

            return "";
        }
        String className = type.toString();

        if (className.startsWith("class")){
            className=className.substring("class".length());
        }
        return className;
    }

    /**
     * 获取子类在父类传入的泛型Class类型
     * 获取泛型对象的泛型化参数
     * @param o
     * @return
     */
    public static Type getParameterizedTypes(Object o){
        Type superclass = o.getClass().getGenericSuperclass();
        if(!ParameterizedType.class.isAssignableFrom(superclass.getClass())) {
            return null;
        }
        Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
        return types[0];
    }

    /**
     * 获取实现类的泛型参数
     * @param o
     * @return
     */

    public static Type getInterfaceTypes(Object o){
        Type[] genericInterfaces = o.getClass().getGenericInterfaces();

        return genericInterfaces[0];
    }

    /**
     *检查对象是否存在默认构造函数
     */

    public static boolean hasDefaultConstructor(Class<?> clazz) throws SecurityException {
        Class<?>[] empty = {};
        try {
            clazz.getConstructor(empty);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }
    /**
     * 通过Type创建对象
     */
    public static Object newInstance(Type type)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = getClass(type);
        if (clazz==null) {
            return null;
        }
        return clazz.newInstance();
    }


    /**
     * 通过Type获取对象class
     * @param type
     * @return
     * @throws ClassNotFoundException
     */

    public static Class<?> getClass(Type type)
            throws ClassNotFoundException {
        String className = getClassName(type);
        if (className==null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }


}