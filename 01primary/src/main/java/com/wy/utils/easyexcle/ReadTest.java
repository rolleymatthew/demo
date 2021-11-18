package com.wy.utils.easyexcle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.wy.bean.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunwang on 2021/10/19 10:44
 */
public class ReadTest {
    public static void main(String[] args) {
        String fileName = "d:\\" + File.separator + "demo.xlsx";
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        EasyExcel.read(fileName, WriteTest.DemoData.class, new DemoDataListener()).doReadAll();

        // 读取部分sheet
        fileName = "d:\\" + File.separator + "all600519.xlsx";
        ExcelReader excelReader = null;
        StockFinDateBean stockFinDateBean=new StockFinDateBean();
        try {
            excelReader = EasyExcel.read(fileName).build();

            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(ProfitDateBean.class)
                            .registerReadListener(new PageReadListener<ProfitDateBean>(s->{
                                if (CollectionUtils.isNotEmpty(s)){
                                    stockFinDateBean.setProfitDateBean(s);
                                }
                            })).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(BalanceDateBean.class)
                            .registerReadListener(new PageReadListener<BalanceDateBean>(s->{
                                if (CollectionUtils.isNotEmpty(s)){
                                    stockFinDateBean.setBalanceDateBean(s);
                                }
                            })).build();
            ReadSheet readSheet3 =
                    EasyExcel.readSheet(2).head(CashFlowBean.class)
                            .registerReadListener(new PageReadListener<CashFlowBean>(s->{
                                if (CollectionUtils.isNotEmpty(s)){
                                    stockFinDateBean.setCashFlowBean(s);
                                }
                            })).build();
            ReadSheet readSheet4 =
                    EasyExcel.readSheet(3).head(FinanceDataBean.class)
                            .registerReadListener(new PageReadListener<FinanceDataBean>(s->{
                                if (CollectionUtils.isNotEmpty(s)){
                                    stockFinDateBean.setFinanceDataBean(s);
                                }
                            })).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2,readSheet3,readSheet4);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
        stockFinDateBean.getBalanceDateBean().stream().forEach(s-> System.out.println(s.getReportDate()));
        stockFinDateBean.getProfitDateBean().stream().forEach(s-> System.out.println(s.getOperatingIncome()));
        stockFinDateBean.getCashFlowBean().stream().forEach(s-> System.out.println(s.getUnrecognizedInvestmentLoss()));
        stockFinDateBean.getFinanceDataBean().stream().forEach(s-> System.out.println(s.getNetAssetsWeight()));
     }
}
