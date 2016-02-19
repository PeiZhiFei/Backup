package feifei.dataanalysis.bean;


public class HotWords
{
    public String getStoreName ()
    {
        return storeName;
    }

    public void setStoreName (String storeName)
    {
        this.storeName = storeName;
    }

    public String getID ()
    {
        return id;
    }

    public void setID (String id)
    {
        this.id = id;
    }

    private String storeName;
    private String id;

    public HotWords ()
    {

    }

    public HotWords (String id, String storeName)
    {
        this.id = id;
        this.storeName = storeName;
    }
}
