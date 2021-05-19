package com.wy.chromedriver;

import com.wy.utils.FilesUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneratePerfit3Data {
    public static void main(String[] args) {
        String dir = "d:\\profit";
        String charset = "GBK";
        List<String> fileNameList = FilesUtil.getFilesOfDictory(dir);
        StringBuilder stringBuilderOneQuarter = new StringBuilder();
        StringBuilder stringBuilderTwoQuarter = new StringBuilder();
        StringBuilder stringBuilderThreeQuarter = new StringBuilder();
        for (String s : fileNameList) {
            //单个制定文件
//            if (!StringUtils.equals("600519.csv",s)){
//                continue;
//            }
            //计算表头和内容不一致的数据
            if (!isCorrect(dir, charset, s)){
                System.out.println(s);
                continue;
            }
            //数量不是23的
//            if (isNotNum(dir, charset, s)) {
//                System.out.println(s);
//            }
            //单个文件格式化数据到Bean数组
            List<Perfit3UpPercent> perfit3UpPercentList = getBeanList(dir, charset, s);
            boolean onePerIsUp = isUp3PerOneQuarter(perfit3UpPercentList);
            //计算最近一个季度三率三升
            if (onePerIsUp) {
                stringBuilderOneQuarter.append("'" + StringUtils.replace(s, ".csv", "") + "\n");
            }
            //计算最近2个季度三率三升
            boolean twoPerIsUp = isUp3PerTwoQuarter(perfit3UpPercentList);
            if (onePerIsUp && twoPerIsUp) {
                stringBuilderTwoQuarter.append("'" + StringUtils.replace(s, ".csv", "") + "\n");
            }
            //计算最近3个季度三率三升
            boolean threePerIsUp = isUp3PerThreQuarter(perfit3UpPercentList);
            if (onePerIsUp && twoPerIsUp && threePerIsUp) {
                stringBuilderThreeQuarter.append("'" + StringUtils.replace(s, ".csv", "") + "\n");
            }
        }
        try {
            FilesUtil.writeFile(dir, "OneQuarter.csv", stringBuilderOneQuarter.toString());
            FilesUtil.writeFile(dir, "TwoQuarter.csv", stringBuilderTwoQuarter.toString());
            FilesUtil.writeFile(dir, "TheeQuarter.csv", stringBuilderThreeQuarter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isNotNum(String dir, String charset, String s) {
        try {
            List<String> fileContent = FilesUtil.readFileAsListOfStrings(dir + "\\" + s, charset);
            for (String s1 : fileContent) {
                String[] ads = s1.split(",");
                System.out.println(s+","+ads.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isCorrect(String dir, String charset, String s) {
        try {
            List<String> fileContent = FilesUtil.readFileAsListOfStrings(dir + "\\" + s, charset);
            int titleLenght = 0;
            int contentLenght = 0;
            for (int i = 0; i < fileContent.size(); i++) {
                if (i == 0) {
                    String[] string = fileContent.get(i).split(",");
                    titleLenght = string.length;
                }
                if (i > 0) {
                    String[] string = fileContent.get(i).split(",");
                    contentLenght = string.length;
                    break;
                }
            }

            //表头和内容一致
            if (titleLenght == contentLenght) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isUp3PerThreQuarter(List<Perfit3UpPercent> perfit3UpPercentList) {
        if (perfit3UpPercentList.size() >= 5) {
            Perfit3UpPercent curPerfit3UpPercent = perfit3UpPercentList.get(2);
            Perfit3UpPercent lastPerfit3UpPercent = perfit3UpPercentList.get(3);
            if (curPerfit3UpPercent.getNetProfitRate() > lastPerfit3UpPercent.getNetProfitRate()
                    && curPerfit3UpPercent.getMarginOperatingRate() > lastPerfit3UpPercent.getMarginOperatingRate()
                    && curPerfit3UpPercent.getMarginRate() > lastPerfit3UpPercent.getMarginRate()
                    && curPerfit3UpPercent.getMarginOperatingRate() > 0
                    && curPerfit3UpPercent.getNetProfitRate() > 0
                    && curPerfit3UpPercent.getMarginRate() > 0
                    && lastPerfit3UpPercent.getMarginOperatingRate() > 0
                    && lastPerfit3UpPercent.getNetProfitRate() > 0
                    && lastPerfit3UpPercent.getMarginRate() > 0
            ) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUp3PerTwoQuarter(List<Perfit3UpPercent> perfit3UpPercentList) {
        if (perfit3UpPercentList.size() >= 5) {
            Perfit3UpPercent curPerfit3UpPercent = perfit3UpPercentList.get(1);
            Perfit3UpPercent lastPerfit3UpPercent = perfit3UpPercentList.get(2);
            if (curPerfit3UpPercent.getNetProfitRate() > lastPerfit3UpPercent.getNetProfitRate()
                    && curPerfit3UpPercent.getMarginOperatingRate() > lastPerfit3UpPercent.getMarginOperatingRate()
                    && curPerfit3UpPercent.getMarginRate() > lastPerfit3UpPercent.getMarginRate()
                    && curPerfit3UpPercent.getMarginOperatingRate() > 0
                    && curPerfit3UpPercent.getNetProfitRate() > 0
                    && curPerfit3UpPercent.getMarginRate() > 0
                    && lastPerfit3UpPercent.getMarginOperatingRate() > 0
                    && lastPerfit3UpPercent.getNetProfitRate() > 0
                    && lastPerfit3UpPercent.getMarginRate() > 0
            ) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUp3PerOneQuarter(List<Perfit3UpPercent> perfit3UpPercentList) {
        if (perfit3UpPercentList.size() >= 5) {
            Perfit3UpPercent curPerfit3UpPercent = perfit3UpPercentList.get(0);
            Perfit3UpPercent lastPerfit3UpPercent = perfit3UpPercentList.get(1);
            if (curPerfit3UpPercent.getNetProfitRate() > lastPerfit3UpPercent.getNetProfitRate()
                    && curPerfit3UpPercent.getMarginOperatingRate() > lastPerfit3UpPercent.getMarginOperatingRate()
                    && curPerfit3UpPercent.getMarginRate() > lastPerfit3UpPercent.getMarginRate()
                    && curPerfit3UpPercent.getMarginOperatingRate() > 15
                    && curPerfit3UpPercent.getNetProfitRate() > 5
                    && curPerfit3UpPercent.getMarginRate() > 30
                    && lastPerfit3UpPercent.getMarginOperatingRate() > 0
                    && lastPerfit3UpPercent.getNetProfitRate() > 5
                    && lastPerfit3UpPercent.getMarginRate() > 30
            ) {
                return true;
            }
        }
        return false;
    }

    private static List<Perfit3UpPercent> getBeanList(String dir, String charset, String s) {
        List<Perfit3UpPercent> perfit3UpPercentList = new ArrayList<Perfit3UpPercent>();
        try {
            List<String> fileContent = FilesUtil.readFileAsListOfStrings(dir + "\\" + s, charset);
            HashMap<String, Integer> itemMap = new HashMap<>();
            Integer location1 = -1;
            Integer location2 = -1;
            Integer location3 = -1;
            for (int i = 0; i < fileContent.size(); i++) {
                Perfit3UpPercent perfit3UpPercent = new Perfit3UpPercent();
                String content = fileContent.get(i);
                String[] items = content.split(",");
                if (itemMap.containsKey("毛利率")) {
                    location1 = itemMap.get("毛利率");
                }
                if (itemMap.containsKey("营业利润率")) {
                    location2 = itemMap.get("营业利润率");
                }
                if (itemMap.containsKey("净利率")) {
                    location3 = itemMap.get("净利率");
                }
                if (i == 0) {
                    for (int j = 0; j < items.length; j++) {
                        itemMap.put(items[j], j);
                    }
                } else {
                    for (int j = 0; j < items.length; j++) {
                        if (j == location1) {
                            perfit3UpPercent.setMarginRate(getDoubleValue(items[j]));
                        }
                        if (j == location2) {
                            perfit3UpPercent.setMarginOperatingRate(getDoubleValue(items[j]));
                        }
                        if (j == location3) {
                            perfit3UpPercent.setNetProfitRate(getDoubleValue(items[j]));
                        }
                    }
                    perfit3UpPercentList.add(perfit3UpPercent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return perfit3UpPercentList;
    }

    private static double getDoubleValue(String item) {
        if (StringUtils.isEmpty(item)) {
            return 0;
        }
        Double value = 0.0;
        try {
            value = Double.parseDouble(item);
        } catch (NumberFormatException e) {

        }
        return value;
    }
}
