package bean;

public class PosBean {

    private String date;
    private String time;
    private String card;
    private String type;
    private String moneyPay;
    private String moneyEarn;
    private String moneyTax;
    private String moneyRate;
    private String moneySum;
    private String remark;

    @Override
    public String toString() {
        return "PosBean{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", card='" + card + '\'' +
                ", type='" + type + '\'' +
                ", moneyPay='" + moneyPay + '\'' +
                ", moneyEarn='" + moneyEarn + '\'' +
                ", moneyTax='" + moneyTax + '\'' +
                ", moneyRate='" + moneyRate + '\'' +
                ", moneySum='" + moneySum + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoneyPay() {
        return moneyPay;
    }

    public void setMoneyPay(String moneyPay) {
        this.moneyPay = moneyPay;
    }

    public String getMoneyEarn() {
        return moneyEarn;
    }

    public void setMoneyEarn(String moneyEarn) {
        this.moneyEarn = moneyEarn;
    }

    public String getMoneyTax() {
        return moneyTax;
    }

    public void setMoneyTax(String moneyTax) {
        this.moneyTax = moneyTax;
    }

    public String getMoneyRate() {
        return moneyRate;
    }

    public void setMoneyRate(String moneyRate) {
        this.moneyRate = moneyRate;
    }

    public String getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(String moneySum) {
        this.moneySum = moneySum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
