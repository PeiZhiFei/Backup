package com.runkun.lbsq.busi.wxapi.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BuyNowActivity;
import com.runkun.lbsq.activity.MyAddressActivity;
import com.runkun.lbsq.activity.ShowOrderActivity;
import com.runkun.lbsq.activity.UsableCouponActivity;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.view.CounterHolder;
import com.runkun.lbsq.view.CounterHolder.CounterCallback;
import com.runkun.lbsq.wxapi.IWXPay;
import com.runkun.lbsq.wxapi.WXPayEntryActivity;
import com.runkun.lbsq.wxapi.WXPayUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import library.util.AnimUtil;
import library.util.L;
import library.widget.ActionSheet;
import project.base.BaseFragment;


public class BuyNowFragment extends BaseFragment implements
        ActionSheet.MenuItemClickListener {
    private CounterHolder countHolder;

    private float adjCount = 0;
    private float adjScore = 0;
    private float adjHongbao = 0;

    private String goodId;
    private String goodName;
    private String storeId;
    private String storeName;
    private float orderCount;
    private String goodPrice;

    private String orderSN;
    private String tradeSN;

    private ActionSheet menuView;

    private String memberId;

    private float totalHongbao = 0;
    private float totalFee = 0;
    private float alisa;
    private float fare = 0;
    private float payFare = 0;
    private int jfdk = 0;
    private int payType = 0; // 0 未知 1 微信 2阿里
    private String mUnit = "";


    private JSONObject couponsJSON;
    private JSONArray couponsJA;
    private String selectedCouponId;
    private int selectedAmount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_now, container, false);
        return view;
    }

    private void initView() {
        Bundle argBundle = getArguments();
        goodId = argBundle.getString("goodId");
        goodName = argBundle.getString("goodName");
        storeId = argBundle.getString("storeId");
        storeName = argBundle.getString("storeName");
        orderCount = Float.valueOf(argBundle.getString("quantity"));
        goodPrice = argBundle.getString("goodPrice");

        adjCount = orderCount;

        memberId = HttpHelper.getPrefParams(getActivity(), "memberId");

        buyGoodNameTV.setText(goodName);
        buyShopNameTV.setText(storeName);
        buyPriceTV.setText(goodPrice);
        buyCountTV.setText(String.valueOf(orderCount));
        totalFee = Float.valueOf(goodPrice) * orderCount;

        BigDecimal bd = new BigDecimal(totalFee);
        totalFee = bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        buyTotalFeeTV.setText(String.valueOf(totalFee));
        realFeeTV.setText(String.valueOf(totalFee));


        initCounter();
        dialogProgress("请稍候");
        queryUsableCoupon();
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("member_id",
                HttpHelper.getPrefParams(getActivity(), "memberId"));
        rp.addQueryStringParameter("store_id", storeId);
        HttpHelper.postByCommand("findstoreinfo", rp,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Tools.toast(getActivity(), arg1);
                        dialogDismiss();
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(ResponseInfo<String> resp) {
                        try {
                            JSONObject jsonStore = new JSONObject(resp.result);
                            if (HttpHelper.isSuccess(jsonStore)) {
                                JSONObject jo = jsonStore.getJSONArray("datas")
                                        .getJSONObject(0);

                                int goodIdInt = Integer.valueOf(goodId);

                                alisa = Float.valueOf(jo.getString("alisa"));    //jo.getInt("alisa")
                                fare = Float.valueOf(jo.getString("fare"));    //jo.getInt("fare")

                                if (goodIdInt < 7) {
                                    alisa = 72;
                                }
                                if (goodIdInt < 4) {
                                    alisa = 29.5f;
                                }

                                if (alisa > totalFee) {

                                    if (fare == 0) {
                                        Tools.dialog(getActivity(), "非常抱歉，该商家不对未满起送额度的订单提供派送服务，或许您可以选择更多您喜欢的商品", true, new onButtonClick() {
                                            @Override
                                            public void buttonClick() {
                                                getActivity().finish();
                                            }
                                        });
                                        dialogDismiss();
                                        return;
                                    }

                                    payFare = fare;
                                    String msg = String.format(
                                            "您消费金额少于起送金额%2.2f元，需要支付%2.2f元外送费！",
                                            alisa, fare);
                                    final PromptDialog pDialog = PromptDialog
                                            .create(getActivity(), "提示", msg,
                                                    PromptDialog.TYPE_CONFIRM_CANCEL);
                                    pDialog.setConfirmButton("确定",
                                            new OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    pDialog.dismiss();

                                                    realFeeTV.setText(String.valueOf(totalFee + payFare - selectedAmount));
                                                }

                                            })
                                            .setCancelButton("取消",
                                                    new OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                View v) {
                                                            getActivity().finish();
                                                        }

                                                    }).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogDismiss();
                        }
                        dialogDismiss();
                    }

                });
    }

    String is_use = "";

    /**
     * 查询可用优惠券，以便提示用户有优惠券可以使用
     */
    private void queryUsableCoupon() {
        RequestParams rp = new RequestParams();

        rp.addQueryStringParameter("member_id", memberId);
        L.l("member_id=====" + memberId);
        rp.addQueryStringParameter("store_id", storeId);
        L.l("store_id=====" + storeId);
        rp.addQueryStringParameter("phone_code", Tools.getUDID(mActivity));
//        rp.addQueryStringParameter ("phone_code", "2C1BA408-1427-4BB1-A313-1F0043BD0416");
        L.l("phone_code=====" + Tools.getUDID(mActivity));
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                L.l(result);
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    if (HttpHelper.isSuccess(resultJSON)) {
                        couponsJSON = resultJSON;
                        L.l("coopjson====" + couponsJSON);
                        is_use = resultJSON.getString("is_use");
//                        if (is_use.equals("1"))
//                        {
//                            //todo 今天已用过
//                            couponTV.setTag("USED");
//                        }
                        JSONArray ja = resultJSON.getJSONArray("datas");
                        couponsJA = ja;
                        L.l("couponsJA====" + couponsJA);
//                        couponTV.setText(String.format("%d张可用优惠券", ja.length()));
                        if (ja.length() > 0) {
                            couponTV.setText(String.format("%d张可用优惠券", ja.length()));
                        }
                    }
                } catch (JSONException ex) {

                } finally {
                    dialogDismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Tools.toast(getActivity(), "系统繁忙，请稍候重试");
                dialogDismiss();
            }
        };
//todo
//        HttpHelper.sendByPost("http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/api_coupon.php?commend=member_coupons_isok", rp, callBack);
        HttpHelper.sendByPost("http://app.lingbushequ.com/mobile/api_coupon.php?commend=member_coupons_isok", rp, callBack);

    }

    private int by = 1;

    private void initCounter() {
        // 处理相关参数
        Bundle argBundle = getArguments();
        mUnit = getArg(argBundle, "unit", "");
        float step = Float.valueOf(getArg(argBundle, "step", "1"));
        float min = Float.valueOf(getArg(argBundle, "min", "1"));
        float max = Float.valueOf(getArg(argBundle, "max", "99"));
        by = Integer.valueOf(getArg(argBundle, "by", "1"));

        float count = orderCount * by;
        buyCountTV.setText(String.valueOf(count) + mUnit);

        countHolder = new CounterHolder(orderCount, step, min, max,
                new CounterCallback() {

                    @Override
                    public void newCount(float newCount) {
                        adjCount = newCount;
                        float count = adjCount * by;
                        buyCountTV.setText(String.valueOf(count) + mUnit);
                        BigDecimal bd = new BigDecimal(goodPrice);
                        bd = bd.multiply(new BigDecimal(String
                                .valueOf(adjCount)));
                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                        totalFee = bd.floatValue();
                        buyTotalFeeTV.setText(String.valueOf(totalFee));

                        if (totalFee > alisa) {
                            payFare = 0;
                        } else {
                            payFare = fare;
                        }

                        realFeeTV.setText(String.valueOf(totalFee + payFare));
                        if (couponsJA != null && couponsJA.length() > 0) {
                            couponTV.setText(String.format("%d张可用优惠券", couponsJA.length()));
                        }
                        selectedCouponId = null;
                        selectedAmount = 0;

                    }

                    @Override
                    public void requestShopCard(boolean plus, float c) {

                    }

                });
        ViewUtils.inject(countHolder, countCounter);
        countHolder.setCount(orderCount);
    }

    private String getArg(Bundle arg, String key, String defaultStr) {
        String result = arg.getString(key);
        if (result == null) {
            result = defaultStr;
        }
        return result;
    }

    @OnClick({R.id.buy_order_btn, R.id.select_addr, R.id.sel_coupon_btn, R.id.tv_coupon})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_addr:
                Intent tIntent = new Intent(getActivity(), MyAddressActivity.class);
                startActivity(tIntent);
                break;
            case R.id.buy_order_btn:
                ifOnlineNext(storeId);
                break;
            case R.id.sel_coupon_btn:
            case R.id.tv_coupon:
                if (couponsJA == null || couponsJA.length() < 1) {
                    Tools.toast(getActivity(), "暂无可用的优惠券");
//                } else if (couponTV.getTag().equals("USED"))
                } else if (is_use.equals("1")) {
                    //todo 一天只能用一次
                    Tools.toast(getActivity(), "一天只能使用一张优惠券");
                } else {
                    Intent cIntent = new Intent();
                    cIntent.putExtra("coupons", couponsJSON.toString());
                    cIntent.putExtra("totalFee", String.valueOf(totalFee));
                    cIntent.putExtra("storeId", storeId);
                    cIntent.setClass(getActivity(), UsableCouponActivity.class);
                    getActivity().startActivityForResult(cIntent, MyConstant.REQUEST_COUPON);
                }
                break;
        }
    }

    //todo    已经有地址的用户第一次登陆会提示没有地址
    private void loadDefaultAddress() {
        String addr = HttpHelper.getPrefParams(getActivity(), "con_address");
        if (addr != null && addr.length() > 0) {
            buyAddrTV.setText(addr);
            buyPersonNameTV.setText(HttpHelper.getPrefParams(getActivity(),
                    "consigner"));
            buyTelNoET.setText(HttpHelper.getPrefParams(getActivity(),
                    "conmobile"));
        }
    }

    /**
     * onResume回调加载地址
     */
    @Override
    public void onResume() {
        super.onResume();
        memberId = HttpHelper.getPrefParams(getActivity(), "memberId");
        loadDefaultAddress();
        if (payType == 1) {
            switch (WXPayEntryActivity.ERR_CODE) {
                case 0:
                    dialogProgress("请稍候");
                    checkOrder();
                    break;
                case -1:
                    Tools.dialog(getActivity(), "支付未完成，请稍候重试", true, null);
                    break;
                case -2:
                    Tools.dialog(getActivity(), "您已取消支付", true, null);
                    break;
            }
            WXPayEntryActivity.ERR_CODE = -3;
        }
    }

    private void createOrder() {
        if ("".equals(memberId)) {
            Tools.dialog(getActivity(), "请先登录", true, new onButtonClick() {
                @Override
                public void buttonClick() {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            return;
        }

        if (checkInput()) {
            RequestParams rp = new RequestParams();

            rp.addQueryStringParameter("goods_id", goodId);
            rp.addQueryStringParameter("member_id", memberId);
            rp.addQueryStringParameter("quantity", String.valueOf(adjCount));
            rp.addQueryStringParameter("goods_name", goodName);
            rp.addQueryStringParameter("store_id", storeId);
            rp.addQueryStringParameter("store_name", storeName);
            rp.addQueryStringParameter("total_fee", buyTotalFeeTV.getText()
                    .toString());
            rp.addQueryStringParameter("con_address", buyAddrTV.getText()
                    .toString());
            rp.addQueryStringParameter("consigner", buyPersonNameTV.getText()
                    .toString());
            rp.addQueryStringParameter("conmobile", buyTelNoET.getText()
                    .toString());
            rp.addQueryStringParameter("conremark", buyCommitsET.getText()
                    .toString());
            rp.addQueryStringParameter("cost_score",
                    String.valueOf((int) adjScore));
            rp.addQueryStringParameter("hongbao", String.valueOf(adjHongbao));
            rp.addQueryStringParameter("fare", String.valueOf(payFare));
            rp.addQueryStringParameter("price", String.valueOf(goodPrice));

            if (!Tools.isEmpty(selectedCouponId)) {
                rp.addQueryStringParameter("couponid", selectedCouponId);
            }

            HttpHelper.postByCommand("addordergoods", rp,
                    new RequestCallBack<String>() {

                        @Override
                        public void onStart() {
                            dialogProgress("请稍候");
                        }

                        @Override
                        public void onFailure(HttpException ex, String msg) {
                            dialogDismiss();
                            Tools.toast(getActivity(), "网络异常，请稍候重试");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> resp) {
                            String result = resp.result;
                            try {
                                JSONObject jsonResult = new JSONObject(result);
                                if (HttpHelper.isSuccess(jsonResult)) {
                                    JSONObject jsonData = jsonResult
                                            .getJSONObject("datas");
                                    orderSN = jsonData.getString("order_sn");
                                    tradeSN = jsonData.getString("trade_sn");

                                    menuView = new ActionSheet(getActivity());

                                    menuView.setCancelButtonTitle("取消");
                                    menuView.addItems("支付宝", "微信");
                                    menuView.setItemClickListener(BuyNowFragment.this);
                                    menuView.setCancelableOnTouchMenuOutside(false);
                                    menuView.showMenu();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialogDismiss();
                            }
                            dialogDismiss();
                        }

                    });
        }
    }

    private boolean checkInput() {
        String addr = buyAddrTV.getText().toString();
        if (Tools.isEmpty(addr)) {
            Tools.toast(getActivity(), "请输入地址");
            AnimUtil.animShakeText(buyAddrTV);
            return false;
        }

        String person = buyPersonNameTV.getText().toString();
        if (Tools.isEmpty(person)) {
            Tools.toast(getActivity(), "请输入联系人");
            AnimUtil.animShakeText(buyPersonNameTV);
            return false;
        }

        String telNo = buyTelNoET.getText().toString();
        if (Tools.isEmpty(telNo)) {
            Tools.toast(getActivity(), "请输入手机号");
            AnimUtil.animShakeText(buyTelNoET);
            return false;
        }
        if (!Tools.checkPhone(telNo)) {
            Tools.toast(getActivity(), "请输入正确的手机号");
            AnimUtil.animShakeText(buyTelNoET);
            return false;
        }

        // 检查积分
        float score2cash = adjScore / jfdk;
        if (score2cash >= totalFee) {
            final PromptDialog pDialog = PromptDialog.create(getActivity(),
                    "提示", "积分抵扣金额需要小于总价", PromptDialog.TYPE_CONFIRM);
            pDialog.setConfirmButton("确定", new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pDialog.dismiss();
                }

            }).show();
            return false;
        }

        // 检查红包
        if (adjHongbao >= totalFee) {
            final PromptDialog pDialog = PromptDialog.create(getActivity(),
                    "提示", "红包抵扣金额需要小于总价", PromptDialog.TYPE_CONFIRM);
            pDialog.setConfirmButton("确定", new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pDialog.dismiss();
                }

            }).show();
            return false;
        }
        if (totalFee > totalHongbao && totalHongbao > 0) {
            adjHongbao = totalHongbao;
            Tools.toast(getActivity(),
                    String.format("已使用红包抵扣%2.2f元", totalHongbao));
        }

        return true;
    }

    @Override
    public void onItemClick(int itemPosition) {
        switch (itemPosition) {
            case 0:
                payByAli();
                break;
            case 1:
                payByWX();
                break;
        }
    }

    private void payByWX() {
        final WXPayUtil wxPayUtil = new WXPayUtil(getActivity());
        wxPayUtil.pay(new IWXPay() {

            @Override
            public String genProductArgs() {
                StringBuffer xml = new StringBuffer();
                String nonceStr = getNonceStr();

                xml.append("</xml>");
                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
                packageParams.add(new BasicNameValuePair("appid",
                        WePayUtil.APP_ID));

                packageParams.add(new BasicNameValuePair("body", goodName));
                packageParams.add(new BasicNameValuePair("mch_id",
                        WePayUtil.MCH_ID));
                packageParams
                        .add(new BasicNameValuePair("nonce_str", nonceStr));
                packageParams.add(new BasicNameValuePair("notify_url",
                        "http://app.lingbushequ.com/mobile/notify_url.php"));
                packageParams.add(new BasicNameValuePair("out_trade_no",
                        orderSN));
                packageParams.add(new BasicNameValuePair("spbill_create_ip",
                        "127.0.0.1"));
                // 换算成分
                int total = 0;
                float ftotal = 0;
                if (jfdk == 0) {
                    ftotal = totalFee * 100 - adjHongbao * 100 + payFare * 100;
                } else {
                    ftotal = totalFee * 100 - adjScore * 100 / jfdk
                            - adjHongbao * 100 + payFare * 100;
                }
                ftotal = Tools.scale(ftotal - selectedAmount * 100);
                total = (int) ftotal;
                // TODO: 2015/9/10 测试修改价格
                packageParams.add(new BasicNameValuePair("total_fee", String
                        .valueOf(total)));
                //packageParams.add(new BasicNameValuePair("total_fee", "0.01"));
                packageParams.add(new BasicNameValuePair("trade_type", "APP"));

                String sign = wxPayUtil.genPackageSign(packageParams);
                packageParams.add(new BasicNameValuePair("sign", sign));

                String xmlstring = wxPayUtil.toXml(packageParams);

                try {
                    return new String(xmlstring.getBytes(), "ISO8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPrePayIdReady(String prePayId) {
                payType = 1;
                wxPayUtil.genPayReq();
                wxPayUtil.sendPayReq();
            }

            @Override
            public void onError(String msg) {
                Log.e("payByWX", msg);
            }

            @Override
            public String getNonceStr() {
                return tradeSN;
            }

        });
    }

    private void payByAli() {
        payType = 2;
        float ftotal;
        if (jfdk != 0) {
            ftotal = totalFee - adjScore / jfdk - adjHongbao + payFare;
        } else {
            ftotal = totalFee - adjHongbao + payFare;
        }
        Intent intent = new Intent();
        // TODO: 2015/9/10
//        intent.putExtra("totalFee", "0.01");// isPage
        intent.putExtra("totalFee", String.valueOf(Tools.scale(ftotal - selectedAmount)));// isPage
        intent.putExtra("goodName", goodName);
        intent.putExtra("goodDetail",
                String.format("%sx%d", goodName, (int) orderCount));
        intent.putExtra("isPage", "now");
        intent.setClass(getActivity(), WepayActivity.class);
        getActivity()
                .startActivityForResult(intent, BuyNowActivity.REQUEST_ALI);
    }

    public void callback(boolean success) {
        if (success) {
            dialogProgress("请稍候");
            updateOrder();
        } else {
            Tools.dialog(getActivity(), "支付未完成，请稍候重试", true, null);
        }
    }

    private int retry = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (retry++ < 3) {
                checkOrder();
            } else {
                dialogDismiss();
            }
        }
    };

    // 检查订单支付状态
    private void checkOrder() {
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("order_sn", orderSN);
        rp.addQueryStringParameter("phone_code", Tools.getUDID(mActivity));
        // TODO: 2015/9/10
        HttpHelper.sendByPost(MyConstant.FindorderStatus, rp,
                // HttpHelper.sendByPost ("http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/api.php?commend=findorderstatus", rp,
//                HttpHelper.postByCommand ("findorderstatus", rp,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp) {
                        String result = resp.result;
                        try {
                            JSONObject jsonResult = new JSONObject(result);
                            result = jsonResult.getJSONObject("datas")
                                    .getString("paystatus");
                            if (result.equals("success")) {
                                dialogDismiss();
                                showOrder();
                            } else {
                                handler.sendMessageDelayed(
                                        handler.obtainMessage(), 1500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    private void updateOrder() {
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("order_sn", orderSN);
        rp.addQueryStringParameter("total_fees", String.valueOf(totalFee));
        rp.addQueryStringParameter("cost_score", String.valueOf((int) adjScore));
        rp.addQueryStringParameter("hongbao", String.valueOf(adjHongbao));
        rp.addQueryStringParameter("pay_type", "1");
        HttpHelper.postByCommand("updatestatus", rp,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp) {
                        String result = resp.result;
                        try {
                            JSONObject jsonResult = new JSONObject(result);
                            result = jsonResult.getJSONObject("datas")
                                    .getString("success");
                            if (result.equals("true")) {
                                dialogDismiss();
                                showOrder();
                            } else {
                                handler.sendMessageDelayed(
                                        handler.obtainMessage(), 1500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    private void offlinePrompt() {
        final PromptDialog pDialog = PromptDialog.create(getActivity(), "提示",
                "亲!店家已打烊,若有急需请电联", PromptDialog.TYPE_CONFIRM_CANCEL);
        pDialog.setConfirmButton("打电话", new OnClickListener() {

            @Override
            public void onClick(View v) {
                String storeTel = HttpHelper.getPrefParams(getActivity(),
                        Shop.SHOPPHONE);
                if ("".equals(storeTel)) {
                    Tools.toast(getActivity(), "联系方式有误！");
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + storeTel));
                    startActivity(intent);
                    pDialog.dismiss();
                }
            }

        }).setCancelButton("取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                getActivity().finish();
            }

        }).show();
    }

    private void ifOnlineNext(String storeId) {
        dialogProgress("请稍候");
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("store_id", storeId);

        HttpHelper.postByCommand("isopenstore", rp,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dialogDismiss();
                        Tools.toast(getActivity(), "请检查您的网络或稍候重试");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp) {
                        String result = resp.result;
                        try {
                            JSONObject jsonResult = new JSONObject(result);
                            String isopen = jsonResult.optString("isopen", "0");
                            if ("1".equals(isopen)) {
                                createOrder();
                                dialogDismiss();
                            } else {
                                offlinePrompt();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogDismiss();
                        }
                        dialogDismiss();
                    }

                });
    }

    private void showOrder() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ShowOrderActivity.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        intent.putExtra("consigner", buyPersonNameTV.getText().toString());
        intent.putExtra("mobile", buyTelNoET.getText().toString());
        intent.putExtra("address", buyAddrTV.getText().toString());
        intent.putExtra("time", sdf.format(new Date()));
        intent.putExtra("orderFee", String.valueOf(totalFee));
        intent.putExtra("fare", String.valueOf(payFare));
        intent.putExtra("payMethod", payType == 1 ? "微信支付" : "支付宝支付");

        startActivity(intent);
        AnimUtil.animBackSlideFinish(getActivity());
    }

    public void setCoupon(String id, String amount) {
        if (Tools.isEmpty(id) || Tools.isEmpty(amount)) {
            return;
        }
        couponTV.setText("已使用优惠券抵扣￥" + amount);
        selectedCouponId = id;
        selectedAmount = Integer.valueOf(amount);
        realFeeTV.setText(String.valueOf(totalFee + payFare - selectedAmount));
    }
}
