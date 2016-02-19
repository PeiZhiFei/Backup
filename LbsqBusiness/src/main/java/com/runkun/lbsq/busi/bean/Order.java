package com.runkun.lbsq.busi.bean;

/**
 * 我的订单实体类
 */
public class Order
{
    private String countorder;
    private String totalorder;
    private String order_time;
    private String order_time_start;
    private String order_time_end;

    public Order()
    {
    }

    public String getOrder_time_start()
    {
        return order_time_start;
    }

    public void setOrder_time_start(String order_time_start)
    {
        this.order_time_start = order_time_start;
    }

    public String getOrder_time_end()
    {
        return order_time_end;
    }

    public void setOrder_time_end(String order_time_end)
    {
        this.order_time_end = order_time_end;
    }


    public String getCountorder()
    {
        return countorder;
    }

    public void setCountorder(String countorder)
    {
        this.countorder = countorder;
    }

    public String getTotalorder()
    {
        return totalorder;
    }

    public void setTotalorder(String totalorder)
    {
        this.totalorder = totalorder;
    }

    public String getOrder_time()
    {
        return order_time;
    }

    public void setOrder_time(String order_time)
    {
        this.order_time = order_time;
    }


}
