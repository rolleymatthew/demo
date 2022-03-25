package com.wy.utils.easyexcle;

import com.alibaba.excel.EasyExcel;
import com.wy.utils.TestFileUtil;

import java.io.File;

/**
 * @Author: yunwang
 */
public class FillWrite {
    private static final String PATH = "d:\\stock\\";

    public static void main(String[] args) {
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        String templateFileName =
                TestFileUtil.getPath() + "demo" + File.separator + "fill" + File.separator + "simple.xlsx";

        // 方案1 根据对象填充
        String fileName = PATH + "simpleFill" + System.currentTimeMillis() + ".xlsx";
        // 这里 会填充到第一个sheet， 然后文件流会自动关T闭
        FillData fillData = new FillData();
        fillData.setName("张三");
        fillData.setNumber(5.2);
        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(fillData);

//        templateFileName =
//                "d:\\stock\\demo" + File.separator + "pe.xlsx";
//        fileName = "d:\\stock\\demo" + File.separator + "pe601318.xlsx";
//        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(fillData);
    }
}
