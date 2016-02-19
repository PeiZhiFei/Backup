package feifei.dataanalysis.bean;


public class Order
{
    private String order_count;
    private String order_money;
    private String store_name;
    private String store_id;

    public Order ()
    {
    }

    public String getStore_id ()
    {
        return store_id;
    }

    public void setStore_id (String store_id)
    {
        this.store_id = store_id;
    }


    public String getOrder_count ()
    {
        return order_count;
    }

    public void setOrder_count (String order_count)
    {
        this.order_count = order_count;
    }

    public String getOrder_money ()
    {
        return order_money;
    }

    public void setOrder_money (String order_money)
    {
        this.order_money = order_money;
    }

    public String getStore_name ()
    {
        return store_name;
    }

    public void setStore_name (String store_name)
    {
        this.store_name = store_name;
    }


}
