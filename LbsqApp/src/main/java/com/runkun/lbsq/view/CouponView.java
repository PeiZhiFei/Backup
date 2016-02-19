package com.runkun.lbsq.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;


public class CouponView extends LinearLayout implements OnClickListener
{
    private Context context;
    @ViewInject(R.id.coupon)
    private Button conpon;
    private String memberId;
    private String storeId;
    private String money;

    public CouponView(Context paramContext)
    {
        super(paramContext);
        this.context = paramContext;
    }

    public CouponView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
        View view = ((LayoutInflater) paramContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.store_coupon, this, true);
        ViewUtils.inject(this, view);
        conpon.setOnClickListener(this);

    }

    public void setTitleForText(String memberId, String storeId, String money)
    {
        this.memberId = memberId;
        this.storeId = storeId;
        this.money = money;

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.coupon:
                if (Tools.isEmpty(memberId))
                {
                    Tools.dialog(context, "清先登录", true, new onButtonClick()
                    {
                        @Override
                        public void buttonClick()
                        {
                            context.startActivity(new Intent(context, LoginActivity.class));
                            AnimUtil.animToSlide((Activity) context);
                        }
                    });
                }
//                if (Tools.isEmpty(memberId)) {
//                    Tools.toast(context, "登陆后可以领取优惠券");
//                }
                else
                {
                    RequestParams rp = new RequestParams();
                    rp.addQueryStringParameter("store_id", storeId);
                    rp.addQueryStringParameter("member_id", memberId);
                    recivieStoreCoupon(context, MyConstant.API_BASE_URL_COUPON + "couponmember", this, rp);
                }
                break;
        }
    }


    public void recivieStoreCoupon(final Context pram,
                                   String url, final View couponView, RequestParams rp)
    {
        HttpUtils hu = new HttpUtils();
        hu.send(HttpRequest.HttpMethod.POST, url, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        try
                        {
                            JSONObject jsonResult = new JSONObject(result);
                            String code = jsonResult.getString("code");
                            if (code.equals("200"))
                            {
                                couponView.setVisibility(View.GONE);
                                Tools.toast(pram, "恭喜你成功领取" + money + "元优惠券,可在我的优惠券查看");
                            } else if (code.equals("201"))
                            {
                                Tools.toast(pram, "您已领取了优惠券,不能重复领取哦");
                                couponView.setVisibility(View.GONE);
                            } else
                            {
                                Tools.toast(pram, "领取失败,请重新领取");
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(HttpException error, String msg)
                    {
                        Tools.toast(pram, "网络开小差了");
                    }
                }
        );

    }
}