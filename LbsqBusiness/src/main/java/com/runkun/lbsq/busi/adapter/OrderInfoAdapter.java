package com.runkun.lbsq.busi.adapter;

import android.content.Context;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.bean.OrderMore;
import com.runkun.lbsq.busi.util.Tools;

import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class OrderInfoAdapter extends MyBaseAdapter<OrderMore>
{
    public OrderInfoAdapter(Context context, List<OrderMore> datas, int layoutId)
    {
        super(context, datas, layoutId, false);
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final OrderMore bean)
    {
        viewHolder.setText(R.id.order_sn, bean.getOrder_sn());
        viewHolder.setText(R.id.order_customer, bean.getConsigner());
        viewHolder.setText(R.id.order_address, bean.getMember_address());
        viewHolder.setText(R.id.order_price, bean.getTotal_fee());
        viewHolder.setText(R.id.order_state, bean.getState().equals("2") ? "已支付" : "已打印");
        String time = bean.getAdd_time();
        viewHolder.setText(R.id.order_time, Tools.isTimeEmpty(time) ? "" : Tools.formatMysqlTimestamp(time, "yyyy-MM-dd"));

    }
}

