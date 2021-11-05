package com.wy.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yunwang
 * @Date 2021-11-06
 */
@Data
@Component
@ConfigurationProperties(prefix = "stockcode")
public class YmlBean {
    private List<String> balance;
}
