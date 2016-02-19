package com.runkun.lbsq.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.bean.Good;
import com.runkun.lbsq.bean.GoodDataEntity;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.fragment.GoodsCategoryFragment;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.XUtilsImageLoader;

import feifei.project.util.ConfigUtil;

public class GoodsCategoryCombineView extends LinearLayout
{

    public String getGoodId ()
    {
        return goodId;
    }


    @ViewInject(R.id.good_img)
    private ImageView goodImgIV;

    @ViewInject(R.id.good_name)
    private TextView goodNameTV;

    @ViewInject(R.id.price)
    private TextView goodPriceTV;


    @ViewInject(R.id.order_count)
    private TextView orderCountTV;


    private Context context;

    private String goodId;
    private String storeId;
    private String goodName;
    private String price;

    private GoodsCategoryFragment gcf;

    public GoodsCategoryCombineView (Context context)
    {
        super (context);
        this.context = context;
        ViewUtils.inject (this, ((LayoutInflater) context
                .getSystemService (Context.LAYOUT_INFLATER_SERVICE)).inflate (
                R.layout.goods_category_item, this, true));
    }

    public GoodsCategoryCombineView (Context paramContext,
                                     AttributeSet paramAttributeSet)
    {
        super (paramContext, paramAttributeSet);
        this.context = paramContext;
        ViewUtils.inject (this, ((LayoutInflater) paramContext
                .getSystemService (Context.LAYOUT_INFLATER_SERVICE)).inflate (
                R.layout.goods_category_item, this, true));

    }

    @SuppressLint("NewApi")
    public GoodsCategoryCombineView (Context paramContext,
                                     AttributeSet paramAttributeSet, int paramInt)
    {
        super (paramContext, paramAttributeSet, paramInt);
        ViewUtils.inject (this, ((LayoutInflater) paramContext
                .getSystemService (Context.LAYOUT_INFLATER_SERVICE)).inflate (
                R.layout.goods_category_item, this, true));
    }

    public void setTitleForText (Good entity, GoodsCategoryFragment gcf)
    {
        this.goodId = entity.getGoodsId ();
        this.storeId = entity.getStoreId ();
        this.goodName = entity.getGoodsName ();
        goodNameTV.setText (goodName);
        this.price = entity.getGoodsPrice ();
        goodPriceTV.setText (price);
        orderCountTV.setText ("1");
        //		new XUtilsImageLoader(this.context, this.goodImgIV.getMeasuredWidth(),
        //				this.goodImgIV.getMeasuredHeight()).display (this.goodImgIV,
        //				entity.getGoodsPic ());

        new XUtilsImageLoader (context, R.drawable.zhanwei, R.drawable.zhanwei,
                true, true).display (goodImgIV, entity.getGoodsPic ());
        this.gcf = gcf;
    }

    public void setTitleForText (GoodDataEntity entity, GoodsCategoryFragment gcf)
    {
        this.goodId = entity.getGoods_id ();
        this.storeId = ConfigUtil.readString (context, Shop.SHOPID, "");
        this.goodName = entity.getGoods_name ();
        goodNameTV.setText (goodName);
        this.price = entity.getGoods_price ();
        goodPriceTV.setText (price);
        orderCountTV.setText ("1");
        //		new XUtilsImageLoader(this.context, this.goodImgIV.getMeasuredWidth(),
        //				this.goodImgIV.getMeasuredHeight()).display (this.goodImgIV,
        //				entity.getGoodsPic ());

        new XUtilsImageLoader (context, R.drawable.zhanwei, R.drawable.zhanwei,
                true, true).display (goodImgIV, entity.getGoods_pic ());
        this.gcf = gcf;
    }


    public ImageView getGoodImgIV ()
    {
        return goodImgIV;
    }


    public TextView getOrderCountTV ()
    {
        return orderCountTV;
    }


    @OnClick(value = {R.id.good_img, R.id.minus_btn, R.id.plus_btn,
            R.id.good_card})
    public void onClick (View v)
    {
        switch (v.getId ())
        {
            case R.id.good_img:
                Intent intent = new Intent ();
                intent.putExtra ("goods_id", goodId);
                intent.setClass (context, GoodDetailActivity.class);
                context.startActivity (intent);
                AnimUtil.animToSlide ((Activity) context);
                break;
            case R.id.minus_btn:
                int oriCnt = Integer.valueOf (orderCountTV.getText ().toString ());
                orderCountTV.setText (String.valueOf (oriCnt == 0 ? 0 : --oriCnt));
                break;
            case R.id.plus_btn:
                int oriCount = Integer.valueOf (orderCountTV.getText ().toString ());
                orderCountTV.setText (String.valueOf (oriCount == 99 ? 99
                        : ++oriCount));
                break;
            case R.id.good_card:
                gcf.add2Shopcart (v, this);
                break;
        }

    }

}
