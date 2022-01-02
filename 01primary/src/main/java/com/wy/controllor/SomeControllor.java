package com.wy.controllor;

import com.wy.bean.Student;
import com.wy.service.SomeService;
import com.wy.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yunwang on 2021/5/19 9:51
 */

@RestController
public class SomeControllor {

    @Autowired
    private SomeService someService;

    @GetMapping("/some")
    public String someHandle() {
        return someService.hello();
    }

    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public String portHandle() {
        return "port=" + port;
    }

    @Value("${compte.adasfa}")
    private String custome;
    @GetMapping("/custome")
    public String customeHandle() {
        return "custome=" + custome;
    }

    @Autowired
    private Student student;
    @GetMapping("/student")
    public String studentHandle() {
        return "student=" + student;
    }

    @GetMapping("/cookie")
    public String cookieHandle(HttpServletRequest request) {
        String voteUserPhone = CookieUtils.getCookieAttribute("stut", request);
        return voteUserPhone;
    }
}
