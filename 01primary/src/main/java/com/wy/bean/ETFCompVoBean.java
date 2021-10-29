package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Created by yunwang on 2021/10/29 19:51
 */
@Data
public class ETFCompVoBean extends ETFBean.PageHelpDTO.DataDTO {
    @ExcelProperty(value = "前期日期", index = 5)
    private String beforeDate;
    @ExcelProperty(value = "前期规模(万份)", index = 6)
    private Double beforeValue;
    @ExcelProperty(value = "期间变化总规模(万份)", index = 7)
    private Double addTotValue = 0.0;
}
