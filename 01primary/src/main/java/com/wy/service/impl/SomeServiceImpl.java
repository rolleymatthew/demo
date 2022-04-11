package com.wy.service.impl;

import com.wy.bean.StockCodeBean;
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
        return "[{\"ip\":\"127.0.0.1\",\"master\":1,\"persistence\":0,\"port\":6379}]";
    }

    @Autowired
    private StockCodeBean stock;
    @Override
    public String getCode() {
        String ret=stock.getShCode();
        return ret;
    }
}
