package com.runkun.lbsq.bean;

public class Coupons
{

    /**
     * id : 21
     * amount : 5
     * is_guoqi : 0
     * end_time : 1438916580
     * coupon_id : 16
     * xianzhi : 5
     * type : 1
     * store_name : null
     * is_use : 0
     * member_id : 779
     */
    private String id;
    private String amount;
    private String is_guoqi;
    private String end_time;
    private String coupon_id;
    private String xianzhi;
    private String type;
    private String store_name;
    private String is_use;
    private String member_id;

    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setIs_guoqi(String is_guoqi) {
        this.is_guoqi = is_guoqi;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public void setXianzhi(String xianzhi) {
        this.xianzhi = xianzhi;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getIs_guoqi() {
        return is_guoqi;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getXianzhi()
    {
        if ("0".equals(xianzhi))
        {
            return  amount;
        }
        return xianzhi;
    }

    public String getType() {
        return type;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getIs_use() {
        return is_use;
    }

    public String getMember_id() {
        return member_id;
    }
}
