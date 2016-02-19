package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.OrderMore;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import feifei.project.util.L;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;
import feifei.project.view.MyListView;
import feifei.project.view.smartimage.SmartImageView;

public class OrderInfoActivity extends BaseAcitivity
{
    // TODO: 2015/7/27
    //    String url1 = "http://app.lingbushequ.com/mobile/api_order.php?commend=order";
    //    String url2 = "http://applingbushequ.nat123.net/lingbushequ/src/lso2o/mobile/api_order.php?commend=order";

    TextView shopName, yunfei, youhui, totalPrice, orderSnText, orderTime, orderPay, orderContact, orderPhone, orderAddress;
    MyListView goods;
    View include_judge, layout;
    OrderDetailAdapter orderDetailAdapter;
    TextView my_judge;
    Button judge;
    String orderSn;

    OrderMore orderMores;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_order_info);
        //        ViewUtils.inject (this);
        initActionbar ();
        setTitles ("订单详情");
        tint ();
        dialogInit();
        initViews ();
        orderSn = getIntent ().getStringExtra ("order");
        loadData (orderSn);
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult (requestCode, resultCode, data);
        if ( requestCode == 5 && resultCode == 8 && data != null )
        {
            if ( data.getBooleanExtra ("success", false) )
            {
                judge.setText ("评论加载中……");
                judge.setEnabled (false);
                // judge.setVisibility (View.GONE);
                loadData (orderSn);
            }
        }
    }

    private void initViews ()
    {
        shopName = (TextView) findViewById (R.id.shop_name);
        yunfei = (TextView) findViewById (R.id.price_yunfei);
        youhui = (TextView) findViewById (R.id.price_youhui);
        totalPrice = (TextView) findViewById (R.id.price_total);
        orderSnText = (TextView) findViewById (R.id.order_sn);
        orderTime = (TextView) findViewById (R.id.order_time);
        orderPay = (TextView) findViewById (R.id.order_pay);
        orderContact = (TextView) findViewById (R.id.order_contact);
        orderPhone = (TextView) findViewById (R.id.order_phone);
        orderAddress = (TextView) findViewById (R.id.order_address);
        goods = (MyListView) findViewById (R.id.listview);
        include_judge = findViewById (R.id.include_judge);
        layout = findViewById (R.id.layout);
        my_judge = (TextView) findViewById (R.id.my_judge);
        judge = (Button) findViewById (R.id.judge);
    }


    private void loadData (final String orderSn)
    {
       dialogProgress (activity,
                Tools.getStr (activity, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("order_sn", orderSn);
        HttpUtils httpUtils = new HttpUtils ();
        httpUtils.send (HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL_ORDER + "order", rp,
                new RequestCallBack<String> ()
                {
                    @Override
                    public void onFailure (HttpException arg0, String arg1)
                    {
                       dialogDismiss ();
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        L.l (result);
                        if ( result == null )
                        {
                           dialogDismiss ();
                            return;
                        }

                        JSONObject json = null;
                        try
                        {
                            json = new JSONObject (result);
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        if ( HttpHelper.isSuccess (json) )
                        {
                            try
                            {
                                orderMores = JSON.parseObject (json.getString ("datas"), OrderMore.class);
                            } catch (JSONException e)
                            {
                                e.printStackTrace ();
                            }
                            layout.setVisibility (View.VISIBLE);
                            yunfei.setText ("￥" + orderMores.getFare ());
                            //                                    youhui.setText ("-￥" + orderMores.getDatas ().getHongbao ());
                            youhui.setText ("-￥" + orderMores.getCouponamount ());

                            orderSnText.setText (orderMores.getOrder_sn ());
                            orderTime.setText (Tools.formatMysqlTimestamp (orderMores.getAdd_time (),
                                    "yyyy-MM-dd"));
                            try
                            {
                                orderPay.setText (orderMores.getPay_type ().equals ("1") ? "支付宝" : "微信支付");
                            } catch (NullPointerException e)
                            {
                                //真实环境不会出现这个问题
                            }
                            orderContact.setText (orderMores.getConsigner ());
                            orderPhone.setText (orderMores.getMobile ());
                            orderAddress.setText (orderMores.getMember_address ());
                            if ( orderMores.getGoods_list () != null )
                            {
                                orderDetailAdapter = new OrderDetailAdapter (activity, orderMores.getGoods_list (), R.layout.item_order_det);
                                goods.setAdapter (orderDetailAdapter);
                                //                                    setListViewHeightBasedOnChildren (goods);
                                //                                    startAnimation();
                                float total = 0;
                                // for (int i = 0; i < orderMores.getDatas ().getGoods_list ().size (); i++)
                                for (OrderMore.GoodsListEntity goodsListEntity : orderMores.getGoods_list ())
                                {
                                    // total += Float.parseFloat (orderMores.getDatas ().getGoods_list ().get (i).getTotal_fee ());
                                    total += Float.parseFloat (goodsListEntity.getTotal_fee ());
                                }
                                total = total - Float.parseFloat (orderMores.getCouponamount ()) + Float.parseFloat (orderMores.getFare ());
                                totalPrice.setText ("￥" + Tools.scale (total));

                            }
                            if ( orderMores.getComments () != null )
                            {
                                my_judge.setVisibility (View.VISIBLE);
                                include_judge.setVisibility (View.VISIBLE);
                                judge.setVisibility (View.GONE);
                                ((TextView) findViewById (R.id.judge_user_nick)).setText (orderMores.getMobile ());
                                ((TextView) findViewById (R.id.judge_date)).setText (orderMores.getComments ().getAdd_time ());
                                ((TextView) findViewById (R.id.judge_content)).setText (orderMores.getComments ().getComment ());
                                ((RatingBar) findViewById (R.id.rationbar)).setRating (orderMores.getComments ().getFlower_num ());
                                ((SmartImageView) findViewById (R.id.judge_photo_img)).setImageResource (R.drawable.avatar_default);
                                //                                    orderJudgeAdapter = new OrderJudgeAdapter (activity, orderMores.getDatas ().getComments (), R.layout.item_order_judge);
                                //                                    comments.setAdapter (orderJudgeAdapter);
                            } else
                            {
                                judge.setVisibility (View.VISIBLE);
                                judge.setText ("我要评价");
                                judge.setEnabled (true);
                                my_judge.setVisibility (View.GONE);
                                include_judge.setVisibility (View.GONE);
                                judge.setOnClickListener (new View.OnClickListener ()
                                {
                                    @Override
                                    public void onClick (View view)
                                    {
                                        Intent intent = new Intent (activity, JudgeActivity.class);
                                        intent.putExtra ("ordersn", orderMores.getOrder_sn ());
                                        intent.putExtra ("ordershop", orderMores.getStore_name());
                                        intent.putExtra ("orderimage", orderMores.getStore_pic());
                                        intent.putExtra ("type", "order");
                                        startActivityForResult(intent, 5);
                                        AnimUtil.animToSlide(activity);
                                    }
                                });
                            }
                           dialogDismiss ();
                        }
                    }

                });
    }

    private Animation anim;

    private void startAnimation ()
    {
        // cListview.layout(0, 0, -layout_order_info.getBottom(), 0);
        // height = height + mAdapter.getCount() *
        // BaseTools.dip2px(getApplicationContext(), 70);
        // height = height + (cListview.getDividerHeight() *
        // (cListview.getCount()));
        anim = new TranslateAnimation (0.0f, 0.0f, -totalHeight, 0);
        anim.setDuration (1000);
        anim.setFillAfter (true);
        anim.setAnimationListener (new Animation.AnimationListener ()
        {

            @Override
            public void onAnimationStart (Animation animation)
            {

            }

            @Override
            public void onAnimationRepeat (Animation animation)
            {

            }

            @Override
            public void onAnimationEnd (Animation animation)
            {
                goods.clearAnimation ();
            }
        });
        goods.startAnimation (anim);
    }

    public static int totalHeight = 0;

    public static void setListViewHeightBasedOnChildren (ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter ();
        if ( listAdapter == null )
        {
            return;
        }
        totalHeight = 0;
        Log.d ("listAdapter.getCount()", "" + listAdapter.getCount ());
        for (int i = 0, len = listAdapter.getCount () - 1; i < len; i++)
        {
            View listItem = listAdapter.getView (i, null, listView);
            listItem.measure (0, 0);
            totalHeight += listItem.getMeasuredHeight ();
            Log.d ("getMeasuredHeight", "" + listItem.getMeasuredHeight ());
        }

        totalHeight = totalHeight + (listView.getDividerHeight () * (listAdapter.getCount () - 1));
        // ViewGroup.LayoutParams params = listView.getLayoutParams();
        // params.height = totalHeight + (listView.getDividerHeight() *
        // (listAdapter.getCount() - 1));
        // listView.setLayoutParams(params);
    }


    class OrderDetailAdapter extends MyBaseAdapter<OrderMore.GoodsListEntity>
    {

        public OrderDetailAdapter (Context context, List<OrderMore.GoodsListEntity> datas, int layoutId)
        {
            super (context, datas, layoutId);
        }

        @Override
        protected void convert (MyViewHolder viewHolder, OrderMore.GoodsListEntity bean)
        {
            final TextView textView = viewHolder.getView (R.id.textView);
            final TextView textView2 = viewHolder.getView (R.id.textView2);
            final TextView textView3 = viewHolder.getView (R.id.textView3);

            textView.setText (bean.getItem_name ());
            textView2.setText ("￥" + bean.getPrice ());
            textView3.setText ("x " + (int) (Float.parseFloat (bean.getNumber ())));

        }
    }


    //    class OrderJudgeAdapter extends MyBaseAdapter<OrderMore.DatasEntity.CommentListEntity>
    //    {
    //
    //        public OrderJudgeAdapter(Context context, List<OrderMore.DatasEntity.CommentListEntity> datas, int layoutId)
    //        {
    //            super (context, datas, layoutId);
    //        }
    //
    //        @Override
    //        protected void convert(MyViewHolder viewHolder, OrderMore.DatasEntity.CommentListEntity bean)
    //        {
    //            final TextView textView = viewHolder.getView (R.id.judge_time);
    //            final TextView textView2 = viewHolder.getView (R.id.judge_content);
    //            final RatingBar textView3 = viewHolder.getView (R.id.judge_ratingbar);
    //
    //            textView.setText (bean.getAdd_time ());
    //            textView2.setText (bean.getComment ());
    //            textView3.setRating (Float.valueOf (bean.getFlower_num ()));
    //        }
    //    }


}
