package com.runkun.lbsq.busi.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/8/31.
 */
public class OrderMore
{
    /**
     * consigner : 豆豆
     * number : 1.00
     * fare : 0
     * conmobile : 18678786386
     * member_address : 21#1#1702
     * price : 7.00
     * total_fee : 26.6
     * item_name : 不二家棒棒糖8只50G清爽 型
     * state : 3
     * add_time : 1440680497
     * goodsinfo : [{"number":"1.00","price":"7.00","name":"不二家棒棒糖8只50G清爽 型"},{"number":"4.00","price":"2.00","name":"南孚电池5号"},{"number":"1.00","price":"5.00","name":"马桶刷"},{"number":"3.00","price":"2.20","name":"康师傅珍品袋面（鲜虾鱼板）"}]
     * order_sn : 201508271100489760331482
     */
    private String consigner;
    private String number;
    private String fare;
    private String conmobile;
    private String member_address;
    private String price;
    private String total_fee;
    private String item_name;
    private String state;
    private String add_time;
    private List<GoodsinfoEntity> goodsinfo;
    private String order_sn;

    public void setConsigner(String consigner)
    {
        this.consigner = consigner;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public void setFare(String fare)
    {
        this.fare = fare;
    }

    public void setConmobile(String conmobile)
    {
        this.conmobile = conmobile;
    }

    public void setMember_address(String member_address)
    {
        this.member_address = member_address;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public void setTotal_fee(String total_fee)
    {
        this.total_fee = total_fee;
    }

    public void setItem_name(String item_name)
    {
        this.item_name = item_name;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setAdd_time(String add_time)
    {
        this.add_time = add_time;
    }

    public void setGoodsinfo(List<GoodsinfoEntity> goodsinfo)
    {
        this.goodsinfo = goodsinfo;
    }

    public void setOrder_sn(String order_sn)
    {
        this.order_sn = order_sn;
    }

    public String getConsigner()
    {
        return consigner;
    }

    public String getNumber()
    {
        return number;
    }

    public String getFare()
    {
        return fare;
    }

    public String getConmobile()
    {
        return conmobile;
    }

    public String getMember_address()
    {
        return member_address;
    }

    public String getPrice()
    {
        return price;
    }

    public String getTotal_fee()
    {
        return total_fee;
    }

    public String getItem_name()
    {
        return item_name;
    }

    public String getState()
    {
        return state;
    }

    public String getAdd_time()
    {
        return add_time;
    }

    public List<GoodsinfoEntity> getGoodsinfo()
    {
        return goodsinfo;
    }

    public String getOrder_sn()
    {
        return order_sn;
    }

    public class GoodsinfoEntity
    {
        /**
         * number : 1.00
         * price : 7.00
         * name : 不二家棒棒糖8只50G清爽 型
         */
        private String number;
        private String price;
        private String name;

        public void setNumber(String number)
        {
            this.number = number;
        }

        public void setPrice(String price)
        {
            this.price = price;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getNumber()
        {
            return number;
        }

        public String getPrice()
        {
            return price;
        }

        public String getName()
        {
            return name;
        }
    }
}
