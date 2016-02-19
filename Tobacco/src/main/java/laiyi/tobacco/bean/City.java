package laiyi.tobacco.bean;

import java.util.ArrayList;
import java.util.List;


public class City
{
    String cityname = "";
    String cityid = "";
    String firstLettor = "";
    String hotCity = "";
    public boolean hasChild = false;
    List<City> citysub = new ArrayList<> ();

    public void addSub (City subCity)
    {
        citysub.add (subCity);
    }

    public boolean isHasChild ()
    {
        return hasChild;
    }

    public void setHasChild (boolean hasChild)
    {
        this.hasChild = hasChild;
    }

    public List<City> getCitysub ()
    {
        return citysub;
    }

    public void setCitysub (List<City> citysub)
    {
        this.citysub = citysub;
    }

    public String getFirstLettor ()
    {
        return firstLettor;
    }

    public void setFirstLettor (String firstLettor)
    {
        this.firstLettor = firstLettor;
    }

    public String getHotCity ()
    {
        return hotCity;
    }

    public void setHotCity (String hotCity)
    {
        this.hotCity = hotCity;
    }

    public City (String cityname, String cityid)
    {
        this.cityname = cityname;
        this.cityid = cityid;

    }

    public String getCityname ()
    {
        return cityname;
    }

    public void setCityname (String cityname)
    {
        this.cityname = cityname;
    }

    public String getCityid ()
    {
        return cityid;
    }

    public void setCityid (String cityid)
    {
        this.cityid = cityid;
    }

}
