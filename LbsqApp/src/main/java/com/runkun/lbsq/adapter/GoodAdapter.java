package com.runkun.lbsq.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BuyNowActivity;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.activity.GoodMoreActivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.SearchGoodsActivity;
import com.runkun.lbsq.bean.GoodMore;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.Tools;

import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;
import feifei.project.view.smartimage.SmartImageView;

public class GoodAdapter extends MyBaseAdapter<GoodMore>
{
    Context context;
    boolean hasShopCard;
    boolean swipe;
    boolean oldprice;

    public GoodAdapter(Context context, List<GoodMore> datas,
                       int layoutId, boolean hasShopCard, boolean swipe, boolean oldprice)
    {
        super(context, datas, layoutId);
        this.context = context;
        this.hasShopCard = hasShopCard;
        this.swipe = swipe;
        this.oldprice = oldprice;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder,
                           final GoodMore bean)
    {
        final TextView time = viewHolder.getView(R.id.shop_name);
        final TextView price = viewHolder.getView(R.id.good_price);
        final TextView old_price = viewHolder.getView(R.id.old_price);
        final TextView buy = viewHolder.getView(R.id.buy_now);
        final SmartImageView image = viewHolder.getView(R.id.good_img);
        final ImageButton shopcard = viewHolder.getView(R.id.shopcard);
        final View layout = viewHolder.getView(R.id.layout);
        if (!hasShopCard)
        {
            shopcard.setVisibility(View.INVISIBLE);
        } else
        {

            shopcard.setVisibility(View.VISIBLE);
            shopcard.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    String memberId = ConfigUtil.readString (context,
                            "memberId", "");
                    if (Tools.isEmpty(memberId))
                    {
                        Tools.dialog(context,
                                Tools.getStr(context, R.string.REQUESTLOGIN), true,
                                new onButtonClick()
                                {

                                    @Override
                                    public void buttonClick()
                                    {
                                        Intent intent = new Intent(context,
                                                LoginActivity.class);
                                        context.startActivity(intent);
                                    }
                                });
                    } else
                    {
                        RequestParams requestParams = new RequestParams();
                        requestParams.addQueryStringParameter("goods_id",
                                bean.getGoodsId());
                        requestParams.addQueryStringParameter("quantity", "1");
                        requestParams.addQueryStringParameter("member_id",
                                memberId);
                        if (context instanceof GoodMoreActivity)
                        {
                            GoodMoreActivity gma = (GoodMoreActivity) context;
                            ImageView aniImg = new ImageView(gma);
                            aniImg.setBackgroundDrawable(image.getDrawable());
                            Tools.addShopCard(gma, "addshopcar", image,
                                    gma.shopcard, aniImg, requestParams, false);
                        } else
                        {
                            if (context instanceof SearchGoodsActivity)
                            {
                                SearchGoodsActivity gma = (SearchGoodsActivity) context;
                                ImageView aniImg = new ImageView(gma);
                                aniImg.setBackgroundDrawable(image.getDrawable());
                                Tools.addShopCard(gma, "addshopcar", image, gma.shopcartBtn, aniImg,
                                        requestParams, false);
                            }
                        }

                    }

                }
            });
        }
        time.setText(bean.getGoodsName());
        if (oldprice)
        {
            price.setText("￥" + bean.getGoodsTejiaPrice());
        } else
        {
            price.setText("￥" + bean.getGoodsPrice());
        }

        image.setImageUrl(bean.getGoodsPic(), R.drawable.zhanwei);

        if (!swipe)
        {
            layout.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context,
                            GoodDetailActivity.class);
                    intent.putExtra("goods_id", bean.getGoodsId());
                    context.startActivity(intent);
                }
            });
        }
        if (oldprice)
        {
            old_price.setVisibility(View.VISIBLE);
            old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            old_price.setText("￥" + bean.getGoodsOldPrice());
        }
        buy.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, BuyNowActivity.class);
                intent.putExtra("goodId", bean.getGoodsId());
                intent.putExtra("goodName", bean.getGoodsName());
                intent.putExtra("storeId", bean.getStoreId());
                intent.putExtra("storeName", bean.getStoreName());
                intent.putExtra("quantity", "1");
                intent.putExtra(
                        "goodPrice",
                        oldprice ? bean.getGoodsTejiaPrice() : bean
                                .getGoodsPrice());
                context.startActivity(intent);
                AnimUtil.animToSlide((Activity) context);

            }
        });
    }
}
