package bean;

public class BillBean {

    private String date;
    private String time;
    private String money;

    @Override
    public String toString() {
        return "BillBean{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", money='" + money + '\'' +
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
