package com.runkun.lbsq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Coupons;
import com.runkun.lbsq.utils.Tools;

import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

/**
 * 优惠卷列表
 */
public class CouponsAdapter extends MyBaseAdapter<Coupons>
{
    private Context context;

    public CouponsAdapter(Context context, List<Coupons> datas, int layoutId)
    {

        super (context, datas, layoutId, false);
        this.context = context;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final Coupons bean)
    {
        final ImageView bottomImage = viewHolder.getView (R.id.bottom_image);
        final ImageView topImage = viewHolder.getView (R.id.top_image);
        final TextView coupousMoney = viewHolder.getView (R.id.coupous_money);
        final TextView typeAndMoney = viewHolder.getView (R.id.type_and_money);
        final TextView useDate = viewHolder.getView (R.id.use_date);
        final TextView store = viewHolder.getView (R.id.store_name);
        coupousMoney.setText ("￥ " + bean.getAmount ());
        useDate.setText ("有效期至：" + Tools.formatMysqlTimestamp (bean.getEnd_time (),
                "yyyy-MM-dd"));

        int type = Integer.valueOf (bean.getType ());
        switch (type)
        {
            case 1:
                typeAndMoney.setText ("【会员优惠券】" + bean.getXianzhi () + "元以上可用");
                store.setText ("本优惠券全场通用");
                ConfigUtil.write (context, "couponsId", bean.getId ());
                break;
            case 2:
                if (!Tools.isEmpty (bean.getStore_name ()))
                {
                    store.setText ("仅限" + bean.getStore_name () + "使用");
                }
                typeAndMoney.setText ("【商户券】" + bean.getXianzhi () + "元以上可用");
                break;
            case 3:
                typeAndMoney.setText ("【新注册奖励券】" + bean.getXianzhi () + "元以上可用");
                store.setText ("本优惠券全场通用");
                break;
            case 4:
                typeAndMoney.setText ("【普通券】" + bean.getXianzhi () + "元以上可用");
                store.setText ("本优惠券全场通用");
                break;
        }


        // 已使用
        if (bean.getIs_use () != null)
        {
            if (bean.getIs_use ().equals ("1"))
            {
                bottomImage.setVisibility (View.VISIBLE);
                bottomImage.setImageResource (R.drawable.coupons_used);
                topImage.setBackgroundResource (R.drawable.item_compons_gray);
                coupousMoney.setTextColor (Color.parseColor ("#e0e0e0"));
                typeAndMoney.setTextColor (Color.parseColor ("#e0e0e0"));
                useDate.setTextColor (Color.parseColor ("#e0e0e0"));
                store.setTextColor (Color.parseColor ("#e0e0e0"));
                // 未使用
            } else if (bean.getIs_use ().equals ("0"))
            {
                // 未使用,已过期
                if (bean.getIs_guoqi ().equals ("1"))
                {
                    bottomImage.setVisibility (View.VISIBLE);
                    bottomImage.setImageResource (R.drawable.coupons_old);
                    topImage.setBackgroundResource (R.drawable.item_compons_gray);
                    coupousMoney.setTextColor (Color.parseColor ("#e0e0e0"));
                    typeAndMoney.setTextColor (Color.parseColor ("#e0e0e0"));
                    useDate.setTextColor (Color.parseColor ("#e0e0e0"));
                    store.setTextColor (Color.parseColor ("#e0e0e0"));
                    // 未使用,未过期,正常态
                } else if (bean.getIs_guoqi ().equals ("0"))
                {
                    bottomImage.setVisibility (View.INVISIBLE);
                    topImage.setBackgroundResource (R.drawable.item_compons);
                    coupousMoney.setTextColor (Color.parseColor ("#ff5757"));
                    typeAndMoney.setTextColor (Color.parseColor ("#666666"));
                    useDate.setTextColor (Color.parseColor ("#666666"));
                    store.setTextColor (Color.parseColor ("#666666"));
                }
            }
        }
    }
}
