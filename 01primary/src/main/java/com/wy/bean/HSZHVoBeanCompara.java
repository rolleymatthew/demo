package com.wy.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yunwang
 * @Date 2021-10-20
 */
@Data
public class HSZHVoBeanCompara extends EastMoneyBeab.ResultDTO.DataDTO{
    @ExcelProperty("买卖方向")
    private String methored;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof String){
            if(this.getSecurityCode().equals(obj.toString())){
                return true;
            }else {
                return false;
            }
        }else{
            return  super.equals(obj);
        }    }
}
