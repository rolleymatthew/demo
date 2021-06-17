package com.wy;

import com.wy.service.SomeService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private SomeService someService;
    @Test
    @Ignore
    void contextLoads() {
        System.out.println(someService.hello());
    }

    @Test
    void getCodeTest(){
        System.out.println(someService.getCode());
    }
}
