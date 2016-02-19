package com.runkun.lbsq.busi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.bean.Order;
import com.runkun.lbsq.busi.util.Tools;

import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class OrderAdapter extends MyBaseAdapter<Order>
{
    int type;

    public OrderAdapter(Context context, List<Order> datas, int layoutId, int type)
    {
        super(context, datas, layoutId, false);
        this.type = type;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final Order bean)
    {
        final TextView order_time = viewHolder.getView(R.id.order_time);
        final TextView order_price = viewHolder.getView(R.id.order_price);
        final TextView order_count = viewHolder.getView(R.id.order_count);

        if (type == 1)
        {
            String time = bean.getOrder_time();
            order_time.setText(Tools.isTimeEmpty(time) ? "" : Tools.formatMysqlTimestamp(time, "yyyy-MM-dd"));
        } else
        {
            String startTime = bean.getOrder_time_start();
            String lastTime = bean.getOrder_time_end();
            order_time.setText((Tools.isTimeEmpty(startTime) || Tools.isTimeEmpty(lastTime)) ? "" : (Tools.formatMysqlTimestamp(startTime, "MM-dd") + "~" + Tools.formatMysqlTimestamp(lastTime, "MM-dd")));

        }
        SpannableString spannableString = new SpannableString("订单总量：" + bean.getCountorder());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#39ac69")), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        order_count.setText(spannableString);

        SpannableString spannableString2 = new SpannableString("交易总额：￥" + bean.getTotalorder());
        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#39ac69")), 5, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        order_price.setText(spannableString2);
    }
}
