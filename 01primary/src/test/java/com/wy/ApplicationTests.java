package com.wy;

import com.wy.bean.YmlBean;
import com.wy.service.SomeService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private SomeService someService;
    @Test
    @Ignore
    void contextLoads() {
        System.out.println(someService.hello());
    }

    @Autowired
    private YmlBean ymlBean;

    @Test
//    @Ignore
    void ymlTest() {
        ymlBean.getBalance().forEach(x -> System.out.println("line:"+x));
        ymlBean.getSh().entrySet().stream().forEach(x-> System.out.println(x.getKey()+"   "+x.getValue()));
        ymlBean.getKc().entrySet().stream().forEach(x-> System.out.println(x.getKey()+"   "+x.getValue()));
        ymlBean.getSz().entrySet().stream().forEach(x-> System.out.println(x.getKey()+"   "+x.getValue()));
    }
}
