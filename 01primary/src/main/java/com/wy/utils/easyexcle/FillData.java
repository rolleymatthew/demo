package com.wy.utils.easyexcle;

import java.math.BigDecimal;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
    private BigDecimal ssAAt;

}
