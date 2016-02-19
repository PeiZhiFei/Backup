package laiyi.tobacco.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2015/9/29.
 */
@DatabaseTable(tableName = "user")
public class User {

    /**
     * UserId : 10431
     * UserNo : 901
     * UserName : 福泉测试
     * Key : 2EG24AFF88Y66DD0B5CB3DS37AGFD9J380D45B1B
     * Time : 2015-09-28 21:57:15
     * PasswordMd5 : 892c91e0a653ba19df81a90f89d99bcd
     * Err : null
     * ListRoom : null
     * ListGrower : null
     */
    @DatabaseField
    private int userId;
    @DatabaseField
    private String userNo;
    @DatabaseField
    private String userName;
    @DatabaseField
    private String key;
    @DatabaseField
    private String time;
    @DatabaseField
    private String passwordMd5;
    @DatabaseField
    private String err;
    @DatabaseField
    private String listRoom;
    @DatabaseField
    private String listGrower;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getListRoom() {
        return listRoom;
    }

    public void setListRoom(String listRoom) {
        this.listRoom = listRoom;
    }

    public String getListGrower() {
        return listGrower;
    }

    public void setListGrower(String listGrower) {
        this.listGrower = listGrower;
    }
}
