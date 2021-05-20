package com.wy.controllor;

import com.wy.service.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yunwang on 2021/5/19 9:51
 */

@RestController
public class SomeControllor {

    @Autowired
    private SomeService someService;
    @GetMapping("some")
    public String someHandle(){
        return someService.hello();
    }
}
