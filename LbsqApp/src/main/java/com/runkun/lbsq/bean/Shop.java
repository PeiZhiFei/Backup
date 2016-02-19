package com.runkun.lbsq.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 15-8-7.
 */
public class Shop implements Parcelable
{

    public static final String SHOPID = "storeId";
    public static final String SHOPNAME = "storeName";
    public static final String SHOPADDRESS = "address";
    public static final String SHOPALISA = "alisa";
    public static final String SHOPDALIVER = "daliver_description";
    public static final String SHOPPHONE = "store_phone";
    public static final String SHOPJFDK = "store_jfdk";
    public static final String SHOPFARE = "store_fare";
    public static final String CLASSID = "classId";

    /**
     * daliver_description : 只限华能路济南留学生创业园周边一公里
     * lon : 117.099156
     * alisa : 1
     * fare : 0
     * store_id : 89
     * pic : http://app.lingbushequ.com/data/upload/shop/store/9b8362cdcdc8d1318e8ae5778755c897.jpg_small.jpg
     * class_id : 24
     * zje : 0
     * address : 济南华能路19号
     * jfdk : 0
     * jf : 0
     * dis : 0.1
     * store_name : 时尚涮吧
     * telephone : 15589965647
     * lat : 36.691361
     */
    private String daliver_description;
    private String lon;
    private String alisa;
    private String fare;
    private String store_id;
    private String pic;
    private String class_id;
    private String zje;
    private String address;
    private String jfdk;
    private String jf;
    private String dis;
    private String store_name;
    private String telephone;
    private String lat;

    public void setDaliver_description (String daliver_description)
    {
        this.daliver_description = daliver_description;
    }

    public void setLon (String lon)
    {
        this.lon = lon;
    }

    public void setAlisa (String alisa)
    {
        this.alisa = alisa;
    }

    public void setFare (String fare)
    {
        this.fare = fare;
    }

    public void setStore_id (String store_id)
    {
        this.store_id = store_id;
    }

    public void setPic (String pic)
    {
        this.pic = pic;
    }

    public void setClass_id (String class_id)
    {
        this.class_id = class_id;
    }

    public void setZje (String zje)
    {
        this.zje = zje;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public void setJfdk (String jfdk)
    {
        this.jfdk = jfdk;
    }

    public void setJf (String jf)
    {
        this.jf = jf;
    }

    public void setDis (String dis)
    {
        this.dis = dis;
    }

    public void setStore_name (String store_name)
    {
        this.store_name = store_name;
    }

    public void setTelephone (String telephone)
    {
        this.telephone = telephone;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    public String getDaliver_description ()
    {
        return daliver_description;
    }

    public String getLon ()
    {
        return lon;
    }

    public String getAlisa ()
    {
        return alisa;
    }

    public String getFare ()
    {
        return fare;
    }

    public String getStore_id ()
    {
        return store_id;
    }

    public String getPic ()
    {
        return pic;
    }

    public String getClass_id ()
    {
        return class_id;
    }

    public String getZje ()
    {
        return zje;
    }

    public String getAddress ()
    {
        return address;
    }

    public String getJfdk ()
    {
        return jfdk;
    }

    public String getJf ()
    {
        return jf;
    }

    public String getDis ()
    {
        return dis;
    }

    public String getStore_name ()
    {
        return store_name;
    }

    public String getTelephone ()
    {
        return telephone;
    }

    public String getLat ()
    {
        return lat;
    }

    @Override
    public int describeContents ()
    {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags)
    {
        dest.writeString (this.daliver_description);
        dest.writeString (this.lon);
        dest.writeString (this.alisa);
        dest.writeString (this.fare);
        dest.writeString (this.store_id);
        dest.writeString (this.pic);
        dest.writeString (this.class_id);
        dest.writeString (this.zje);
        dest.writeString (this.address);
        dest.writeString (this.jfdk);
        dest.writeString (this.jf);
        dest.writeString (this.dis);
        dest.writeString (this.store_name);
        dest.writeString (this.telephone);
        dest.writeString (this.lat);
    }

    public Shop ()
    {
    }

    protected Shop (Parcel in)
    {
        this.daliver_description = in.readString ();
        this.lon = in.readString ();
        this.alisa = in.readString ();
        this.fare = in.readString ();
        this.store_id = in.readString ();
        this.pic = in.readString ();
        this.class_id = in.readString ();
        this.zje = in.readString ();
        this.address = in.readString ();
        this.jfdk = in.readString ();
        this.jf = in.readString ();
        this.dis = in.readString ();
        this.store_name = in.readString ();
        this.telephone = in.readString ();
        this.lat = in.readString ();
    }

    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop> ()
    {
        public Shop createFromParcel (Parcel source)
        {
            return new Shop (source);
        }

        public Shop[] newArray (int size)
        {
            return new Shop[size];
        }
    };
}
