package laiyi.tobacco.bean;

public class Order
{
    private double la;
    private double lo;
    //编号
    private String number;
    //地势
    private String area;
    //地址
    private String address;

    public Order ()
    {

    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public double getLa ()
    {
        return la;
    }

    public void setLa (double la)
    {
        this.la = la;
    }

    public double getLo ()
    {
        return lo;
    }

    public void setLo (double lo)
    {
        this.lo = lo;
    }

    public String getNumber ()
    {
        return number;
    }

    public void setNumber (String number)
    {
        this.number = number;
    }

    public String getArea ()
    {
        return area;
    }

    public void setArea (String area)
    {
        this.area = area;
    }

}
