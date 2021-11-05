package com.wy;

import com.wy.bean.ConstMapBean;
import com.wy.bean.ConstantBean;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yunwang on 2021/11/5 17:02
 */
public class ConstMap {
    public static void main(String[] args) {
        String balance = ConstantBean.balance;
        Map<String, String> collect = Stream.of(balance).map(c -> c.split(","))
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(x -> StringUtils.substring(x, 0, StringUtils.indexOf(x, "|"))
                        , x -> StringUtils.substring(x, StringUtils.indexOf(x, "|") + 1)));

//        Map<String, String> collect1 = collect.stream().collect(Collectors.toMap(x -> {
//            return StringUtils.substring(x.toString(), 0, StringUtils.indexOf(x.toString(), "|"));
//        }, x -> StringUtils.substring(x.toString(), StringUtils.indexOf(x.toString(), "|") + 1)));

        String a="asfsd|asdfds";
        System.out.println(StringUtils.substring(a,0,StringUtils.indexOf(a,"|")));
        System.out.println(StringUtils.substring(a,StringUtils.indexOf(a,"|")+1));
    }
}
