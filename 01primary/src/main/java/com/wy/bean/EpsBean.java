package com.wy.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: yunwang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EpsBean {
    private String date;
    private BigDecimal eps;
}
