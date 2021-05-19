package com.wy.chromedriver;

public class Perfit3UpPercent {

    /**
     * 毛利率=(营业收入-销售成本)/营业收入
     */
    private double marginRate;

    /**
     * 营业利润率=(营业收入-销售成本-营业费用)/营业收入
     */
    private double marginOperatingRate;

    /**
     * 净利率=(营业收入-销售成本-营销费用+-营业外收支)/营业收入
     */
    private double netProfitRate;

    public double getMarginRate() {
        return marginRate;
    }

    public void setMarginRate(double marginRate) {
        this.marginRate = marginRate;
    }

    public double getMarginOperatingRate() {
        return marginOperatingRate;
    }

    public void setMarginOperatingRate(double marginOperatingRate) {
        this.marginOperatingRate = marginOperatingRate;
    }

    public double getNetProfitRate() {
        return netProfitRate;
    }

    public void setNetProfitRate(double netProfitRate) {
        this.netProfitRate = netProfitRate;
    }
}
