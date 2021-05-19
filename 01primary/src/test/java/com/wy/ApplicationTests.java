package com.wy;

import com.wy.service.SomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private SomeService someService;
    @Test
    void contextLoads() {
        System.out.println(someService.hello());
    }

}
