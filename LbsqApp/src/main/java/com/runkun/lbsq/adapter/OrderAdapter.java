package com.runkun.lbsq.adapter;

import android.content.Context;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Order;
import com.runkun.lbsq.utils.Tools;

import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;
import feifei.project.view.smartimage.SmartImageView;

public class OrderAdapter extends MyBaseAdapter<Order>
{
    public OrderAdapter (Context context, List<Order> datas, int layoutId)
    {
        super (context, datas, layoutId, false);
    }

    @Override
    protected void convert (final MyViewHolder viewHolder, final Order bean)
    {
        final TextView order_time = viewHolder.getView (R.id.order_time);
        //        final TextView order_state = viewHolder.getView (R.id.order_state);
        final TextView price = viewHolder.getView (R.id.price);
        final TextView shop_name = viewHolder.getView (R.id.shop_name);
        final SmartImageView order_image = viewHolder.getView (R.id.order_image);
        order_time.setText (Tools.formatMysqlTimestamp (bean.getAdd_time (), "yyyy-MM-dd"));
        //        order_state.setText (bean.getState ());
        price.setText ("￥" + bean.getZongjia ());
        shop_name.setText (bean.getStore_name ());
        order_image.setImageUrl (bean.getStore_pic ());

    }
}
