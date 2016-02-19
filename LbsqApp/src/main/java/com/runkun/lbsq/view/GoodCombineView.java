package com.runkun.lbsq.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.bean.Good;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.XUtilsImageLoader;

public class GoodCombineView extends LinearLayout implements OnClickListener
{
    @ViewInject(R.id.good_img)
    private ImageView good;

    @ViewInject(R.id.good_layout)
    private RelativeLayout good_layout;

    @ViewInject(R.id.good_name)
    private TextView goodName;

    @ViewInject(R.id.or_price)
    private TextView originalPrice;

    @ViewInject(R.id.price)
    private TextView price;

    private String storeid;
    private String tjprice;
    private Context context;
    private String goodId;


    public TextView getPrice()
    {
        return price;
    }

    public TextView getGoodName()
    {
        return goodName;
    }

    public GoodCombineView(Context context)
    {
        super(context);
        this.context = context;
        ViewUtils.inject(this, ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.good_item, this, true));
        good_layout.setOnClickListener(this);
    }

    public GoodCombineView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        ViewUtils.inject(this, ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.good_item, this, true));
        good_layout.setOnClickListener(this);
    }

    public GoodCombineView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        ViewUtils.inject(this, ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.good_item, this, true));
        good_layout.setOnClickListener(this);
    }

    public ImageView getGood()
    {
        return this.good;
    }

    public String getGoodId()
    {
        return this.goodId;
    }


    public RelativeLayout getGood_layout()
    {
        return good_layout;
    }

    public void setTitleForText(Boolean tejia, String goodName, String tprice,
                                String price, String imgUrl, String goodId, String storeid)
    {
        this.goodId = goodId;
        this.storeid = storeid;
        this.goodName.setText(goodName);
        if (tejia)
        {
            this.originalPrice.setVisibility(View.VISIBLE);
            this.originalPrice.setText("￥" + price);
            this.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            this.price.setText("￥" + tprice);
            tjprice = tprice;
        } else
        {
            this.price.setText("￥" + price);
        }
        new XUtilsImageLoader(this.context, this.good.getMeasuredWidth(),
                this.good.getMeasuredHeight()).display(this.good, imgUrl);
    }

    public void setTitleForText(Good entity)
    {
        this.goodId = entity.getGoodsId();
        this.storeid = entity.getStoreId();
        this.goodName.setText(entity.getGoodsName());
        String orPrice = entity.getGoodsOrPrice();
        this.originalPrice.setText(orPrice == null ? "" : orPrice);
        tjprice = entity.getGoodsPrice();
        this.price.setText(tjprice);
        new XUtilsImageLoader(this.context, this.good.getMeasuredWidth(),
                this.good.getMeasuredHeight()).display(this.good,
                entity.getGoodsPic());
    }

    @Override
    @OnClick(value = {R.id.good_img, R.id.good_layout})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.good_img:
            case R.id.good_layout:
                Intent intent2 = new Intent(context, GoodDetailActivity.class);
                intent2.putExtra("goods_id", goodId);
                intent2.putExtra("tjprice", tjprice);
                context.startActivity(intent2);
                AnimUtil.animToSlide(((Activity) context));
                break;
        }

    }
}
