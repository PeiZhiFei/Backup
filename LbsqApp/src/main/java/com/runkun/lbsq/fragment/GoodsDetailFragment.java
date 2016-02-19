package com.runkun.lbsq.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.activity.JudgeListActivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.bean.Comment;
import com.runkun.lbsq.bean.Good;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.BadgeView;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;
import com.runkun.lbsq.view.GoodCombineView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;
import feifei.project.util.ToastUtil;

public class GoodsDetailFragment extends BaseFragment
{
    @ViewInject(R.id.gd_fav_btn)
    private ImageButton favBtn;

    @ViewInject(R.id.order_count)
    private TextView orderCountTV;

    @ViewInject(R.id.gd_add2shopcart)
    private Button add2ShopcartBtn;

    @ViewInject(R.id.gd_img)
    private ImageView goodPicIV;

    @ViewInject(R.id.gd_name)
    private TextView goodNameTV;

    @ViewInject(R.id.gd_price)
    private TextView goodPriceTV;

    @ViewInject(R.id.gd_guess_cc)
    private LinearLayout howGuess;

    @ViewInject(R.id.with_comment)
    private LinearLayout commentInGD;

    @ViewInject(R.id.no_comment)
    private TextView nocommentTipTV;

    @ViewInject(R.id.judge_user_nick)
    private TextView memberNameTV;

    @ViewInject(R.id.judge_content)
    private TextView commentTV;

    @ViewInject(R.id.rationbar)
    private RatingBar ratingBar;

    @ViewInject(R.id.shopcart_btn)
    private ImageButton shopcartBtn;

    @ViewInject(R.id.rl_shopcart)
    private View shopcartContainer;

    private String goodId;
    private String goodName;
    private String goodPrice;
    private String goodPic;

    private int orderCount = 1;

    GoodDetailActivity activity;
    String memberId;


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach (activity);
        this.activity = (GoodDetailActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView (inflater, container, savedInstanceState);
        if (!(getActivity () instanceof GoodDetailActivity))
        {
            return null;
        }
        View view = inflater.inflate (R.layout.fragment_good_detail, container,
                false);
        ViewUtils.inject (this, view);
        dialogInit ();
        memberId = ConfigUtil.readString (activity, MyConstant.KEY_MEMBERID, "");
        goodId = getArguments ().getString ("goods_id");
        queryGoodDetailById (goodId);
        favBtn.startAnimation (AnimUtil.getRotateAnimation (0, 360, 300));

        String countStr = HttpHelper.getPrefParams (getActivity (), "shopcount");
        if (!"".equals (countStr))
        {
            int count = Integer.valueOf (countStr);
            refreshShopcart (count);
        }

        return view;
    }


    private void queryGoodDetailById(String goodId)
    {
        dialogProgress ( Tools.getStr (fragment, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("goods_id", goodId);
        HttpHelper.postByCommand ("goodsdetail", rp,
                new RequestCallBack<String> ()
                {
                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            String code = jsonResult.getString ("code");
                            if ("200".equals (code))
                            {
                                JSONObject datas = jsonResult
                                        .getJSONObject ("datas");
                                loadGoodDetail (datas);
                            }
                            dialogDismiss ();
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                            dialogDismiss ();
                        }
                    }

                    private void loadGoodDetail(JSONObject json)
                    {
                        try
                        {
                            goodPic = json.getString ("goods_pic");
                            if (getActivity () != null)
                            {
                                new XUtilsImageLoader (getActivity (), R.drawable.zhanwei, R.drawable.zhanwei,
                                        true, false).display (goodPicIV, goodPic);

                                goodName = json.getString ("goods_name");

                                String tjprice = getActivity ().getIntent ()
                                        .getStringExtra ("tjprice");

                                goodPrice = json.getString ("goods_price");

                                if (tjprice != null)
                                {
                                    goodPrice = tjprice;
                                }

                                goodNameTV.setText (goodName);
                                goodPriceTV.setText (goodPrice);

                                JSONArray comments = json
                                        .getJSONArray ("goodscomment");
                                if (comments.length () > 0)
                                {
                                    Comment comment = Comment
                                            .from (comments.getJSONObject (0));
                                    memberNameTV.setText (comment
                                            .getMemberName ());
                                    commentTV.setText (comment.getComment ());
                                    ratingBar.setRating (comment.getFlowerNum ());
                                } else
                                {
                                    getActivity ().findViewById (R.id.title_comment).setVisibility (View.GONE);
                                    commentInGD.setVisibility (View.GONE);
                                    nocommentTipTV.setVisibility (View.VISIBLE);
                                }

                                JSONArray likeGoods = json
                                        .getJSONArray ("likegoods");
                                for (int i = 0; i < likeGoods.length (); i++)
                                {
                                    Good entity = json2Entity (likeGoods
                                            .getJSONObject (i));
                                    GoodCombineView itemView = new GoodCombineView (getActivity ());
                                    itemView.setTitleForText (entity);
                                    howGuess.addView (itemView, new LinearLayout.LayoutParams (Tools.getWidth (getActivity ()) / 3, ViewGroup.LayoutParams.WRAP_CONTENT));
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                    }

                    private Good json2Entity(JSONObject jo)
                    {
                        Good entity = new Good ();
                        try
                        {
                            entity.setGoodsId (jo.getString ("goods_id"));
                            entity.setGoodsName (jo.getString ("goods_name"));
                            entity.setGoodsPic (jo.getString ("goods_pic"));
                            entity.setGoodsPrice (jo.getString ("goods_price"));
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        return entity;
                    }

                });
    }

    @OnClick({R.id.gd_fav_btn, R.id.minus_btn,
            R.id.shopcart_btn, R.id.plus_btn, R.id.gd_add2shopcart,
            R.id.more_judge})
    public void onClick(View v)
    {
        switch (v.getId ())
        {
            case R.id.gd_fav_btn:
                if (Tools.isEmpty (memberId))
                {
                    activity.startActivity (new Intent (activity, LoginActivity.class));
                    AnimUtil.animToSlide (activity);
                } else
                {
                    add2MyFav ();
                }

                break;
            case R.id.minus_btn:
                orderCountTV.setText (String.valueOf (orderCount == 1 ? orderCount
                        : --orderCount));
                break;
            case R.id.plus_btn:
                orderCountTV.setText (String.valueOf (orderCount == 99 ? orderCount
                        : ++orderCount));
                break;
            case R.id.gd_add2shopcart:
                //                v.setEnabled(false);
                if (Tools.isEmpty (memberId))
                {
                    activity.startActivity (new Intent (activity, LoginActivity.class));
                    AnimUtil.animToSlide (activity);
                } else
                {
                    add2Shopcart ();
                }
                break;
            case R.id.shopcart_btn:
                Intent intent = new Intent (getActivity (), MainActivity.class);
                intent.putExtra ("types", "shopcard");
                startActivity (intent);
                AnimUtil.animBackSlideFinish (getActivity ());
                break;
            case R.id.more_judge:
                Intent jIntent = new Intent ();
                jIntent.putExtra ("goodId", goodId);
                jIntent.setClass (getActivity (), JudgeListActivity.class);
                startActivity (jIntent);
                break;
        }
    }

    private void add2MyFav()
    {
        RequestParams rp = new RequestParams ();

        rp.addQueryStringParameter ("member_id", memberId);
        rp.addQueryStringParameter ("goods_id", goodId);
        rp.addQueryStringParameter ("goods_name", goodName);
        rp.addQueryStringParameter ("goods_price", goodPrice);
        rp.addQueryStringParameter ("goods_pic", goodPic);

        HttpHelper.postByCommand ("favorite_goods", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        ToastUtil.toast (getActivity (), arg1);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            int code = jsonResult.getInt ("code");
                            switch (code)
                            {
                                case 200:
                                    Tools.toast (activity, Tools.getStr (fragment,
                                            R.string.FAVADDSUCCESS));
                                    break;
                                case 201:
                                    Tools.toast (activity, Tools.getStr (fragment,
                                            R.string.FAVADDFAIL));
                                    break;
                                case 203:
                                    Tools.toast (activity, Tools.getStr (fragment,
                                            R.string.FAVFULL));
                                    break;
                                case 202:
                                    Tools.toast (activity, Tools.getStr (fragment,
                                            R.string.FAVALREADYHAS));
                                    break;
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                    }

                });
    }

    private void add2Shopcart()
    {
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("goods_id", goodId);
        rp.addQueryStringParameter ("quantity", orderCountTV.getText ()
                .toString ());
        rp.addQueryStringParameter ("member_id", memberId);
        HttpHelper.postByCommand ("addshopcar", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        add2ShopcartBtn.setEnabled (true);
                        Tools.toast (getActivity (), arg1);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        add2ShopcartBtn.setEnabled (true);

                        String result = resp.result;
                        L.l (result);
                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            int code = jsonResult.getInt ("code");
                            switch (code)
                            {
                                case 200:
                                    int count = jsonResult.getInt ("count");
                                    refreshShopcart (count);
                                    break;
                                case 203:
                                    Tools.toast (getActivity (), Tools.getStr (
                                            fragment, R.string.SHOPCARDFULL));
                                    break;
                                case 205:
                                    int counts = jsonResult.getInt ("count");
                                    Tools.toast (getActivity (), "超过商品限购数量,商品限购" + counts + "件");
                                    break;
                                case 206:
                                    Tools.toast (getActivity (), "这个商品已经下架了");
                                    break;
                                case 207:
                                    int cou = jsonResult.getInt ("count");
                                    Tools.toast (getActivity (), "超过每日限购数量,商品今日限购" + cou + "件");
                                    break;
                                case 201:
                                    Tools.toast (getActivity (), "请登录吧");
                                    break;
                                case 404:
                                    Tools.toast (getActivity (), Tools.getStr (fragment, R.string.SHOPCARDADDFAIL));
                                    break;
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                    }

                });


    }

    private BadgeView shopcartBadge = null;

    @SuppressLint("RtlHardcoded")
    private void refreshShopcart(int count)
    {
        int[] startLoc = new int[2];
        int[] endLoc = new int[2];
        int width = goodPicIV.getMeasuredWidth ();
        int height = goodPicIV.getMeasuredHeight ();
        final ImageView aniImg = new ImageView (getActivity ());

        aniImg.setBackgroundResource (R.drawable.shape_circle_green);
        aniImg.setBackgroundDrawable (goodPicIV.getDrawable ());
        goodPicIV.getLocationInWindow (startLoc);
        shopcartBtn.getLocationInWindow (endLoc);
        startLoc[0] = startLoc[0] + width / 2;
        startLoc[1] = startLoc[1] + height / 2;

        Tools.animateTo (getActivity (), aniImg, startLoc, endLoc, 300,
                new Animation.AnimationListener ()
                {

                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        aniImg.setVisibility (View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        aniImg.setVisibility (View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                    }

                });


        if (getActivity () != null)
        {
            View b = shopcartContainer.findViewWithTag ("badge");

            if (shopcartBadge == null || b == null)
            {
                BadgeView badgeView = new BadgeView (getActivity ());
                badgeView.setBadgeCount (Integer.valueOf (count));
                badgeView.setBackground (20, Color.RED);
                badgeView.setTextColor (Color.WHITE);
                badgeView.setBadgeGravity (Gravity.RIGHT | Gravity.TOP);
                badgeView.setBadgeMargin (0, 0, 3, 0);
                badgeView.setTargetView (shopcartBtn);
                badgeView.setTag ("badge");
                shopcartBadge = badgeView;
            } else
            {
                shopcartBadge.setBadgeCount (count);
            }

            SharedPreferences sp = MainActivity.mainActivity.getSharedPreferences (
                    MyConstant.FILE_NAME, Context.MODE_PRIVATE);
            Editor editor = sp.edit ();
            editor.putString ("shopcount", String.valueOf (count));
            editor.apply ();
            Tools.refreshShopcartBadge (MainActivity.mainActivity, count);
        }
    }
}
