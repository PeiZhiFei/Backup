package com.runkun.lbsq.busi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.activity.OrderInfoActivity;
import com.runkun.lbsq.busi.adapter.OrderAdapter;
import com.runkun.lbsq.busi.bean.Order;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;

public class OrderFragment extends SimpleListFragment<Order>
{
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            type = bundle.getInt("type");
        }
    }

    public static OrderFragment newInstance(int type)
    {
        OrderFragment myBuyBasefragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        myBuyBasefragment.setArguments(args);
        return myBuyBasefragment;
    }

    @Override
    protected void itemClick(int position)
    {
        Intent intent = new Intent(fragment, OrderInfoActivity.class);
        intent.putExtra("type", type);
        if (type == 1)
        {
            intent.putExtra("time", data.get(position).getOrder_time());
            L.l (data.get (position).getOrder_time ());
        } else
        {
            intent.putExtra("startTime", data.get(position).getOrder_time_start());
            intent.putExtra("endTime", data.get(position).getOrder_time_end());
        }
        startActivity(intent);
        AnimUtil.animTo((Activity) fragment);
    }

    protected void initType()
    {
        adapter = new OrderAdapter(fragment, data, R.layout.item_order, type);

        rp.addQueryStringParameter("store_id", ConfigUtil.readString (fragment, MyConstant.KEY_STOREID, ""));
        //// TODO: 2015/8/31
//        rp.addQueryStringParameter("store_id", "83");
        cls = Order.class;
        switch (type)
        {
            case 1:
                url = MyConstant.CORDERDAY;
                break;
            case 2:
                url = MyConstant.CORDERWEEK;
                break;
            case 3:
                url = MyConstant.CORDERMONTH;
                break;
        }
    }

    @Override
    protected void makeData(JSONObject jsonObject) throws JSONException
    {
        JSONObject result = jsonObject.getJSONObject("datas");
        JSONObject total = result.getJSONObject("totalorder");
        JSONObject count = result.getJSONObject("countorder");
        JSONObject time = null, startTime = null, endTime = null;
        if (type == 1)
        {
            time = result.getJSONObject("order_time");
        } else
        {
            startTime = result.getJSONObject("order_time_start");
            endTime = result.getJSONObject("order_time_end");
        }

        for (int i = 0; i < total.length(); i++)
        {
            Order order = new Order();
            order.setCountorder(count.getString(String.valueOf(i + 1)));
            order.setTotalorder(total.getString(String.valueOf(i + 1)));
            if (type == 1)
            {
                order.setOrder_time(time.getString(String.valueOf(i + 1)));
            } else
            {
                order.setOrder_time_start(startTime.getString(String.valueOf(i + 1)));
                order.setOrder_time_end(endTime.getString(String.valueOf(i + 1)));
            }
            data.add(order);
        }
        adapter.notifyDataSetChanged();
    }

}
