package com.runkun.lbsq.busi.activity;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.adapter.OrderInfoAdapter;
import com.runkun.lbsq.busi.bean.OrderMore;
import com.runkun.lbsq.busi.util.MyConstant;

public class SearchOrderActivity extends SearchBaseActivity<OrderMore>
{


    @Override
    protected void initKey()
    {
        key = "order_sn";
        hint = "请输入订单号";
    }


    @Override
    protected void itemClick(int position)
    {
    }

    @Override
    protected void initType()
    {
        adapter = new OrderInfoAdapter(activity, data, R.layout.item_orderinfo);
        url = MyConstant.CSEARCHORDER;
        cls = OrderMore.class;
    }
}
