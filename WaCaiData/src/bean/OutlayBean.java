package bean;

/**
 * 支出
 */
public class OutlayBean {

    /**
     * 支出大类
     */
    private String categoryParent;

    /**
     * 支出小类
     */
    private String categoryChild;

    /**
     * 账户
     */
    private String account;

    /**
     * 币种
     */
    private String currency;

    /**
     * 项目
     */
    private String project;

    /**
     * 商家
     */
    private String seller;

    /**
     * 报销
     */
    private String repay;

    /**
     * 消费日期
     */
    private String date;

    /**
     * 消费金额
     */
    private String price;

    /**
     * 成员金额
     */
    private String priceMember;

    /**
     * 备注
     */
    private String remark;

    /**
     * 账本
     */
    private String accountBook;

    @Override
    public String toString() {
        return "{" +
                "categoryParent='" + categoryParent + '\'' +
                ", categoryChild='" + categoryChild + '\'' +
                ", account='" + account + '\'' +
                ", currency='" + currency + '\'' +
                ", project='" + project + '\'' +
                ", seller='" + seller + '\'' +
                ", repay='" + repay + '\'' +
                ", date='" + date + '\'' +
                ", price='" + price + '\'' +
                ", priceMember='" + priceMember + '\'' +
                ", remark='" + remark + '\'' +
                ", accountBook='" + accountBook + '\'' +
                '}';
    }

    public String getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent;
    }

    public String getCategoryChild() {
        return categoryChild;
    }

    public void setCategoryChild(String categoryChild) {
        this.categoryChild = categoryChild;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getRepay() {
        return repay;
    }

    public void setRepay(String repay) {
        this.repay = repay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceMember() {
        return priceMember;
    }

    public void setPriceMember(String priceMember) {
        this.priceMember = priceMember;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccountBook() {
        return accountBook;
    }

    public void setAccountBook(String accountBook) {
        this.accountBook = accountBook;
    }
}