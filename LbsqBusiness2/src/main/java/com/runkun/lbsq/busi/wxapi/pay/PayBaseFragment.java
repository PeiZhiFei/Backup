package com.runkun.lbsq.busi.wxapi.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.wxapi.IWXPay;
import com.runkun.lbsq.busi.wxapi.WXPayEntryActivity;
import com.runkun.lbsq.busi.wxapi.WXPayUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import library.util.L;
import library.util.Tools;
import project.base.BaseFragment;


public class PayBaseFragment extends BaseFragment implements View.OnClickListener
{

    public String redMoney;
    protected String totalFees;
    protected JSONObject couponsJSON;
    protected JSONArray couponsJA;

    protected WepayActivity wepayActivity;
    protected ImageView logo;
    protected View view;
    protected Button pay;
    protected TextView fare;
    protected TextView goodMoney;
    public TextView goodName;
    public EditText goodDescribe;
    protected ImageButton selectCouponBtn;
    public TextView shMoney;
    protected TextView redMoneyv;


    protected String selectedCouponId;
    protected int selectedAmount = 0;

    int type = 1;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        wepayActivity = (WepayActivity) activity;
    }

    public static PayBaseFragment newInstance(int type)
    {
        PayBaseFragment payBaseFragment = new PayBaseFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        payBaseFragment.setArguments(args);
        return payBaseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //todo
        type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.pay_external, container, false);
        logo = (ImageView) view.findViewById(R.id.logo);
        pay = (Button) view.findViewById(R.id.pay);
        fare = (TextView) view.findViewById(R.id.fare);
        goodMoney = (TextView) view.findViewById(R.id.good_money);
        goodName = (TextView) view.findViewById(R.id.good_name);
        goodDescribe = (EditText) view.findViewById(R.id.good_describe);
        shMoney = (TextView) view.findViewById(R.id.mony);
        redMoneyv = (TextView) view.findViewById(R.id.red_money);
        selectCouponBtn = (ImageButton) view.findViewById(R.id.sel_coupon_btn);


        logo.setImageResource(type == 1 ? R.drawable.pay_ali : R.drawable.pay_wechat);
        dialogInit();

        pay.setOnClickListener(this);
        totalFees = String.valueOf(wepayActivity.totalFee + wepayActivity.fareInt);
        SharedPreferences sp = getActivity().getSharedPreferences("lbsq",
                Context.MODE_PRIVATE);
        String difineGood = sp.getString("selectStoreName", "");
        goodName.setText("".equals(difineGood) ? "" : difineGood);
        String buyedGood = sp.getString("buyedGood", "");
        goodDescribe.setText("".equals(buyedGood) ? "" : buyedGood);

        fare.setText("￥" + String.valueOf(wepayActivity.fareInt));

        redMoneyv.setText("0张可用优惠券");
        goodMoney.setText(String.valueOf(wepayActivity.totalFee));
        String sPay = getActivity().getIntent().getStringExtra("sPay");
        shMoney.setText("￥" + (null == sPay ? totalFees : sPay));

        redMoneyv.setOnClickListener(this);
        selectCouponBtn.setOnClickListener(this);

        queryUsableCoupon();
        return view;
    }

    /**
     * 查询可用优惠券，以便提示用户有优惠券可以使用
     */
    private void queryUsableCoupon()
    {
        dialogProgress("请稍候");

        RequestParams rp = new RequestParams();

        rp.addQueryStringParameter("member_id", wepayActivity.memberId);

        L.l("member_id==" + wepayActivity.memberId);
        rp.addQueryStringParameter("store_id", wepayActivity.storeIds);
        L.l ("store_id==" + wepayActivity.storeIds);
        rp.addQueryStringParameter("phone_code", Tools.getUDID(mActivity));
        // rp.addQueryStringParameter ("phone_code",  "123");

        L.l("phone_code==" + Tools.getUDID(mActivity));

        RequestCallBack<String> callBack = new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;
                L.l(result);
                try
                {
                    JSONObject resultJSON = new JSONObject(result);
                    if (HttpHelper.isSuccess(resultJSON))
                    {
                        couponsJSON = resultJSON;
                        L.l("couponsJSON===" + couponsJSON);
                        is_use = resultJSON.getString("is_use");
                        //                        if (is_use.equals("1"))
                        //                        {
                        //                            //todo 今天已用过
                        //                            redMoneyv.setTag("USED");
                        //                        }
                        JSONArray ja = resultJSON.getJSONArray("datas");
                        couponsJA = ja;
                        L.l("ja====" + ja);
                        if (ja.length() > 0)
                        {
                            redMoneyv.setTextColor(Color.rgb(255, 0, 0));
                            redMoneyv.setText(String.format("%d张可用优惠券", ja.length()));
                        }
                    }
                } catch (JSONException ex)
                {
                    Tools.toast(getActivity(), "系统繁忙，请稍候重试");
                } finally
                {
                    dialogDismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                dialogDismiss();
            }
        };

        //    HttpHelper.sendByPost("http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/api_coupon.php?commend=member_coupons_isok", rp, callBack);
        // TODO: 2015/9/10
        HttpHelper.sendByPost("http://app.lingbushequ.com/mobile/api_coupon.php?commend=member_coupons_isok", rp, callBack);
    }

    public void setCoupon(String id, String amount)
    {
        if (Tools.isEmpty(id) || Tools.isEmpty(amount))
        {
            return;
        }
        redMoneyv.setText("已使用优惠券抵扣￥" + amount);
        selectedCouponId = id;
        selectedAmount = Integer.valueOf(amount);
        shMoney.setText("￥" + String.valueOf(Tools.scale(wepayActivity.totalFee + wepayActivity.fareInt - selectedAmount)));
    }

    String is_use = "";

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.pay:
                createOrder();
                break;
            case R.id.red_money:
            case R.id.sel_coupon_btn:
                if (couponsJA == null || couponsJA.length() < 1)
                {
                    Tools.toast(getActivity(), "暂无可用的优惠券");
                } else if (is_use.equals("1"))
                {
                    //todo 一天只能用一次
                    Tools.toast(getActivity(), "一天只能使用一张优惠券");
                } else
                {
                    Intent cIntent = new Intent();
                    cIntent.putExtra("coupons", couponsJSON.toString());
                    cIntent.putExtra("totalFee", String.valueOf(wepayActivity.totalFee));
                    cIntent.putExtra("storeId", wepayActivity.storeIds);
                    cIntent.setClass(getActivity(), UsableCouponActivity.class);
                    getActivity().startActivityForResult(cIntent, MyConstant.REQUEST_COUPON);
                }
                break;
        }

    }

    public void createOrder()
    {
        dialogProgress("请稍候");
        wepayActivity.createOrder(selectedCouponId, selectedAmount, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;
                try
                {
                    JSONObject resultJson = new JSONObject(result);
                    if (HttpHelper.isSuccess(resultJson))
                    {
                        JSONObject orderJson = resultJson.getJSONArray("datas").getJSONObject(0);
                        if (type == 1)
                        {
                            wepayActivity.outTradeNo = orderJson.getString("order_sn");
                            wepayActivity.pay();
                        } else if (type == 2)
                        {
                            outTradeNo = orderJson.getString("order_sn");
                            float payFee = Tools.scale(wepayActivity.totalFee + wepayActivity.fareInt - selectedAmount);
                            int total = (int) (payFee * 100);
                            //
                            payByWX(goodName.getText().toString(), String.valueOf(total), goodDescribe.getText().toString(), outTradeNo);
                        }

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                } finally
                {
                    dialogDismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                Tools.toast(getActivity(), "系统繁忙，请稍候重试");
                dialogDismiss();
            }
        });
    }

    private String outTradeNo;

    /**
     * @param body       标题
     * @param totalFee   总费用（纯商品）
     * @param detail     描述
     * @param outTradeNo 订单号
     */
    private void payByWX(final String body, final String totalFee, final String detail, final String outTradeNo)
    {
        final WXPayUtil wxPayUtil = new WXPayUtil(getActivity());
        wxPayUtil.pay(new IWXPay()
        {

            @Override
            public String genProductArgs()
            {
                StringBuffer xml = new StringBuffer();
                String nonceStr = getNonceStr();

                xml.append("</xml>");
                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
                packageParams.add(new BasicNameValuePair("appid",
                        WePayUtil.APP_ID));

                packageParams.add(new BasicNameValuePair("body", body));
                packageParams.add(new BasicNameValuePair("detail", detail));

                packageParams.add(new BasicNameValuePair("mch_id",
                        WePayUtil.MCH_ID));
                packageParams.add(new BasicNameValuePair("nonce_str",
                        nonceStr));
                packageParams.add(new BasicNameValuePair("notify_url",
                        MyConstant.WECHAT_PAY_NOTIFY_URL));
                packageParams.add(new BasicNameValuePair("out_trade_no",
                        outTradeNo));
                packageParams.add(new BasicNameValuePair(
                        "spbill_create_ip", "127.0.0.1"));

                // TODO: 2015/9/10 测试价格
                packageParams.add(new BasicNameValuePair("total_fee",
                        String.valueOf(totalFee)));

                //微信支付价格使用分为单位
                // packageParams.add (new BasicNameValuePair ("total_fee",
                //        "1"));

                packageParams.add(new BasicNameValuePair("trade_type",
                        "APP"));

                String sign = wxPayUtil.genPackageSign(packageParams);

                packageParams.add(new BasicNameValuePair("sign", sign));

                String xmlstring = wxPayUtil.toXml(packageParams);

                try
                {
                    return new String(xmlstring.getBytes(), "ISO8859-1");
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPrePayIdReady(String prePayId)
            {
                wxPayUtil.genPayReq();
                wxPayUtil.sendPayReq();
            }

            @Override
            public void onError(String msg)
            {
                Log.e("payByWX", msg);
            }

            @Override
            public String getNonceStr()
            {
                return outTradeNo;
            }

        });
    }

    private int retry = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (retry++ < 3)
            {
                checkOrder();
            } else
            {
                dialogDismiss();
            }
        }
    };

    // 检查订单支付状态
    private void checkOrder()
    {
        RequestParams rp = new RequestParams();
        //todo order_sn
        rp.addQueryStringParameter("order_sn", outTradeNo);
        L.l(outTradeNo);
        rp.addQueryStringParameter("phone_code", Tools.getUDID(mActivity));
        // TODO: 2015/9/10
        HttpHelper.postByCommand(MyConstant.FindorderStatus, rp,
                // HttpHelper.sendByPost ("http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/api.php?commend=findorderstatus", rp,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        try
                        {
                            JSONObject jsonResult = new JSONObject(result);
                            result = jsonResult.getJSONObject("datas")
                                    .getString("paystatus");
                            if (result.equals("success"))
                            {
                                dialogDismiss();
                                Intent intent = new Intent();
                                intent.putExtra("result", "true");
                                getActivity().setResult(
                                        Activity.RESULT_OK, intent);
                                getActivity().finish();
                            } else
                            {
                                handler.sendMessageDelayed(
                                        handler.obtainMessage(), 1500);
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (type == 2)
        {
            switch (WXPayEntryActivity.ERR_CODE)
            {
                case 0:
                    dialogProgress("请稍候");
                    checkOrder();
                    //                    Intent intent = new Intent();
                    //                    intent.putExtra("result", "true");
                    //                    getActivity().setResult(
                    //                            Activity.RESULT_OK, intent);
                    //                    getActivity().finish();
                    break;
                case -1:
                    final PromptDialog pDialog = PromptDialog.create(getActivity(),
                            "提示", "支付未完成，请稍候重试", PromptDialog.TYPE_CONFIRM);
                    pDialog.setConfirmButton("确定", new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            pDialog.dismiss();
                        }

                    }).show();
                    break;
                case -2:
                    final PromptDialog pCDialog = PromptDialog.create(getActivity(),
                            "提示", "您已取消支付", PromptDialog.TYPE_CONFIRM);
                    pCDialog.setConfirmButton("确定", new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            pCDialog.dismiss();
                        }

                    }).show();
                    break;
            }
            WXPayEntryActivity.ERR_CODE = -3;
        }
    }

}
