package com.runkun.lbsq.busi.wxapi.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import library.util.AnimUtil;
import library.util.L;


public class WepayActivity extends Activity
{

    private PayBaseFragment fragment;
    private PayBaseFragment wxFragment;

    private SharedPreferences sp;
    public String outTradeNo;
    // 商户PID
    public static final String PARTNER = "2088911144234050";
    // 商户收款账号
    public static final String SELLER = "2638226483@qq.com";
    // 商户私钥，pkcs8格式，暂时用写死的数据
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALzZWwHY/6K3wJ3xYdOgz0O6TJJMK7KqIFuVTvPX4URsmucz2txxXoHqBgmEgrq5q2wHTT+JzmRCqxBYJNJhfQmPUGLIBBZo+/cL/rB7gnydi0PaFkX8hYIMRUPaA4TL10QL60DEizTehUFtF4tzlqrhOPIsxpY/DqudRKNsyOV5AgMBAAECgYB6/+J7gJd3ptDozjfWO63jQervencXpiDvJX9H6LqK82Ws4qRQ4fIZEZCfEFSJQ7b35IWWta3ctWNvgMly0RY7TbUyr2KAvuZtBW5DkhlxGMmCXoGKba2YNHOyhibwq0MzhS/67VqpDv6CJ0l23aMXdMYYsD6F7945Q7FHWeA1pQJBAOrwIL0Me5dJuszpRu4T0kGanpphwF8VEMCtKk8XC111XytPcHWNbiwrnNxVtoe0+BOJUp1yI7Di912eDdHljJcCQQDNx3ouZJI3ZkW/SGiMeP3p8wmcEOaP4QC9yqoIkq0OR8zyARFIWsmnzaTj+sziXrbUGx/TIST58YW1ie83tJBvAkAgFnHyQB01OY245PeaFrz11t9oqIc0tVTXbA9GRBh6SEiaSrxKYem1QLOo6FAI0u+7A1t0Q52aUDWG5Mpwa5C7AkAGZiuggbQiUOXeWuwVYjXTLqGf3s6srryNKl47QgRrq0PuSqY0783RkA2OoV/5siRRWD6XHDLAKwiTaqSrwMu1AkEAqm4qRaB/+wSzCectM0swcedLXgsj0YOeP8hQS/U0eWpRI5Z7rMECSIfl6GjcLQpYdp3tfWCCzy2PMfPnkLHo8w==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SDK_PAY_FLAG:
                {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000"))
                    {
                        if (isPage.equals("now"))
                        {
//                            // 单商品结算
//                            Intent intent = new Intent();
//                            intent.putExtra("result", "true");
//                            WepayActivity.this.setResult(RESULT_OK, intent);
//                            AnimUtil.animBackSlideFinish(mActivity);
                        } else
                        {
                            String shopcarids = sp.getString("shopcarids", "");
                            RequestParams rp = new RequestParams();
                            String redMoney = fragment.redMoney;

                            rp.put("order_sn", outTradeNo);

                            int start1 = resultInfo.indexOf("&total_fee=")
                                    + "&total_fee=".length();
                            int end1 = resultInfo.indexOf("&notify_url=");
                            String totalFee = resultInfo.substring(start1, end1);
                            rp.put("total_fees", totalFee);
                            rp.put("hongbao",
                                    null == redMoney ? "0" : redMoney.substring(1));
                            rp.put("pay_type", "1");
                            rp.put("shopcarid", shopcarids);

//                            Intent intent = new Intent();
//                            intent.putExtra ("result", "true");
//                            setResult (Activity.RESULT_OK,
//                                    intent);
//                            AnimUtil.animBackSlideFinish (mActivity);
                        }
                        dialogProgress (activity, "请稍候");
                        checkOrder ();
                       /* // 单商品结算
                        Intent intent = new Intent();
                        intent.putExtra("result", "true");
                        WepayActivity.this.setResult(RESULT_OK, intent);
                        AnimUtil.animBackSlideFinish(mActivity);*/


                    } else
                    {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000"))
                        {
                            if (isPage.equals("now"))
                            {
                                Intent intent = new Intent();
                                intent.putExtra("result", "false");
                                WepayActivity.this.setResult(RESULT_OK, intent);
                                AnimUtil.animBackSlideFinish(activity);
                            } else
                            {
                                Tools.toast(activity, "支付结果确认中");
                            }

                        } else
                        {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            if (isPage.equals("now"))
                            {
                                Intent intent = new Intent();
                                intent.putExtra("result", "false");
                                WepayActivity.this.setResult(RESULT_OK, intent);
                                AnimUtil.animBackSlideFinish(activity);

                            } else
                            {
                                Tools.toast(activity, "支付失败");
                            }

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG:
                {
                    Tools.toast(activity, "检查结果为：" + msg.obj);
                    break;
                }
            }
        }
    };
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

        L.l ("进入查询订单支付状态");
        RequestParams rp = new RequestParams();
        //todo order_sn
        rp.put ("order_sn", outTradeNo);
        L.l ("outTradeNo" + outTradeNo);
        rp.put ("phone_code", Tools.getUDID (activity));
      //  rp.put ("phone_code","123");
        L.l("phone_code==" + Tools.getUDID(activity));
        HttpHelper.sendByPost (MyConstant.FindorderStatus,rp,
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
                        L.l ("result=="+result);
                        try
                        {
                            JSONObject jsonResult = new JSONObject(result);
                            result = jsonResult.getJSONObject("datas")
                                    .getString("paystatus");
                            L.l ("订单返回=="+result);
                            if (result.equals("success"))
                            {
                                dialogDismiss();
                                Intent intent = new Intent();
                                intent.putExtra("result", "true");
                                WepayActivity.this.setResult (RESULT_OK, intent);
                                AnimUtil.animBackSlideFinish (activity);
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

    public String memberId;
    public String address;
    public String consigner;
    public String conmobile;
    public String conremark;
    public String storeIds;
    public String shopcarids;
    public String goodsnums;
    public float totalFee;
    private String isPage;
    public int fareInt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        ViewUtils.inject(this);
        tint();
        dialogInit ();
        initActionbar();
        setTitles("支付");

        sp = getSharedPreferences("lbsq",
                Context.MODE_PRIVATE);
//        outTradeNo = sp.getString("order_sn", "");
        isPage = getIntent().getStringExtra("isPage");
        FragmentManager fm = getSupportFragmentManager();

        if (isPage.equals("ExternalFragment"))
        {
            initOrderData();
            fragment = PayBaseFragment.newInstance(1);
            fm.beginTransaction().replace(R.id.aipay_page, fragment).commit();
        } else if (isPage.equals("WeChatFragment"))
        {
            initOrderData();
            wxFragment = PayBaseFragment.newInstance(2);
            fm.beginTransaction().replace(R.id.aipay_page, wxFragment)
                    .commit();
        } else if (isPage.equals("now"))
        {
            String totalFee = getIntent().getStringExtra("totalFee");
            String goodName = getIntent().getStringExtra("goodName");
            String goodTetail = getIntent().getStringExtra("goodTetail");
            String orderInfo = getOrderInfo(goodName, goodTetail, totalFee, outTradeNo);
            // 对订单做RSA 签名
            String sign = sign(orderInfo);
            try
            {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            // 完整的符合支付宝参数规范的订单信息
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                    + getSignType();

            Runnable payRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(WepayActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();

        }
    }

    public void initOrderData()
    {
        Intent intent = getIntent();
        memberId = intent.getStringExtra("member_id");
        address = intent.getStringExtra("con_address");
        consigner = intent.getStringExtra("consigner");
        conmobile = intent.getStringExtra("conmobile");
        conremark = intent.getStringExtra("conremark");
        storeIds = intent.getStringExtra("store_ids");
        shopcarids = intent.getStringExtra("shopcarids");
        goodsnums = intent.getStringExtra("goodsnums");
        totalFee = Float.valueOf(intent.getStringExtra("total_fee"));
        fareInt = intent.getIntExtra("fareInt", 0);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */

    public void pay()
    {
        TextView shMoney = fragment.shMoney;
        final TextView goodDescribe = fragment.goodDescribe;
        TextView goodName = fragment.goodName;
        String totalFees = shMoney.getText().toString().trim();
        // 支付金额
        String result = totalFees.substring(1);
        String describe = goodDescribe.getText().toString();
        if (Tools.isEmpty(describe))
        {

            Tools.dialog(this, "提交订单前,请简单描述一下您购买的商品做为支付包支付凭证", true, new onButtonClick()
            {
                @Override
                public void buttonClick()
                {
                    goodDescribe.setFocusable(true);
                    dialog.dismiss();
                }
            });
        } else
        {
            String orderInfo = getOrderInfo(goodName.getText().toString()
                            .trim(), goodDescribe.getText().toString().trim(),
                    result, outTradeNo);

            // 对订单做RSA 签名
            String sign = sign(orderInfo);
            try
            {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            // 完整的符合支付宝参数规范的订单信息
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                    + getSignType();

            Runnable payRunnable = new Runnable()
            {

                @Override
                public void run()
                {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(WepayActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }

    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price, String outTradeNo)
    {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
      //  orderInfo += "&total_fee=" + "\"" + "0.01" + "\"";
          orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + MyConstant.WX_PAY_NOTIFY_URL
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content)
    {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType()
    {
        return "sign_type=\"RSA\"";
    }

    public void createOrder(String couponId, int amount, RequestCallBack<String> callBack)
    {
        RequestParams rp = new RequestParams();

        rp.put("member_id", memberId);
        rp.put("con_address", address);
        rp.put("consigner", consigner);
        rp.put("conmobile", conmobile);
        rp.put("conremark", conremark);
        rp.put("store_ids", storeIds);
        rp.put("store_id", storeIds);
        rp.put("shopcarids", shopcarids);
        rp.put("goodsnums", goodsnums);

        rp.put("total_fee", String.valueOf(totalFee));
        rp.put("cost_score", "0");
        rp.put("couponid", couponId);

        // TODO 生产环境需要访问正式地址接口
//        HttpHelper.sendByPost("http://applingbushequ.nat123.net/lingbushequ/src/lso2o/mobile/api.php?commend=addshopcarorder", rp, callBack);
        HttpHelper.postByCommand("addshopcarorder", rp, callBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == MyConstant.REQUEST_COUPON)
        {
            if (fragment == null)
            {
                wxFragment.setCoupon(data.getStringExtra("id"), data.getStringExtra("amount"));
            } else
            {
                fragment.setCoupon(data.getStringExtra("id"), data.getStringExtra("amount"));
            }
        }
    }
}
