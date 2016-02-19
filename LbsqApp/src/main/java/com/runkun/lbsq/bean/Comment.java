package com.runkun.lbsq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment implements Parcelable
{

    private String orderSn;
    private String storePic;
    private String storeId;
    private String memberName;
    private String comment;
    private String addTime;
    private int flowerNum;
    private String goods_id;

    public String getOrderSn ()
    {
        return orderSn;
    }

    public void setOrderSn (String orderSn)
    {
        this.orderSn = orderSn;
    }

    public String getStorePic ()
    {
        return storePic;
    }

    public void setStorePic (String storePic)
    {
        this.storePic = storePic;
    }


    public Comment ()
    {
    }


    public String getStoreId ()
    {
        return storeId;
    }

    public void setStoreId (String storeId)
    {
        this.storeId = storeId;
    }

    public String getMemberName ()
    {
        return memberName;
    }

    public void setMemberName (String memberName)
    {
        this.memberName = memberName;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public String getAddTime ()
    {
        return addTime;
    }

    public void setAddTime (String addTime)
    {
        this.addTime = Tools.formatMysqlTimestamp (addTime, "yyyy-MM-dd");
    }

    public int getFlowerNum ()
    {
        return flowerNum;
    }

    public void setFlowerNum (int flowerNum)
    {
        this.flowerNum = flowerNum;
    }

    public static Comment from (JSONObject jo) throws JSONException
    {
        Comment result = new Comment ();
//        result.setOrderSn (jo.getString ("order_sn"));
//        result.setStorePic (jo.getString ("store_pic"));
        result.setStoreId (jo.getString ("store_id"));
        result.setMemberName (jo.getString ("member_name"));
        result.setComment (jo.getString ("comment"));
        result.setFlowerNum (jo.getInt ("flower_num"));
        result.setAddTime (jo.getString ("add_time"));
        return result;
    }

    public String getGoods_id ()
    {
        return goods_id;
    }

    public void setGoods_id (String goods_id)
    {
        this.goods_id = goods_id;
    }


    @Override
    public int describeContents ()
    {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags)
    {
        dest.writeString (this.orderSn);
        dest.writeString (this.storePic);
        dest.writeString (this.storeId);
        dest.writeString (this.memberName);
        dest.writeString (this.comment);
        dest.writeString (this.addTime);
        dest.writeInt (this.flowerNum);
        dest.writeString (this.goods_id);
    }

    protected Comment (Parcel in)
    {
        this.orderSn = in.readString ();
        this.storePic = in.readString ();
        this.storeId = in.readString ();
        this.memberName = in.readString ();
        this.comment = in.readString ();
        this.addTime = in.readString ();
        this.flowerNum = in.readInt ();
        this.goods_id = in.readString ();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment> ()
    {
        public Comment createFromParcel (Parcel source)
        {
            return new Comment (source);
        }

        public Comment[] newArray (int size)
        {
            return new Comment[size];
        }
    };
}