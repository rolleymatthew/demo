package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yunwang
 * @Date 2021-10-20
 */
@Data
public class HSZHVoBeanCompara extends EastMoneyBeab.ResultDTO.DataDTO {
    @ExcelProperty(value = "买卖方向", index = 20)
    private String methored;

}
