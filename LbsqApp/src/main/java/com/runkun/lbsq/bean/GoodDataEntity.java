package com.runkun.lbsq.bean;

/**
 * Created by Administrator on 2015/9/17.
 */
public class GoodDataEntity
{
    /**
     * goods_id : 45557
     * goods_name : 明泉宝三年老陈醋
     * goods_price : 10.50
     * goods_pic : http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/c9b814529722de7656b86f42718986ee.jpg
     */

    private String goods_id;
    private String goods_name;
    private String goods_price;
    private String goods_pic;
    private String parent_class_name;

    public String getParent_class_name()
    {
        return parent_class_name;
    }

    public void setParent_class_name(String parent_class_name)
    {
        this.parent_class_name = parent_class_name;
    }

    public void setGoods_id(String goods_id)
    {
        this.goods_id = goods_id;
    }

    public void setGoods_name(String goods_name)
    {
        this.goods_name = goods_name;
    }

    public void setGoods_price(String goods_price)
    {
        this.goods_price = goods_price;
    }

    public void setGoods_pic(String goods_pic)
    {
        this.goods_pic = goods_pic;
    }

    public String getGoods_id()
    {
        return goods_id;
    }

    public String getGoods_name()
    {
        return goods_name;
    }

    public String getGoods_price()
    {
        return goods_price;
    }

    public String getGoods_pic()
    {
        return goods_pic;
    }
}
