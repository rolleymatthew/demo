package com.wy.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwang on 2021/10/20 17:37
 */
public class SortByUp {
    public static void main(String[] args) {
        List<Integer> ss= new ArrayList<>() ;
        ss.add(1);
        ss.add(2);
        ss.add(3);
        ss.add(0);
        ss.add(-1);

        for (int i = 0; i < ss.size(); i++) {
            Integer integer = ss.get(i);
            if (integer.intValue()<0){
                continue;
            }
        }
    }
}
