package feifei.dataanalysis.bean;

public class Order2 {


    String store_id;
    String store_name;
    String order_count_today;
    String order_money_today;
    String order_count_week;
    String order_money_week;
    String order_count_month;
    String order_money_month;

    public Order2() {
    }

    @Override
    public String toString() {
        return "Order2{" +
                "store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", order_count_today='" + order_count_today + '\'' +
                ", order_money_today='" + order_money_today + '\'' +
                ", order_count_week='" + order_count_week + '\'' +
                ", order_money_week='" + order_money_week + '\'' +
                ", order_count_month='" + order_count_month + '\'' +
                ", order_money_month='" + order_money_month + '\'' +
                '}';
    }


    public String getOrder_count_week() {
        return order_count_week;
    }

    public void setOrder_count_week(String order_count_week) {
        this.order_count_week = order_count_week;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getOrder_count_today() {
        return order_count_today;
    }

    public void setOrder_count_today(String order_count_today) {
        this.order_count_today = order_count_today;
    }

    public String getOrder_money_today() {
        return order_money_today;
    }

    public void setOrder_money_today(String order_money_today) {
        this.order_money_today = order_money_today;
    }

    public String getOrder_money_week() {
        return order_money_week;
    }

    public void setOrder_money_week(String order_money_week) {
        this.order_money_week = order_money_week;
    }

    public String getOrder_count_month() {
        return order_count_month;
    }

    public void setOrder_count_month(String order_count_month) {
        this.order_count_month = order_count_month;
    }

    public String getOrder_money_month() {
        return order_money_month;
    }

    public void setOrder_money_month(String order_money_month) {
        this.order_money_month = order_money_month;
    }


}
