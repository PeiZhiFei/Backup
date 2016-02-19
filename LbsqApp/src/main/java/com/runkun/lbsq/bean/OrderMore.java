package com.runkun.lbsq.bean;

import com.runkun.lbsq.utils.Tools;

import java.util.List;

public class OrderMore
{
    /**
     * fare : 0
     * send_type : null
     * cost_score : 0
     * consigner : 李守迎
     * number : 1.00
     * trade_sn : T2015072397995555
     * conremark : 神烦狗不纠结唱歌好好计划
     * goods_list : [{"number":"1.00","item_id":"1591","price":"0.01","total_fee":"0.01","item_name":"醒目苹果330ml"}]
     * price : 0.01
     * total_fee : 0.01
     * store_name : 测试零步社区
     * pay_type : 1
     * state : 2
     * order_type : 2
     * member_id : 887
     * store_id : 29
     * hongbao : 0
     * item_id : 1591
     * use_time : 1437641422
     * member_address : 济南话能量源
     * mobile : 15628834660
     * item_name : 醒目苹果330ml
     * member_name : 15628834660
     * order_out : 2015072397995555
     * conmobile : 15628834660
     * comment : null
     * order_id : 8756
     * add_time : 1437641401
     * order_sn : 2015072397995555
     */
    private String fare;
    private String send_type;
    private String cost_score;
    private String consigner;
    private String number;
    private String trade_sn;
    private String conremark;
    private List<GoodsListEntity> goods_list;
    private CommentsEntity comments;
    private String price;
    private String total_fee;
    private String store_name;
    private String pay_type;
    private String state;
    private String order_type;
    private String member_id;
    private String store_id;

    public class CommentsEntity
    {
        /**
         * flower_num : 3
         * comment : 规划好
         * add_time : 1437817878
         */
        private int flower_num;
        private String comment;
        private String add_time;

        public void setFlower_num (int flower_num)
        {
            this.flower_num = flower_num;
        }

        public void setComment (String comment)
        {
            this.comment = comment;
        }

        public void setAdd_time (String add_time)
        {
            this.add_time = Tools.formatMysqlTimestamp (add_time, "yyyy-MM-dd");
        }

        public int getFlower_num ()
        {
            return flower_num;
        }

        public String getComment ()
        {
            return comment;
        }

        public String getAdd_time ()
        {
            return add_time;
        }
    }

    public String getStore_pic ()
    {
        return store_pic;
    }

    public void setStore_pic (String store_pic)
    {
        this.store_pic = store_pic;
    }

    private String store_pic;
    private String hongbao;
    private String item_id;
    private String use_time;
    private String member_address;
    private String mobile;
    private String item_name;
    private String member_name;
    private String order_out;
    private String conmobile;
    private String comment;
    private String order_id;
    private String add_time;
    private String order_sn;

    private String couponamount;

    public void setFare (String fare)
    {
        this.fare = fare;
    }

    public void setSend_type (String send_type)
    {
        this.send_type = send_type;
    }

    public void setCost_score (String cost_score)
    {
        this.cost_score = cost_score;
    }

    public void setConsigner (String consigner)
    {
        this.consigner = consigner;
    }

    public void setNumber (String number)
    {
        this.number = number;
    }

    public void setTrade_sn (String trade_sn)
    {
        this.trade_sn = trade_sn;
    }

    public void setConremark (String conremark)
    {
        this.conremark = conremark;
    }

    public void setGoods_list (List<GoodsListEntity> goods_list)
    {
        this.goods_list = goods_list;
    }

    public void setComments (CommentsEntity comments)
    {
        this.comments = comments;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public void setTotal_fee (String total_fee)
    {
        this.total_fee = total_fee;
    }

    public void setStore_name (String store_name)
    {
        this.store_name = store_name;
    }

    public void setPay_type (String pay_type)
    {
        this.pay_type = pay_type;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public void setOrder_type (String order_type)
    {
        this.order_type = order_type;
    }

    public void setMember_id (String member_id)
    {
        this.member_id = member_id;
    }

    public void setStore_id (String store_id)
    {
        this.store_id = store_id;
    }

    public void setHongbao (String hongbao)
    {
        this.hongbao = hongbao;
    }

    public void setCouponamount (String couponamount)
    {
        this.couponamount = couponamount;
    }

    public void setItem_id (String item_id)
    {
        this.item_id = item_id;
    }

    public void setUse_time (String use_time)
    {
        this.use_time = use_time;
    }

    public void setMember_address (String member_address)
    {
        this.member_address = member_address;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public void setItem_name (String item_name)
    {
        this.item_name = item_name;
    }

    public void setMember_name (String member_name)
    {
        this.member_name = member_name;
    }

    public void setOrder_out (String order_out)
    {
        this.order_out = order_out;
    }

    public void setConmobile (String conmobile)
    {
        this.conmobile = conmobile;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public void setOrder_id (String order_id)
    {
        this.order_id = order_id;
    }

    public void setAdd_time (String add_time)
    {
        this.add_time = add_time;
    }

    public void setOrder_sn (String order_sn)
    {
        this.order_sn = order_sn;
    }

    public String getFare ()
    {
        return fare;
    }

    public String getSend_type ()
    {
        return send_type;
    }

    public String getCost_score ()
    {
        return cost_score;
    }

    public String getConsigner ()
    {
        return consigner;
    }

    public String getNumber ()
    {
        return number;
    }

    public String getTrade_sn ()
    {
        return trade_sn;
    }

    public String getConremark ()
    {
        return conremark;
    }

    public List<GoodsListEntity> getGoods_list ()
    {
        return goods_list;
    }

    public CommentsEntity getComments ()
    {
        return comments;
    }

    public String getPrice ()
    {
        return price;
    }

    public String getTotal_fee ()
    {
        return total_fee;
    }

    public String getStore_name ()
    {
        return store_name;
    }

    public String getPay_type ()
    {
        return pay_type;
    }

    public String getState ()
    {
        return state;
    }

    public String getOrder_type ()
    {
        return order_type;
    }

    public String getMember_id ()
    {
        return member_id;
    }

    public String getStore_id ()
    {
        return store_id;
    }

    public String getHongbao ()
    {
        return hongbao;
    }

    public String getCouponamount ()
    {
        return couponamount;
    }

    public String getItem_id ()
    {
        return item_id;
    }

    public String getUse_time ()
    {
        return use_time;
    }

    public String getMember_address ()
    {
        return member_address;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public String getItem_name ()
    {
        return item_name;
    }

    public String getMember_name ()
    {
        return member_name;
    }

    public String getOrder_out ()
    {
        return order_out;
    }

    public String getConmobile ()
    {
        return conmobile;
    }

    public String getComment ()
    {
        return comment;
    }

    public String getOrder_id ()
    {
        return order_id;
    }

    public String getAdd_time ()
    {
        return add_time;
    }

    public String getOrder_sn ()
    {
        return order_sn;
    }


    public class GoodsListEntity
    {
        /**
         * number : 1.00
         * item_id : 1591
         * price : 0.01
         * total_fee : 0.01
         * item_name : 醒目苹果330ml
         */
        private String number;
        private String item_id;
        private String price;
        private String total_fee;
        private String item_name;

        public void setNumber (String number)
        {
            this.number = number;
        }

        public void setItem_id (String item_id)
        {
            this.item_id = item_id;
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public void setTotal_fee (String total_fee)
        {
            this.total_fee = total_fee;
        }

        public void setItem_name (String item_name)
        {
            this.item_name = item_name;
        }

        public String getNumber ()
        {
            return number;
        }

        public String getItem_id ()
        {
            return item_id;
        }

        public String getPrice ()
        {
            return price;
        }

        public String getTotal_fee ()
        {
            return total_fee;
        }

        public String getItem_name ()
        {
            return item_name;
        }
    }
}
