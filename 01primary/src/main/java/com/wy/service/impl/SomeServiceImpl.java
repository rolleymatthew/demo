package com.wy.service.impl;

import com.wy.bean.Stock;
import com.wy.bean.Student;
import com.wy.service.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yunwang on 2021/5/19 16:53
 */
@Service
public class SomeServiceImpl implements SomeService {
    @Override
    public String hello() {
//        int i=3/0;
        return "hello service";
    }

    @Autowired
    private Stock stock;
    @Override
    public String getCode() {
        String ret=stock.getShCode();
        return ret;
    }
}
