package com.runkun.lbsq.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    /**
     * id : 2364
     * isdefault : 1
     * remark :
     * address :
     * consigner :
     * mobile : 15665797058
     * member_id : 943
     */
    private String id;
    private boolean isdefault;
    private String remark;
    private String address;
    private String consigner;
    private String mobile;
    private String member_id;

    public void setId(String id) {
        this.id = id;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setConsigner(String consigner) {
        this.consigner = consigner;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getId() {
        return id;
    }

    public boolean getIsdefault() {
        return isdefault;
    }

    public String getRemark() {
        return remark;
    }

    public String getAddress() {
        return address;
    }

    public String getConsigner() {
        return consigner;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMember_id() {
        return member_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(isdefault ? (byte) 1 : (byte) 0);
        dest.writeString(this.remark);
        dest.writeString(this.address);
        dest.writeString(this.consigner);
        dest.writeString(this.mobile);
        dest.writeString(this.member_id);
    }

    public Address () {
    }

    protected Address (Parcel in) {
        this.id = in.readString();
        this.isdefault = in.readByte() != 0;
        this.remark = in.readString();
        this.address = in.readString();
        this.consigner = in.readString();
        this.mobile = in.readString();
        this.member_id = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        public Address createFromParcel(Parcel source) {
            return new Address (source);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
