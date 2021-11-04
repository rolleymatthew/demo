package com.wy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by yunwang on 2021/11/4 9:22
 */
public class Stream {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("zhong");
        list.add("ming");
        list.add("mao");
        List<String[]> collect1 = list.stream().map(line -> line.split(""))
                .collect(toList());
        for (String[] strings : collect1) {
            for (String string : strings) {
                System.out.println(string);
            }
        }
        List<String> collect = list.stream().map(line -> line.split(""))
                .flatMap(Arrays::stream).distinct().collect(toList());
        System.out.println(collect.toString());
    }
}
