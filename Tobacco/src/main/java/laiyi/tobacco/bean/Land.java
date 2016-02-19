package laiyi.tobacco.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2015/9/29.
 */
@DatabaseTable(tableName = "land")
public class Land implements Parcelable
{

    /**
     * LandId : 2
     * SId : 1223333
     * LandNo : 2005-01-10
     * LandName : 西北坡1号
     * LandAddress : 西边
     * LandArea : 12.09
     * DeptId : 1
     * FarmId : 1
     * LandType : 旱地
     * LandFeature : 平田
     * SoilType : 沙土
     * SoilFertility : 高
     * TobaccoType : 考烟
     * TobaccoBreed : 云烟87
     * WaterExist : 1
     * WaterState : 1
     * GpsArea : 0.0
     * SeaLevel : 978.12
     * Pic :
     * Gps : 百度
     * GpsX : 10.2
     * GpsY : 123.33
     * Mem :
     * SortIndex : 1
     * State : 1
     * SYear : 2015
     * CreateTime : /Date(1443402072000)/
     * CreateIp : 1
     */
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int LandId;
    @DatabaseField
    private String SId;
    @DatabaseField
    private String LandNo;
    @DatabaseField
    private String LandName;
    @DatabaseField
    private String LandAddress;
    @DatabaseField
    private double LandArea;
    @DatabaseField
    private int DeptId;
    @DatabaseField
    private int FarmId;
    @DatabaseField
    private String LandType;
    @DatabaseField
    private String LandFeature;
    @DatabaseField
    private String SoilType;
    @DatabaseField
    private String SoilFertility;
    @DatabaseField
    private String TobaccoType;
    @DatabaseField
    private String TobaccoBreed;
    @DatabaseField
    private int WaterExist;
    @DatabaseField
    private int WaterState;
    @DatabaseField
    private double GpsArea;
    @DatabaseField
    private double SeaLevel;
    @DatabaseField
    private String Pic;
    @DatabaseField
    private String Gps;
    @DatabaseField
    private String GpsX;
    @DatabaseField
    private String GpsY;
    @DatabaseField
    private String Mem;
    @DatabaseField
    private int SortIndex;
    @DatabaseField
    private int State; //本地状态  1 修改  2新增 3 删除
    @DatabaseField
    private int SYear;
    @DatabaseField
    private String CreateTime;
    @DatabaseField
    private String CreateIp;
    @DatabaseField
    private String Pic1;
    @DatabaseField
    private String Pic2;
    @DatabaseField
    private String Pic3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic1() {
        return Pic1;
    }

    public void setPic1(String pic1) {
        Pic1 = pic1;
    }

    public String getPic2() {
        return Pic2;
    }

    public void setPic2(String pic2) {
        Pic2 = pic2;
    }

    public String getPic3() {
        return Pic3;
    }

    public void setPic3(String pic3) {
        Pic3 = pic3;
    }

    public Land() {
    }

    public void setLandId(int LandId) {
        this.LandId = LandId;
    }

    public void setSId(String SId) {
        this.SId = SId;
    }

    public void setLandNo(String LandNo) {
        this.LandNo = LandNo;
    }

    public void setLandName(String LandName) {
        this.LandName = LandName;
    }

    public void setLandAddress(String LandAddress) {
        this.LandAddress = LandAddress;
    }

    public void setLandArea(double LandArea) {
        this.LandArea = LandArea;
    }

    public void setDeptId(int DeptId) {
        this.DeptId = DeptId;
    }

    public void setFarmId(int FarmId) {
        this.FarmId = FarmId;
    }

    public void setLandType(String LandType) {
        this.LandType = LandType;
    }

    public void setLandFeature(String LandFeature) {
        this.LandFeature = LandFeature;
    }

    public void setSoilType(String SoilType) {
        this.SoilType = SoilType;
    }

    public void setSoilFertility(String SoilFertility) {
        this.SoilFertility = SoilFertility;
    }

    public void setTobaccoType(String TobaccoType) {
        this.TobaccoType = TobaccoType;
    }

    public void setTobaccoBreed(String TobaccoBreed) {
        this.TobaccoBreed = TobaccoBreed;
    }

    public void setWaterExist(int WaterExist) {
        this.WaterExist = WaterExist;
    }

    public void setWaterState(int WaterState) {
        this.WaterState = WaterState;
    }

    public void setGpsArea(double GpsArea) {
        this.GpsArea = GpsArea;
    }

    public void setSeaLevel(double SeaLevel) {
        this.SeaLevel = SeaLevel;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }

    public void setGps(String Gps) {
        this.Gps = Gps;
    }

    public void setGpsX(String GpsX) {
        this.GpsX = GpsX;
    }

    public void setGpsY(String GpsY) {
        this.GpsY = GpsY;
    }

    public void setMem(String Mem) {
        this.Mem = Mem;
    }

    public void setSortIndex(int SortIndex) {
        this.SortIndex = SortIndex;
    }

    public void setState(int State) {
        this.State = State;
    }

    public void setSYear(int SYear) {
        this.SYear = SYear;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public void setCreateIp(String CreateIp) {
        this.CreateIp = CreateIp;
    }

    public int getLandId() {
        return LandId;
    }

    public String getSId() {
        return SId;
    }

    public String getLandNo() {
        return LandNo;
    }

    public String getLandName() {
        return LandName;
    }

    public String getLandAddress() {
        return LandAddress;
    }

    public double getLandArea() {
        return LandArea;
    }

    public int getDeptId() {
        return DeptId;
    }

    public int getFarmId() {
        return FarmId;
    }

    public String getLandType() {
        return LandType;
    }

    public String getLandFeature() {
        return LandFeature;
    }

    public String getSoilType() {
        return SoilType;
    }

    public String getSoilFertility() {
        return SoilFertility;
    }

    public String getTobaccoType() {
        return TobaccoType;
    }

    public String getTobaccoBreed() {
        return TobaccoBreed;
    }

    public int getWaterExist() {
        return WaterExist;
    }

    public int getWaterState() {
        return WaterState;
    }

    public double getGpsArea() {
        return GpsArea;
    }

    public double getSeaLevel() {
        return SeaLevel;
    }

    public String getPic() {
        return Pic;
    }

    public String getGps() {
        return Gps;
    }

    public String getGpsX() {
        return GpsX;
    }

    public String getGpsY() {
        return GpsY;
    }

    public String getMem() {
        return Mem;
    }

    public int getSortIndex() {
        return SortIndex;
    }

    public int getState() {
        return State;
    }

    public int getSYear() {
        return SYear;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public String getCreateIp() {
        return CreateIp;
    }

    @Override
    public int describeContents ()
    {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags)
    {
        dest.writeInt (this.id);
        dest.writeInt (this.LandId);
        dest.writeString (this.SId);
        dest.writeString (this.LandNo);
        dest.writeString (this.LandName);
        dest.writeString (this.LandAddress);
        dest.writeDouble (this.LandArea);
        dest.writeInt (this.DeptId);
        dest.writeInt (this.FarmId);
        dest.writeString (this.LandType);
        dest.writeString (this.LandFeature);
        dest.writeString (this.SoilType);
        dest.writeString (this.SoilFertility);
        dest.writeString (this.TobaccoType);
        dest.writeString (this.TobaccoBreed);
        dest.writeInt (this.WaterExist);
        dest.writeInt (this.WaterState);
        dest.writeDouble (this.GpsArea);
        dest.writeDouble (this.SeaLevel);
        dest.writeString (this.Pic);
        dest.writeString (this.Gps);
        dest.writeString (this.GpsX);
        dest.writeString (this.GpsY);
        dest.writeString (this.Mem);
        dest.writeInt (this.SortIndex);
        dest.writeInt (this.State);
        dest.writeInt (this.SYear);
        dest.writeString (this.CreateTime);
        dest.writeString (this.CreateIp);
        dest.writeString (this.Pic1);
        dest.writeString (this.Pic2);
        dest.writeString (this.Pic3);
    }

    protected Land (Parcel in)
    {
        this.id = in.readInt ();
        this.LandId = in.readInt ();
        this.SId = in.readString ();
        this.LandNo = in.readString ();
        this.LandName = in.readString ();
        this.LandAddress = in.readString ();
        this.LandArea = in.readDouble ();
        this.DeptId = in.readInt ();
        this.FarmId = in.readInt ();
        this.LandType = in.readString ();
        this.LandFeature = in.readString ();
        this.SoilType = in.readString ();
        this.SoilFertility = in.readString ();
        this.TobaccoType = in.readString ();
        this.TobaccoBreed = in.readString ();
        this.WaterExist = in.readInt ();
        this.WaterState = in.readInt ();
        this.GpsArea = in.readDouble ();
        this.SeaLevel = in.readDouble ();
        this.Pic = in.readString ();
        this.Gps = in.readString ();
        this.GpsX = in.readString ();
        this.GpsY = in.readString ();
        this.Mem = in.readString ();
        this.SortIndex = in.readInt ();
        this.State = in.readInt ();
        this.SYear = in.readInt ();
        this.CreateTime = in.readString ();
        this.CreateIp = in.readString ();
        this.Pic1 = in.readString ();
        this.Pic2 = in.readString ();
        this.Pic3 = in.readString ();
    }

    public static final Parcelable.Creator<Land> CREATOR = new Parcelable.Creator<Land> ()
    {
        public Land createFromParcel (Parcel source)
        {
            return new Land (source);
        }

        public Land[] newArray (int size)
        {
            return new Land[size];
        }
    };

    @Override
    public String toString ()
    {
        return "Land{" +
                "id=" + id +
                ", LandId=" + LandId +
                ", SId='" + SId + '\'' +
                ", LandNo='" + LandNo + '\'' +
                ", LandName='" + LandName + '\'' +
                ", LandAddress='" + LandAddress + '\'' +
                ", LandArea=" + LandArea +
                ", DeptId=" + DeptId +
                ", FarmId=" + FarmId +
                ", LandType='" + LandType + '\'' +
                ", LandFeature='" + LandFeature + '\'' +
                ", SoilType='" + SoilType + '\'' +
                ", SoilFertility='" + SoilFertility + '\'' +
                ", TobaccoType='" + TobaccoType + '\'' +
                ", TobaccoBreed='" + TobaccoBreed + '\'' +
                ", WaterExist=" + WaterExist +
                ", WaterState=" + WaterState +
                ", GpsArea=" + GpsArea +
                ", SeaLevel=" + SeaLevel +
                ", Pic='" + Pic + '\'' +
                ", Gps='" + Gps + '\'' +
                ", GpsX='" + GpsX + '\'' +
                ", GpsY='" + GpsY + '\'' +
                ", Mem='" + Mem + '\'' +
                ", SortIndex=" + SortIndex +
                ", State=" + State +
                ", SYear=" + SYear +
                ", CreateTime='" + CreateTime + '\'' +
                ", CreateIp='" + CreateIp + '\'' +
                ", Pic1='" + Pic1 + '\'' +
                ", Pic2='" + Pic2 + '\'' +
                ", Pic3='" + Pic3 + '\'' +
                '}';
    }
}
