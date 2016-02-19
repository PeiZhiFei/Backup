package com.runkun.lbsq.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodMore implements Parcelable
{

    private String storeId;
    private String storeName;
    private String goodsId;
    private String goodsName;
    private String goodsPrice;
    private String goodsTejiaPrice;

    private String goodsOldPrice;
    private String goodsPic;

    private String className;
    private String other;

    public GoodMore ()
    {
    }

    public GoodMore (String storeId, String goodsId, String goodsName,
                     String goodsPrice, String goodsPic, String className)
    {
        this.storeId = storeId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsPic = goodsPic;
        this.className = className;
    }

    public String getStoreName ()
    {
        return storeName;
    }

    public void setStoreName (String storeName)
    {
        this.storeName = storeName;
    }

    public String getOther ()
    {
        return other;
    }

    public void setOther (String other)
    {
        this.other = other;
    }

    public String getGoodsTejiaPrice ()
    {
        return goodsTejiaPrice;
    }

    public void setGoodsTejiaPrice (String goodsTejiaPrice)
    {
        this.goodsTejiaPrice = goodsTejiaPrice;
    }

    public String getClassName ()
    {
        return className;
    }

    public void setClassName (String className)
    {
        this.className = className;
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

    public String getGoodsOldPrice ()
    {
        return goodsOldPrice;
    }

    public void setGoodsOldPrice (String goodsOldPrice)
    {
        this.goodsOldPrice = goodsOldPrice;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString (this.storeId);
        dest.writeString (this.storeName);
        dest.writeString (this.goodsId);
        dest.writeString (this.goodsName);
        dest.writeString (this.goodsPrice);
        dest.writeString (this.goodsTejiaPrice);
        dest.writeString (this.goodsOldPrice);
        dest.writeString (this.goodsPic);
        dest.writeString (this.className);
        dest.writeString (this.other);
    }

    protected GoodMore (Parcel in)
    {
        this.storeId = in.readString ();
        this.storeName = in.readString ();
        this.goodsId = in.readString ();
        this.goodsName = in.readString ();
        this.goodsPrice = in.readString ();
        this.goodsTejiaPrice = in.readString ();
        this.goodsOldPrice = in.readString ();
        this.goodsPic = in.readString ();
        this.className = in.readString ();
        this.other = in.readString ();
    }

    public static final Parcelable.Creator<GoodMore> CREATOR = new Parcelable.Creator<GoodMore> ()
    {
        public GoodMore createFromParcel(Parcel source)
        {
            return new GoodMore (source);
        }

        public GoodMore[] newArray(int size)
        {
            return new GoodMore[size];
        }
    };
}
