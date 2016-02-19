package com.runkun.lbsq.busi.activity;

import android.os.Bundle;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.adapter.OrderInfoAdapter;
import com.runkun.lbsq.busi.bean.OrderMore;
import com.runkun.lbsq.busi.util.MyConstant;

import butterknife.ButterKnife;
import feifei.project.util.ConfigUtil;

public class OrderInfoActivity extends FooterListActivity<OrderMore>
{
    String time;
    String startTime;
    String endTime;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ButterKnife.inject(this);
        tint();
        initActionbar();
        setTitles("订单详情");

        type = getIntent().getIntExtra("type", 1);
        if (type == 1)
        {
            time = getIntent().getStringExtra("time");
        } else
        {
            startTime = getIntent().getStringExtra("startTime");
            endTime = getIntent().getStringExtra("endTime");
        }
        init();
        loadData();
    }

    @Override
    protected void itemClick(int position)
    {

    }

    @Override
    protected void initType()
    {
        adapter = new OrderInfoAdapter(activity, data, R.layout.item_orderinfo);
        url = MyConstant.CORDERDAYDETAIL;
        cls = OrderMore.class;
        //// TODO: 2015/8/31  
        rp.addQueryStringParameter("store_id", ConfigUtil.readString (activity, MyConstant.KEY_STOREID, ""));
//        rp.addQueryStringParameter("store_id", "83");
        switch (type)
        {
            case 1:
                rp.addQueryStringParameter("type", "1");
                rp.addQueryStringParameter("ordertime", time);
                break;
            case 2:
            case 3:
                rp.addQueryStringParameter("type", "2");
                rp.addQueryStringParameter("start_time", startTime);
                rp.addQueryStringParameter("end_time", endTime);
                //    这里不知道要不要加
                //    rp.addQueryStringParameter("ordertime", time);
                break;
        }


    }
}
