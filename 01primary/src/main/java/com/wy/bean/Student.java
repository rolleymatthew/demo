package com.wy.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * Created by yunwang on 2021/5/20 14:33
 */
@Component
@PropertySource(value = "classpath:custom.properties", encoding = "UTF-8")
@ConfigurationProperties("student")
@Data
public class Student {
    private String name;
    private int age;
    private String fav;

}
