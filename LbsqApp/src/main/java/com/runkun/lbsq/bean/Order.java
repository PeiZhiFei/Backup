package com.runkun.lbsq.bean;

/**
 * 我的订单实体类
 */
public class Order
{

    private String add_time;
    private String state;
    private String store_name;
    private String zongjia;
    private String store_pic;

    public String getStore_name ()
    {
        return store_name;
    }

    public void setStore_name (String store_name)
    {
        this.store_name = store_name;
    }

    public String getZongjia ()
    {
        return zongjia;
    }

    public void setZongjia (String zongjia)
    {
        this.zongjia = zongjia;
    }

    public String getStore_pic ()
    {
        return store_pic;
    }

    public void setStore_pic (String store_pic)
    {
        this.store_pic = store_pic;
    }

    public String getOrder_sn ()
    {
        return order_sn;
    }

    public void setOrder_sn (String order_sn)
    {
        this.order_sn = order_sn;
    }

    public String getAdd_time ()
    {

        return add_time;
    }

    public void setAdd_time (String add_time)
    {
        this.add_time = add_time;
    }

    private String order_sn;


    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }


}
