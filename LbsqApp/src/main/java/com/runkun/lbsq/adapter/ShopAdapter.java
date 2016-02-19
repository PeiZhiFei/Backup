package com.runkun.lbsq.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.Tools;

import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class ShopAdapter extends MyBaseAdapter<Shop>
{
    boolean arrow;
    boolean dis;

    public ShopAdapter (Context context, List<Shop> datas, int layoutId,
                        boolean arrow, boolean dis)
    {
        super (context, datas, layoutId, true);
        this.arrow = arrow;
        this.dis = dis;
    }

    @Override
    protected void convert (final MyViewHolder viewHolder, final Shop bean)
    {
        final TextView shopName = viewHolder.getView (R.id.shop_name);
        final TextView address = viewHolder.getView (R.id.address);
        final TextView distance = viewHolder.getView (R.id.distance);
        final ImageView arrows = viewHolder.getView (R.id.arrow);
        shopName.setText (bean.getStore_name ());
        address.setText (bean.getAddress ());
        if ( dis )
        {
            if ( !Tools.isEmpty (bean.getDis ()) )
            {
                distance.setText (bean.getDis () + "KM");
                distance.setVisibility (View.VISIBLE);
                // distance.setText((int) (Double.valueOf(bean.getDistance()) *
                // 1000)
                // + "M");
            }

        } else
        {
            distance.setVisibility (View.INVISIBLE);
        }

        if ( arrow )
        {
            arrows.setVisibility (View.VISIBLE);
        } else
        {
            arrows.setVisibility (View.INVISIBLE);
        }

    }
}
