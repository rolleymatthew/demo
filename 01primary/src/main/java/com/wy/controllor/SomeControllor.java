package com.wy.controllor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yunwang on 2021/5/19 9:51
 */

@RestController
public class SomeControllor {

    @GetMapping("some")
    public String someHandle(){
        return "hello SpringBoot";
    }
}
