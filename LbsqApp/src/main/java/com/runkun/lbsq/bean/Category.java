package com.runkun.lbsq.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/15.
 */
public class Category
{

    /**
     * class_name : 调味品
     * good_data : [{"goods_id":"45557","goods_name":"明泉宝三年老陈醋","goods_price":"10.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/c9b814529722de7656b86f42718986ee.jpg"},{"goods_id":"45558","goods_name":"明泉宝两年陈醋","goods_price":"6.60","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/25a8a93aa869bf3ede421d2b51e32098.jpg"},{"goods_id":"45560","goods_name":"欣和甜面酱400g","goods_price":"3.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/b3984b285756f8bc48a3ca4482fed759.jpg"},{"goods_id":"45561","goods_name":"葱伴侣豆瓣酱400g","goods_price":"2.80","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/f8459e3db4cd1eded9e0a90c247862b1.jpg"},{"goods_id":"45562","goods_name":"葱伴侣六月香豆瓣酱","goods_price":"11.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/e90d0c5d10c8f7e6ef823c074b4a752b.jpg"},{"goods_id":"45563","goods_name":"葱伴侣六月香豆瓣酱300g","goods_price":"6.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/5d6cb29cd90cc79af8b1b6f0eb8f35fc.jpg"},{"goods_id":"45564","goods_name":"来福山西陈醋300ml","goods_price":"17.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/img1441179333.png"},{"goods_id":"45565","goods_name":"威极老陈醋800ml","goods_price":"7.10","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/d2efff2e2726e43fbf825c23f9e80183.jpg"},{"goods_id":"45566","goods_name":"海天酱油老抽王800ml","goods_price":"7.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/70aead056f86d0c634502f85aab183d8.jpg"},{"goods_id":"45567","goods_name":"海天酱油鲜味生抽王800ml","goods_price":"6.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/4b7c01078ccd82753f58011beba7a352.jpg"},{"goods_id":"45568","goods_name":"海天350ML黄豆","goods_price":"2.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/a354abca606edc2bb9db073e257cde65.jpg"},{"goods_id":"45569","goods_name":"海天鲜味生抽王500ml","goods_price":"5.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/1cd779e0a8e028d927ae2fbc232090f2.jpg"},{"goods_id":"45570","goods_name":"海天上等蚝油700g","goods_price":"6.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/b6944c694a49b3b79a6534ea4a8f6cb2.jpg"},{"goods_id":"45571","goods_name":"味达美1L","goods_price":"12.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/4833b94ce981de5c3a5d605f3d504f8c.jpg"},{"goods_id":"45572","goods_name":"利民蒜蓉辣酱","goods_price":"1.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/04ba516319b117179715f39aa39f8a50.jpg"},{"goods_id":"45573","goods_name":"利民蒜蓉辣酱450g","goods_price":"5.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/5617c47b08c151486f061dac1cf0950c.jpg"},{"goods_id":"45574","goods_name":"巧媳妇清香米醋1.3L","goods_price":"10.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/cb602af5a30930094131aea6f87c9b66.jpg"},{"goods_id":"45575","goods_name":"巧媳妇原汁酱油1.3L","goods_price":"11.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/c67b5dbe9b9a3f92457f1d417b6f341b.jpg"},{"goods_id":"45576","goods_name":"巧媳妇甜面酱","goods_price":"3.50","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/796c1050dfe258c0161a4a8be213e303.jpg"},{"goods_id":"45577","goods_name":"德馨斋原汁酱油300ml","goods_price":"2.00","goods_pic":"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/2762bdbb5695208e62b6f27815a7092b.jpg"}]
     */

    private String class_name;
    private List<GoodDataEntity> good_data;

    public void setClass_name(String class_name)
    {
        this.class_name = class_name;
    }

    public void setGood_data(List<GoodDataEntity> good_data)
    {
        this.good_data = good_data;
    }

    public String getClass_name()
    {
        return class_name;
    }

    public List<GoodDataEntity> getGood_data()
    {
        return good_data;
    }


}

