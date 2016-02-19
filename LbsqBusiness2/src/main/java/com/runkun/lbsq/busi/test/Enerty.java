package com.runkun.lbsq.busi.test;

import android.os.Parcel;
import android.os.Parcelable;

public class Enerty implements Parcelable {
	String name;
	String address;
	double lat;// 纬度
	double lon;// 经度
	String person;
	String phone;
	String type;

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Enerty(String name, String address, double lat, double lon) {
		this.address = address;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "Enerty{" + "name='" + name + '\'' + ", address='" + address
				+ '\'' + ", lat=" + lat + ", lon=" + lon + '}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

		parcel.writeString(name);
		parcel.writeString(address);
		parcel.writeDouble(lat);
		parcel.writeDouble(lon);
		parcel.writeString(person);
		parcel.writeString(phone);
		parcel.writeString(type);

	}

	public static final Parcelable.Creator<Enerty> CREATOR = new Parcelable.Creator<Enerty>() {

		@Override
		public Enerty createFromParcel(Parcel parcel) {
			Enerty enerty = new Enerty(parcel.readString(),
					parcel.readString(), parcel.readDouble(),
					parcel.readDouble());
			enerty.person = parcel.readString();
			enerty.phone = parcel.readString();
			enerty.type = parcel.readString();
			return enerty;
		}

		@Override
		public Enerty[] newArray(int i) {
			return new Enerty[0];
		}
	};
}
