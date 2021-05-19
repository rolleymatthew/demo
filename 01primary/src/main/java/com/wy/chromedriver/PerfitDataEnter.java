package com.wy.chromedriver;

import java.util.List;

/**
 * @author yunwang
 * @Date 2020-12-06
 */
public class PerfitDataEnter {

    /**
     * 表头
     */
    private List<String> tltleNameList;

    /**
     * 数据
     */
    private List<PerfitData> perfitDataList;

    public List<PerfitData> getPerfitDataList() {
        return perfitDataList;
    }

    public void setPerfitDataList(List<PerfitData> perfitDataList) {
        this.perfitDataList = perfitDataList;
    }

    public List<String> getTltleNameList() {
        return tltleNameList;
    }

    public void setTltleNameList(List<String> tltleNameList) {
        this.tltleNameList = tltleNameList;
    }
}
