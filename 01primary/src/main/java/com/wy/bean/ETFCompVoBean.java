package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Created by yunwang on 2021/10/29 19:51
 */
@Data
public class ETFCompVoBean extends ETFBean.PageHelpDTO.DataDTO{
    @ExcelProperty("期间变化总规模(万份)")
    private Double addTotValue;
}
