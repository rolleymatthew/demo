package com.wy.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by yunwang on 2021/6/17 15:24
 */
@Component
@PropertySource(value = "classpath:stock.properties", encoding = "UTF-8")
@ConfigurationProperties("stock")
@Data
public class Stock {
    private String shCode;
    private String szCode;
    private String finCode;

}
