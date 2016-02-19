package com.runkun.lbsq.bean;

public class Good
{
    private String storeId;
    private String goodsId;
    private String goodsName;
    private String goodsPrice;
    private String goodsOrPrice;
    private String goodsPic;

    public String getGoodsOrPrice ()
    {
        return goodsOrPrice;
    }

    public void setGoodsOrPrice (String goodsOrPrice)
    {
        this.goodsOrPrice = goodsOrPrice;
    }

    public void setStoreId (String storeId)
    {
        this.storeId = storeId;
    }

    public String getStoreId ()
    {
        return storeId;
    }

    public void setGoodsId (String goodsId)
    {
        this.goodsId = goodsId;
    }

    public String getGoodsId ()
    {
        return goodsId;
    }

    public void setGoodsName (String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName ()
    {
        return goodsName;
    }

    public void setGoodsPrice (String goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPrice ()
    {
        return goodsPrice;
    }

    public void setGoodsPic (String goodsPic)
    {
        this.goodsPic = goodsPic;
    }

    public String getGoodsPic ()
    {
        return goodsPic;
    }
}
