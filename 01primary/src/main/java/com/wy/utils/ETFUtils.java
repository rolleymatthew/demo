package com.wy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ETFUtils {
    private static String url = "http://query.sse.com.cn/commonQuery.do?" +
            "isPagination=true&pageHelp.pageSize=%s" +
            "&pageHelp.pageNo=1&pageHelp.cacheSize=1" +
            "&sqlId=COMMON_SSE_ZQPZ_ETFZL_XXPL_ETFGM_SEARCH_L&STAT_DATE=%s" +
            "&_=1606103195911";
//    private static String url = "http://query.sse.com.cn/commonQuery" +
//            ".do?isPagination=true&pageHelp.pageSize=%s&pageHelp" +
//            ".pageNo=1&pageHelp.cacheSize=1&sqlId=COMMON_SSE_ZQPZ_ETFZL_XXPL_ETFGM_SEARCH_L&STAT_DATE=%s" +
//            "&_=1606103195911";
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static String pat = "d:\\etf";

    public static List<String> dateList = new ArrayList<>();

    private static List<ResultClass> totalResult = new ArrayList<>();
    private static String[] columnNames = {"日期", "名称", "代码", "总份额(万份)","第一天","第二天","第三天"};
    private static String[] columnNamesScope = {"日期", "名称", "代码", "总份额(万份)","比对日期","比对前总份额(万份)", "变动份额(万份)"};

    public static void main(String[] args) throws IOException, InterruptedException {
        //导出一天的所有数据文件
//        getETFcsv(1, false, false, true);
        //导出20天和长天期的数据文件
//        getETFcsv(20, true,true,true);
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        getETFcsv(91, false, true, false, exportExcelUtil);

        //计算数据
        HashMap<Integer, List<ResultClass>> totalMap = new HashMap<>();
        for (ResultClass resultClass : totalResult) {
            List<ResultClass> list = new ArrayList<>();
            Integer code = 0;

            code = Integer.parseInt(resultClass.getSEC_CODE());
            if (totalMap.containsKey(code)) {
                list = totalMap.get(code);
            }
            list.add(resultClass);
            totalMap.put(code, list);

        }

        //1.比昨天的
        getOnedayComp(totalMap, exportExcelUtil);
        getOneDayDelComp(totalMap, exportExcelUtil);

        //2.连续两天增加的
        getTwoDayComp(totalMap, exportExcelUtil);
        getTwoDayDelComp(totalMap, exportExcelUtil);

        //3.连续3天增加的
        getFourDayAddComp(totalMap, exportExcelUtil);
        getFourDayDelComp(totalMap, exportExcelUtil);

        //4.三天变化最大的前十
        int[] days = {1,2,3,5,10,20,30,60,90};
        for (int day : days) {
            getScopesByDays(exportExcelUtil, totalMap, day);
        }
    }

    private static void getScopesByDays(ExportExcelUtil exportExcelUtil, HashMap<Integer, List<ResultClass>> totalMap, int days) throws FileNotFoundException {
        //增加规模
        List<ScopeClass> addMap = new ArrayList<>();
        //减少规模
        List<ScopeClass> reduceMap = new ArrayList<>();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> listEntryValue = integerListEntry.getValue();
            if (listEntryValue.size() >= days+1) {

                ResultClass resultClass = listEntryValue.get(0);
                ResultClass resultClass2 = listEntryValue.get(days);
                ScopeClass scopeClass = new ScopeClass();
                scopeClass.setTOT_VOL(resultClass.getTOT_VOL());
                scopeClass.setTOT_VOL2(resultClass2.getTOT_VOL());
                scopeClass.setSEC_CODE(resultClass.getSEC_CODE());
                scopeClass.setSEC_NAME(resultClass.getSEC_NAME());
                scopeClass.setSTAT_DATE(resultClass.getSTAT_DATE());
                scopeClass.setSTAT_DATE2(resultClass2.getSTAT_DATE());
                if (resultClass.getTOT_VOL() > resultClass2.getTOT_VOL()) {
                    scopeClass.setADD_VOL(resultClass.getTOT_VOL() - resultClass2.getTOT_VOL());
                    addMap.add(scopeClass);
                } else {
                    scopeClass.setADD_VOL(resultClass2.getTOT_VOL() - resultClass.getTOT_VOL());
                    reduceMap.add(scopeClass);
                }
            }
        }
        sortByAdd(addMap);
        sortByAdd(reduceMap);
        exportExcelUtil.exportExcel("ETF_" + days + "day_add", columnNamesScope, addMap, new FileOutputStream(pat + "//" +
                        "ETF_" + days + "day_add_scope.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
        exportExcelUtil.exportExcel("ETF_" + days + "day_reduce", columnNamesScope, reduceMap, new FileOutputStream(pat +
                        "//" +
                        "ETF_" + days + "day_reduce_scope.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
    }

    private static void getFourDayDelComp(HashMap<Integer, List<ResultClass>> totalMap, ExportExcelUtil exportExcelUtil) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ResultClass> resultClassListAll = new ArrayList<>();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> resultClassList = integerListEntry.getValue();
            Double yesterdayETF = 0.0;
            Double nowETF = 0.0;
            Double thirdETF = 0.0;
            Double fourETF = 0.0;
            for (ResultClass resultClass : resultClassList) {
                if (resultClass.getSTAT_DATE().equals(dateList.get(0))) {
                    nowETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(1))) {
                    yesterdayETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(2))) {
                    thirdETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(3))) {
                    fourETF = resultClass.getTOT_VOL();
                }
                if (yesterdayETF > 0 && nowETF > 0 && thirdETF > 0 && fourETF > 0
                        && fourETF > thirdETF
                        && thirdETF > yesterdayETF
                        && yesterdayETF > nowETF) {
                    resultClassListAll.add(resultClass);
                    resultClass.setDEFF1(yesterdayETF-nowETF);
                    resultClass.setDEFF2(thirdETF-yesterdayETF);
                    resultClass.setDEFF3(fourETF-thirdETF);
                    stringBuilder.append(getString(resultClass));
                    yesterdayETF = 0.0;
                    nowETF = 0.0;
                    thirdETF = 0.0;
                    fourETF = 0.0;
                }
            }
        }
        sort(resultClassListAll);
        exportExcelUtil.exportExcel("ETF_3day_del", columnNames, resultClassListAll, new FileOutputStream(pat + "//" + "ETF_3day_del.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);

//        writeFile(pat, "ETF_3day_del.csv", stringBuilder.toString());
    }

    private static void getFourDayAddComp(HashMap<Integer, List<ResultClass>> totalMap, ExportExcelUtil exportExcelUtil) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ResultClass> resultClassListAll = new ArrayList<>();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> resultClassList = integerListEntry.getValue();
            Double yesterdayETF = 0.0;
            Double nowETF = 0.0;
            Double thirdETF = 0.0;
            Double fourETF = 0.0;
            for (ResultClass resultClass : resultClassList) {
                if (resultClass.getSTAT_DATE().equals(dateList.get(0))) {
                    nowETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(1))) {
                    yesterdayETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(2))) {
                    thirdETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(3))) {
                    fourETF = resultClass.getTOT_VOL();
                }
                if (yesterdayETF > 0 && nowETF > 0 && thirdETF > 0 && fourETF > 0
                        && fourETF < thirdETF
                        && thirdETF < yesterdayETF
                        && yesterdayETF < nowETF) {
                    stringBuilder.append(getString(resultClass));
                    resultClass.setDEFF1(nowETF-yesterdayETF);
                    resultClass.setDEFF2(yesterdayETF-thirdETF);
                    resultClass.setDEFF3(thirdETF-fourETF);
                    resultClassListAll.add(resultClass);
                    yesterdayETF = 0.0;
                    nowETF = 0.0;
                    thirdETF = 0.0;
                    fourETF = 0.0;
                }
            }
        }
        sort(resultClassListAll);
        exportExcelUtil.exportExcel("ETF_3day_add", columnNames, resultClassListAll, new FileOutputStream(pat + "//" + "ETF_3day_add.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
//        writeFile(pat, "ETF_3day_add.csv", stringBuilder.toString());
    }

    private static void getTwoDayDelComp(HashMap<Integer, List<ResultClass>> totalMap, ExportExcelUtil exportExcelUtil) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ResultClass> resultClassListAll = new ArrayList<>();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> resultClassList = integerListEntry.getValue();
            Double yesterdayETF = 0.0;
            Double nowETF = 0.0;
            Double thirdETF = 0.0;
            for (ResultClass resultClass : resultClassList) {
                if (resultClass.getSTAT_DATE().equals(dateList.get(0))) {
                    nowETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(1))) {
                    yesterdayETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(2))) {
                    thirdETF = resultClass.getTOT_VOL();
                }
                if (yesterdayETF > 0 && nowETF > 0 && thirdETF > 0
                        && thirdETF > yesterdayETF
                        && yesterdayETF > nowETF) {
                    stringBuilder.append(getString(resultClass));
                    resultClass.setDEFF1(yesterdayETF-nowETF);
                    resultClass.setDEFF2(thirdETF-yesterdayETF);
                    resultClassListAll.add(resultClass);
                    yesterdayETF = 0.0;
                    nowETF = 0.0;
                    thirdETF = 0.0;
                }
            }
        }
        sort(resultClassListAll);
        exportExcelUtil.exportExcel("ETF_2day_del", columnNames, resultClassListAll, new FileOutputStream(pat + "//" + "ETF_2day_del.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
//        writeFile(pat, "ETF_2day_del.csv", stringBuilder.toString());
    }

    private static void getOneDayDelComp(HashMap<Integer, List<ResultClass>> totalMap, ExportExcelUtil exportExcelUtil) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ResultClass> resultClassListAll = new ArrayList<>();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> resultClassList = integerListEntry.getValue();
            Double yesterdayETF = 0.0;
            Double nowETF = 0.0;
            for (ResultClass resultClass : resultClassList) {
                if (resultClass.getSTAT_DATE().equals(dateList.get(0))) {
                    nowETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(1))) {
                    yesterdayETF = resultClass.getTOT_VOL();
                }
                if (yesterdayETF > 0 && nowETF > 0 && yesterdayETF > nowETF) {
                    stringBuilder.append(getString(resultClass));
                    resultClass.setDEFF1(yesterdayETF-nowETF);
                    resultClassListAll.add(resultClass);
                    yesterdayETF = 0.0;
                    nowETF = 0.0;
                }
            }
        }
        sort(resultClassListAll);
        exportExcelUtil.exportExcel("ETF_1day_del", columnNames, resultClassListAll, new FileOutputStream(pat + "//" + "ETF_1day_del.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
//        writeFile(pat, "ETF_1day_del.csv", stringBuilder.toString());
    }

    private static void getTwoDayComp(HashMap<Integer, List<ResultClass>> totalMap, ExportExcelUtil exportExcelUtil) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ResultClass> resultClassListAll = new ArrayList<>();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> resultClassList = integerListEntry.getValue();
            Double yesterdayETF = 0.0;
            Double nowETF = 0.0;
            Double thirdETF = 0.0;
            for (ResultClass resultClass : resultClassList) {
                if (resultClass.getSTAT_DATE().equals(dateList.get(0))) {
                    nowETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(1))) {
                    yesterdayETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(2))) {
                    thirdETF = resultClass.getTOT_VOL();
                }
                if (yesterdayETF > 0 && nowETF > 0 && thirdETF > 0
                        && thirdETF < yesterdayETF
                        && yesterdayETF < nowETF) {
                    stringBuilder.append(getString(resultClass));
                    resultClass.setDEFF1(nowETF-yesterdayETF);
                    resultClass.setDEFF2(yesterdayETF-thirdETF);
                    resultClassListAll.add(resultClass);
                    yesterdayETF = 0.0;
                    nowETF = 0.0;
                    thirdETF = 0.0;
                }
            }
        }
        sort(resultClassListAll);
        exportExcelUtil.exportExcel("ETF_2day_add", columnNames, resultClassListAll, new FileOutputStream(pat + "//" + "ETF_2day_add.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
//        writeFile(pat, "ETF_2day_add.csv", stringBuilder.toString());
    }

    private static void getOnedayComp(HashMap<Integer, List<ResultClass>> totalMap, ExportExcelUtil exportExcelUtil) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ResultClass> resultClassListAll = new ArrayList();
        for (Map.Entry<Integer, List<ResultClass>> integerListEntry : totalMap.entrySet()) {
            List<ResultClass> resultClassList = integerListEntry.getValue();
            Double yesterdayETF = 0.0;
            Double nowETF = 0.0;
            for (ResultClass resultClass : resultClassList) {
                if (resultClass.getSTAT_DATE().equals(dateList.get(0))) {
                    nowETF = resultClass.getTOT_VOL();
                }
                if (resultClass.getSTAT_DATE().equals(dateList.get(1))) {
                    yesterdayETF = resultClass.getTOT_VOL();
                }
                if (yesterdayETF > 0 && nowETF > 0 && yesterdayETF < nowETF) {
                    stringBuilder.append(getString(resultClass));
                    resultClass.setDEFF1(nowETF-yesterdayETF);
                    resultClassListAll.add(resultClass);
                    yesterdayETF = 0.0;
                    nowETF = 0.0;
                }
            }
        }
        sort(resultClassListAll);
        exportExcelUtil.exportExcel("ETF_1day_add", columnNames, resultClassListAll, new FileOutputStream(pat + "//" + "ETF_1day_add.xlsx"),
                ExportExcelUtil.EXCEl_FILE_2007);
//        writeFile(pat, "ETF_1day_add.csv", stringBuilder.toString());
    }

    private static void getETFcsv(int days, boolean longDuration, boolean expSigleCSV, boolean expTotalCSV, ExportExcelUtil exportExcelUtil) throws IOException,
            InterruptedException {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_SHORT);
        Date date = new Date();
        //当前时间前20天的日期
        for (int i = 0; i < days; i++) {
            date = getPreviousWorkingDay(date, -1);
            String ss = df.format(date);
            List<ResultClass> resultClasses = getResultClassesByDay(ss);
            int dayCount = 1;
            while (CollectionUtils.isEmpty(resultClasses) && dayCount <= 10) {
                dayCount++;
                date = getPreviousWorkingDay(date, -1);
                ss = df.format(date);
                resultClasses = getResultClassesByDay(ss);
            }
            dateList.add(ss);
        }

        //当前日期的第30、60、90、120、
        if (longDuration) {
            dateList.add(df.format(getPreviousWorkingDay(new Date(), -30)));
            dateList.add(df.format(getPreviousWorkingDay(new Date(), -60)));
            dateList.add(df.format(getPreviousWorkingDay(new Date(), -90)));
            dateList.add(df.format(getPreviousWorkingDay(new Date(), -120)));
            dateList.add(df.format(getPreviousWorkingDay(new Date(), -250)));
        }
        //获取数据，生成文件
        for (String s : dateList) {
            List<ResultClass> temp = getETF(s);
            Thread.sleep(1000);
            //按照日期写入单个文件
            if (CollectionUtils.isNotEmpty(temp) && expSigleCSV) {
                //写入CSV
                String tempString = getStringBuilder(temp).toString();
//                writeFile(pat, "ETF_" + s + ".csv", tempString);
                //写入excle
                exportExcelUtil.exportExcel(s, columnNames, temp, new FileOutputStream(pat + "//" + "ETF_" + s + ".xlsx"),
                        ExportExcelUtil.EXCEl_FILE_2007);
            } else if (CollectionUtils.isEmpty(temp)) {
                System.out.println(s + "内容为空");
            }
        }
        if (CollectionUtils.isNotEmpty(totalResult) && expTotalCSV) {
            //写入excle
            exportExcelUtil.exportExcel("全部", columnNames, totalResult, new FileOutputStream(pat + "//" + "ETF.xlsx"),
                    ExportExcelUtil.EXCEl_FILE_2007);
//            writeFile(pat, "ETF.csv", getStringBuilder(totalResult).toString());
        }
    }

    //计算周末，得到上一个指定间隔的工作日
    public static Date getPreviousWorkingDay(Date date, int interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayOfWeek;
        do {
            cal.add(Calendar.DAY_OF_MONTH, interval);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        } while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);

        return cal.getTime();
    }

    private static List<ResultClass> getETF(String data) throws IOException {
        //1.得到返回数组，加入总体数据组
        List<ResultClass> resultClassList = getResultClassesByDay(data);
        sort(resultClassList);
        totalResult.addAll(resultClassList);

        //2.拼接字符串输出文件
        return resultClassList;
    }

    private static List<ResultClass> getResultClassesByDay(String data) {
        String urlString = String.format(url, 500, data);
        return getResultClasses(urlString);
    }

    private static void sort(List<ResultClass> resultClassList) {
        Collections.sort(resultClassList, new Comparator<ResultClass>() {
            @Override
            public int compare(ResultClass o1, ResultClass o2) {
                return (int) (o2.getTOT_VOL() - o1.getTOT_VOL());
            }
        });
    }

    private static void sortByAdd(List<ScopeClass> resultClassList) {
        Collections.sort(resultClassList, new Comparator<ScopeClass>() {
            @Override
            public int compare(ScopeClass o1, ScopeClass o2) {
                return (int) (o2.getADD_VOL() - o1.getADD_VOL());
            }
        });
    }

    private static StringBuilder getStringBuilder(List<ResultClass> resultClassList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ResultClass resultClass : resultClassList) {
            String s = getString(resultClass);
            stringBuilder.append(s);
        }
        return stringBuilder;
    }

    private static String getString(ResultClass resultClass) {
        return resultClass.getSTAT_DATE()
                + "," + resultClass.getSEC_CODE()
                + "," + resultClass.getSEC_NAME()
                + "," + resultClass.getTOT_VOL() + "\n";
    }

    private static List<ResultClass> getResultClasses(String url) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Proxy-Connection", "keep-alive");
        headerMap.put("Cookie", "yfx_c_g_u_id_10000042=_ck21101414504517997211586335557; VISITED_MENU=%5B%228491%22%5D; JSESSIONID=34BC087D3D7D634FB2AEB302A405762C; yfx_f_l_v_t_10000042=f_t_1634194245788__r_t_1634194245788__v_t_1634194438289__r_c_0");
        headerMap.put("Host", "query.sse.com.cn");
        headerMap.put("Referer", "http://www.sse.com.cn/");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        String ss = OkHttpUtil.doGet(url, headerMap,null);
        JSONObject jsonObject = JSON.parseObject(ss);
        JSONArray result = jsonObject.getJSONArray("result");
        List<ResultClass> resultClassList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ResultClass resultClass = new ResultClass();
            resultClass.setSEC_CODE(result.getJSONObject(i).get("SEC_CODE").toString());
            resultClass.setSEC_NAME(result.getJSONObject(i).get("SEC_NAME").toString());
            resultClass.setSTAT_DATE(result.getJSONObject(i).get("STAT_DATE").toString());
            resultClass.setTOT_VOL(Double.parseDouble(result.getJSONObject(i).get("TOT_VOL").toString()));
            resultClassList.add(resultClass);
        }
        return resultClassList;
    }

    public static void writeFile(String filepath, String filename, String text) throws IOException {

        mkdirs(filepath);
        FileOutputStream fos = new FileOutputStream(filepath + "\\" + filename);

        OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");

        BufferedWriter out = new BufferedWriter(osw);
        out.write(text);
        out.flush();

        osw.flush();

        fos.flush();

    }

    public static boolean existsAndIsFile(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile();
    }

    public static void mkdir(String filename) throws IOException {
        File file = new File(filename);
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

    public static void mkdirs(String filename) throws IOException {
        File file = new File(filename);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    static class ResultClass {
        private String STAT_DATE;
        private String SEC_NAME;
        private String SEC_CODE;
        private Double TOT_VOL;
        private Double DEFF1;
        private Double DEFF2;
        private Double DEFF3;

        public Double getDEFF1() {
            return DEFF1;
        }

        public void setDEFF1(Double DEFF1) {
            this.DEFF1 = DEFF1;
        }

        public Double getDEFF2() {
            return DEFF2;
        }

        public void setDEFF2(Double DEFF2) {
            this.DEFF2 = DEFF2;
        }

        public Double getDEFF3() {
            return DEFF3;
        }

        public void setDEFF3(Double DEFF3) {
            this.DEFF3 = DEFF3;
        }

        public String getSEC_NAME() {
            return SEC_NAME;
        }

        public void setSEC_NAME(String SEC_NAME) {
            this.SEC_NAME = SEC_NAME;
        }

        public String getSEC_CODE() {
            return SEC_CODE;
        }

        public void setSEC_CODE(String SEC_CODE) {
            this.SEC_CODE = SEC_CODE;
        }

        public Double getTOT_VOL() {
            return TOT_VOL;
        }

        public void setTOT_VOL(Double TOT_VOL) {
            this.TOT_VOL = TOT_VOL;
        }

        public String getSTAT_DATE() {
            return STAT_DATE;
        }

        public void setSTAT_DATE(String STAT_DATE) {
            this.STAT_DATE = STAT_DATE;
        }
    }

    static class ScopeClass {
        private String STAT_DATE;
        private String SEC_NAME;
        private String SEC_CODE;
        private Double TOT_VOL;

        private String STAT_DATE2;
        private Double TOT_VOL2;
        private Double ADD_VOL;

        public String getSTAT_DATE2() {
            return STAT_DATE2;
        }

        public void setSTAT_DATE2(String STAT_DATE2) {
            this.STAT_DATE2 = STAT_DATE2;
        }

        public String getSTAT_DATE() {
            return STAT_DATE;
        }

        public void setSTAT_DATE(String STAT_DATE) {
            this.STAT_DATE = STAT_DATE;
        }

        public String getSEC_NAME() {
            return SEC_NAME;
        }

        public void setSEC_NAME(String SEC_NAME) {
            this.SEC_NAME = SEC_NAME;
        }

        public String getSEC_CODE() {
            return SEC_CODE;
        }

        public void setSEC_CODE(String SEC_CODE) {
            this.SEC_CODE = SEC_CODE;
        }

        public Double getTOT_VOL() {
            return TOT_VOL;
        }

        public void setTOT_VOL(Double TOT_VOL) {
            this.TOT_VOL = TOT_VOL;
        }

        public Double getTOT_VOL2() {
            return TOT_VOL2;
        }

        public void setTOT_VOL2(Double TOT_VOL2) {
            this.TOT_VOL2 = TOT_VOL2;
        }

        public Double getADD_VOL() {
            return ADD_VOL;
        }

        public void setADD_VOL(Double ADD_VOL) {
            this.ADD_VOL = ADD_VOL;
        }
    }

}
