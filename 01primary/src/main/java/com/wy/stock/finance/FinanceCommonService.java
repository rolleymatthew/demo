package com.wy.stock.finance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.wy.bean.*;
import com.wy.service.impl.StockServiceImpl;
import com.wy.utils.AllStock;
import com.wy.utils.ClassUtil;
import com.wy.utils.FilesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yunwang
 * @Date 2021-11-03
 */
public class FinanceCommonService {
    private static Logger logger = LoggerFactory.getLogger(FinanceCommonService.class);

    public static String PATH_MAIN = Contant.DIR + File.separator + "financeStock";

    public static String FILE_NAME_REPORT = "三率三升%s.xlsx";
    public static String FILE_NAME_PER = "同比环比%s.xlsx";
    public static String FILE_NAME_ZQH = "%s财务透视.xlsx";
    public static String FILE_NAME_ALL = "all%s.xlsx";

    public static String PATH_REPORT = Contant.DIR + File.separator + "report";
    public static String PATH_ALL = PATH_MAIN + File.separator + "all";
    public static String PATH_ZQH = Contant.DIR + File.separator + "ZQHFin";

    public static Map<String, String> BalanceDicMap = convertDicMap(ConstantBean.balance);

    public static Map<String, String> CashFlowDicMap = convertDicMap(ConstantBean.cashFlow);

    public static List<String> noXlsxFileList = new ArrayList<>();

    /**
     * 把字符数据格式化成二维数组
     * 定义两个做行转列用的二维数组,把数据赋值到一个二维数组orgData里,用第二个二维数组newData实现行转列
     *
     * @param temp 需要格式化的字符数据
     * @return 格式化好的二维数组，准备生成bean
     */
    public static String[][] getArrayDates(String temp) {
        List<String[]> collect = getStringsList(temp);
        if (CollectionUtils.isEmpty(collect)) return null;

        String[][] orgData = fillStringsArray(collect);
        String[][] newData = lineToColumnsArray(orgData, collect);

        return newData;
    }

    /**
     * 行数据，对应bean的属性，每一行数据同时按照","拆分成数组
     *
     * @param temp
     * @return 按照bean的一列数据
     */
    public static List<String[]> getStringsList(String temp) {
        String[] line = temp.split(StringUtils.CR + StringUtils.LF);
        List<String[]> collect = Arrays.stream(line).filter(x -> x.length() > 10)
                .map(l -> l.trim().split(",")).collect(Collectors.toList());
        return collect;
    }

    /**
     * 行转列
     *
     * @param orgData
     * @param collect
     * @return
     */
    private static String[][] lineToColumnsArray(String[][] orgData, List<String[]> collect) {
        int lineLen = getHeaders(collect).size();
        int columnLen = getColumnLens(collect);
        String[][] newData = new String[columnLen][lineLen];
        for (int i = 0; i < lineLen; i++) {
            for (int j = 0; j < columnLen; j++) {
                newData[j][i] = orgData[i][j];
            }
        }
        return newData;
    }

    /**
     * 获取表头数据
     *
     * @param collect
     * @return
     */
    public static List<String> getHeaders(List<String[]> collect) {
        List<String> col = collect.stream()
                .filter(x -> x.length >= 2)
                .map(s -> s[0])
                .collect(Collectors.toList());
        List<String> header = col.stream().filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.toList());
        return header;
    }

    /**
     * 填充二维数组数据，准备行转列
     *
     * @param collect
     * @return
     */
    private static String[][] fillStringsArray(List<String[]> collect) {
        //二维数组赋值
        int columnLen = getColumnLens(collect);
        int lineLen = collect.size();
        String[][] orgData = new String[lineLen][columnLen];
        for (int i = 0; i < lineLen; i++) {
            String[] split = collect.get(i);
            for (int j = 1; j < columnLen; j++) {
                orgData[i][j - 1] = split[j];

            }
        }
        return orgData;
    }

    /**
     * 获取有多少天的数据，既是行转列的行数
     *
     * @param collect
     * @return
     */
    public static int getColumnLens(List<String[]> collect) {
        String[] cell = collect.get(0);
        int columnLen = cell.length;
        return columnLen;
    }

    /**
     * 所有代码
     *
     * @param debug 调试标记位 true正式产生所有数据 false只产生一个代码的数据
     * @return
     */
    public static List<String> getAllCodes(boolean debug) {
        List<String> codeList = new ArrayList<>();
        if (debug) {
            codeList.add(StringUtils.trim(AllStock.SH_MAIN_));
            return codeList;
        }
        String[] codes = AllStock.SH_MAIN.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }

        codes = AllStock.SH_KC.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }

        codes = AllStock.SZ.split(",");
        for (String code : codes) {
            codeList.add(StringUtils.trim(code));
        }
        return codeList;
    }

    /**
     * 把下载的文件转化成实体bean列表，按照规定好的表头映射
     *
     * @param temp
     * @param dicMap
     * @param clazz
     * @return
     */
    public static List<Object> convertStringToBeans(String temp, Map<String, String> dicMap, Class<?> clazz) {
        List<Object> ret = new ArrayList<>();
        //1.拆分出行，抽取表头数据,计算出行数，对应BEAN的属性
        List<String[]> stringList = FinanceCommonService.getStringsList(temp);
        if (CollectionUtils.isEmpty(stringList)) return null;
        List<String> header = FinanceCommonService.getHeaders(stringList);
        if (CollectionUtils.isEmpty(header)) return null;

        //2.产生二维数组
        String[][] newData = FinanceCommonService.getArrayDates(temp);

        //3.使用类的反射机制把生成的二维数组转化成需要的bean列表，返回
        int columnLen = FinanceCommonService.getColumnLens(stringList);
        for (int line = 0; line < columnLen; line++) {
            Object o = null;
            try {
                o = clazz.newInstance();
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }

            if (o == null) continue;
            for (int col = 0; col < header.size(); col++) {
                ClassUtil.setFieldValueByFieldName(o, StringUtils.trim(dicMap.get(StringUtils.trim(header.get(col)))), newData[line][col]);
            }
            ret.add(o);

        }
        return ret;
    }

    /**
     * 拆分标题和bean属性对应字典
     *
     * @param dicString
     * @return
     */
    public static Map<String, String> convertDicMap(String dicString) {
        return Stream.of(dicString).map(c -> c.split(","))
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(x -> StringUtils.substring(x, 0, StringUtils.indexOf(x, "|")).trim()
                        , x -> StringUtils.substring(x, StringUtils.indexOf(x, "|") + 1).trim()));
    }


    /**
     * 利润表、资产负债表、现金流表、主要财务指标
     *
     * @param allCodes
     * @return
     */
    public static Map<String, StockFinDateBean> getStockFinDateMap(List<String> allCodes) {
        Map<String, StockFinDateBean> dataMap = new ConcurrentHashMap<>();
        allCodes.parallelStream().forEach(f -> {
            String fileName = PATH_ALL + File.separator + File.separator
                    + String.format(FILE_NAME_ALL, StringUtils.trim(f));
            if (!FilesUtil.existsAndIsFile(fileName)) {
                noXlsxFileList.add(f);
            } else {
                ExcelReader excelReader = null;
                StockFinDateBean stockFinDateBean = new StockFinDateBean();
                try {
                    excelReader = EasyExcel.read(fileName).build();

                    ReadSheet readSheet1 =
                            EasyExcel.readSheet(0).head(ProfitDateBean.class)
                                    .registerReadListener(new PageReadListener<ProfitDateBean>(s -> {
                                        if (CollectionUtils.isNotEmpty(s)
                                                && CollectionUtils.isEmpty(stockFinDateBean.getProfitDateBean())) {
                                            stockFinDateBean.setProfitDateBean(s);
                                        } else if (CollectionUtils.isNotEmpty(stockFinDateBean.getProfitDateBean())) {
                                            stockFinDateBean.getProfitDateBean().addAll(s);
                                        }
                                    })).build();
                    ReadSheet readSheet2 =
                            EasyExcel.readSheet(1).head(BalanceDateBean.class)
                                    .registerReadListener(new PageReadListener<BalanceDateBean>(s -> {
                                        if (CollectionUtils.isNotEmpty(s)
                                                && CollectionUtils.isEmpty(stockFinDateBean.getBalanceDateBean())) {
                                            stockFinDateBean.setBalanceDateBean(s);
                                        } else if (CollectionUtils.isNotEmpty(stockFinDateBean.getBalanceDateBean())) {
                                            stockFinDateBean.getBalanceDateBean().addAll(s);
                                        }
                                    })).build();
                    ReadSheet readSheet3 =
                            EasyExcel.readSheet(2).head(CashFlowBean.class)
                                    .registerReadListener(new PageReadListener<CashFlowBean>(s -> {
                                        if (CollectionUtils.isNotEmpty(s)
                                                && CollectionUtils.isEmpty(stockFinDateBean.getCashFlowBean())) {
                                            stockFinDateBean.setCashFlowBean(s);
                                        } else if (CollectionUtils.isNotEmpty(stockFinDateBean.getCashFlowBean())) {
                                            stockFinDateBean.getCashFlowBean().addAll(s);
                                        }
                                    })).build();
                    ReadSheet readSheet4 =
                            EasyExcel.readSheet(3).head(FinanceDataBean.class)
                                    .registerReadListener(new PageReadListener<FinanceDataBean>(s -> {
                                        if (CollectionUtils.isNotEmpty(s)
                                                && CollectionUtils.isEmpty(stockFinDateBean.getFinanceDataBean())) {
                                            stockFinDateBean.setFinanceDataBean(s);
                                        } else if (CollectionUtils.isNotEmpty(stockFinDateBean.getFinanceDataBean())) {
                                            stockFinDateBean.getFinanceDataBean().addAll(s);
                                        }
                                    })).build();
                    // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
                    excelReader.read(readSheet1, readSheet2, readSheet3, readSheet4);
                } finally {
                    if (excelReader != null) {
                        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                        excelReader.finish();
                    }
                }
                if (CollectionUtils.isEmpty(stockFinDateBean.getProfitDateBean())
                        || CollectionUtils.isEmpty(stockFinDateBean.getBalanceDateBean())
                        || CollectionUtils.isEmpty(stockFinDateBean.getCashFlowBean())
                        || CollectionUtils.isEmpty(stockFinDateBean.getFinanceDataBean())) {
                    noXlsxFileList.add(f);
                } else {
                    dataMap.put(f, stockFinDateBean);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(noXlsxFileList)) {
            noXlsxFileList.stream().forEach(s -> logger.info("{} {} file no exist", "all", s));
        }
        return dataMap;
    }
}
