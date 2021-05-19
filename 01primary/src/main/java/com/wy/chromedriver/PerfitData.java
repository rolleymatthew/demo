package com.wy.chromedriver;

public class PerfitData {

    private String code;
    /**
     * 报告期
     */
    private String reportdate;

    /**
     * 中文名称
     */
    private String zhName;

    /**
     * 营业总收入
     */
    private String totalOperateIncome;

    /**
     * 营业收入
     */
    private String operateIncome;

    /**
     * 利息收入
     */
    private String InterestIncome;

    /**
     * 营业总成本
     */
    private String totalExpenses;
    /**
     * 营业成本
     */
    private String operatingCost;
    /**
     * 研发费用
     */
    private String RAndDExpenses;
    /**
     * 营业税金及附加
     */
    private String SalesTaxExtra;
    /**
     * 销售费用
     */
    private String sellingExpenses;

    /**
     * 管理费用
     */
    private String managerExpenses;

    /**
     * 财务费用
     */
    private String financialExpenses;

    /**
     * 公允价值变动
     */
    private String fairValue;

    /**
     * 投资收益
     */
    private String investmentIncome;

    /**
     * 其中:对联营企业和合营企业的投资收益
     */
    private String venturesIncome;

    /**
     * 营业利润
     */
    private String operatingProfit;

    /**
     * 加：营业外收入
     */
    private String notOperatingIncome;

    /**
     * 减：营业外支出
     */
    private String notOperatingExpenses;

    /**
     * 利润总额
     */
    private String totalProfit;

    /**
     * 减：所得税费用
     */
    private String incomeTaxExpense;
    /**
     * 净利润
     */
    private String parentNetprofit;

    /**
     * 扣非归母净利润
     */
    private String NetProfitNotParent;

    /**
     * 毛利率、营业利润率、净利率
     */
    private Perfit3UpPercent perfit3UpPercent;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getReportdate() {
        return reportdate;
    }

    public void setReportdate(String reportdate) {
        this.reportdate = reportdate;
    }

    public String getFairValue() {
        return fairValue;
    }

    public void setFairValue(String fairValue) {
        this.fairValue = fairValue;
    }

    public String getInvestmentIncome() {
        return investmentIncome;
    }

    public void setInvestmentIncome(String investmentIncome) {
        this.investmentIncome = investmentIncome;
    }

    public String getVenturesIncome() {
        return venturesIncome;
    }

    public void setVenturesIncome(String venturesIncome) {
        this.venturesIncome = venturesIncome;
    }

    public String getTotalOperateIncome() {
        return totalOperateIncome;
    }

    public void setTotalOperateIncome(String totalOperateIncome) {
        this.totalOperateIncome = totalOperateIncome;
    }

    public String getOperateIncome() {
        return operateIncome;
    }

    public void setOperateIncome(String operateIncome) {
        this.operateIncome = operateIncome;
    }

    public String getInterestIncome() {
        return InterestIncome;
    }

    public void setInterestIncome(String interestIncome) {
        InterestIncome = interestIncome;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public String getOperatingCost() {
        return operatingCost;
    }

    public void setOperatingCost(String operatingCost) {
        this.operatingCost = operatingCost;
    }

    public String getRAndDExpenses() {
        return RAndDExpenses;
    }

    public void setRAndDExpenses(String RAndDExpenses) {
        this.RAndDExpenses = RAndDExpenses;
    }

    public String getSalesTaxExtra() {
        return SalesTaxExtra;
    }

    public void setSalesTaxExtra(String salesTaxExtra) {
        SalesTaxExtra = salesTaxExtra;
    }

    public String getSellingExpenses() {
        return sellingExpenses;
    }

    public void setSellingExpenses(String sellingExpenses) {
        this.sellingExpenses = sellingExpenses;
    }

    public String getManagerExpenses() {
        return managerExpenses;
    }

    public void setManagerExpenses(String managerExpenses) {
        this.managerExpenses = managerExpenses;
    }

    public String getFinancialExpenses() {
        return financialExpenses;
    }

    public void setFinancialExpenses(String financialExpenses) {
        this.financialExpenses = financialExpenses;
    }

    public String getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(String operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public String getNotOperatingIncome() {
        return notOperatingIncome;
    }

    public void setNotOperatingIncome(String notOperatingIncome) {
        this.notOperatingIncome = notOperatingIncome;
    }

    public String getNotOperatingExpenses() {
        return notOperatingExpenses;
    }

    public void setNotOperatingExpenses(String notOperatingExpenses) {
        this.notOperatingExpenses = notOperatingExpenses;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getIncomeTaxExpense() {
        return incomeTaxExpense;
    }

    public void setIncomeTaxExpense(String incomeTaxExpense) {
        this.incomeTaxExpense = incomeTaxExpense;
    }

    public String getParentNetprofit() {
        return parentNetprofit;
    }

    public void setParentNetprofit(String parentNetprofit) {
        this.parentNetprofit = parentNetprofit;
    }

    public String getNetProfitNotParent() {
        return NetProfitNotParent;
    }

    public void setNetProfitNotParent(String netProfitNotParent) {
        NetProfitNotParent = netProfitNotParent;
    }

    public Perfit3UpPercent getPerfit3UpPercent() {
        return perfit3UpPercent;
    }

    public void setPerfit3UpPercent(Perfit3UpPercent perfit3UpPercent) {
        this.perfit3UpPercent = perfit3UpPercent;
    }
}