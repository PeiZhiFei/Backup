package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.GoodMore;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.view.RatingBarView;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.view.smartimage.SmartImageView;

public class JudgeActivity extends BaseAcitivity
{
    @ViewInject (R.id.ratingbar)
    RatingBarView ratingBar;
    @ViewInject (R.id.complete)
    Button complete;
    @ViewInject (R.id.edittext)
    EditText editText;
    @ViewInject (R.id.shop_name)
    TextView shopname;
    @ViewInject (R.id.good_name)
    TextView goodname;
    @ViewInject (R.id.good_img)
    SmartImageView image;

    String store_id;
    String store_name;
    String goods_id;
    String goodnames;
    String images;


    String judgeUrl = MyConstant.URLJUDGE;
    private String ordersn;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_judge);
        ViewUtils.inject (this);
        tint ();
        initActionbar ();
        setTitles (Tools.getStr (this, R.string.TMYJUDGE));
        dialogInit();
        Intent intent = getIntent ();
        type = intent.getStringExtra ("type");
        if (type.equals ("order"))
        {
            store_name = intent.getStringExtra ("ordershop");
            ordersn = intent.getStringExtra ("ordersn");
            images = intent.getStringExtra ("orderimage");
            goodname.setText ("订单号：" + ordersn);
        } else if (type.equals ("good"))
        {
            GoodMore goodMoreEntity = intent.getParcelableExtra ("judge");
            store_name = goodMoreEntity.getStoreName ();
            goodnames = goodMoreEntity.getGoodsName ();
            images = goodMoreEntity.getGoodsPic ();
            goods_id = goodMoreEntity.getGoodsId ();
            store_id = goodMoreEntity.getStoreId ();
            goodname.setText (goodnames);
        }
        shopname.setText (store_name);
        image.setImageUrl (images);
        complete.setOnClickListener (new OnClickListener ()
        {

            @Override
            public void onClick(View v)
            {
                String content = editText.getText ().toString ();
                if ((content != null) && (!content.equals ("")))
                {
                    if (ratingBar.getStartCount () > 0)
                    {
                        comment (content, ratingBar.getStartCount ());
                    } else
                    {
                        Tools.toast (activity,
                                     Tools.getStr (activity, R.string.PLEASEJUDGE));
                    }
                } else
                {
                    Tools.toast (activity,
                                 Tools.getStr (activity, R.string.PLEASEWRITE));
                }
            }

        });

    }

    private void comment(String content, int count)
    {
       dialogProgress (activity,
                                Tools.getStr (activity, R.string.JUDGECOMMIT));
        RequestParams rp = new RequestParams ();
        String url = null;
        if (type.equals ("good"))
        {
            url = judgeUrl;
            rp.addQueryStringParameter ("store_id", store_id);
            rp.addQueryStringParameter ("store_name", store_name);
            rp.addQueryStringParameter ("goods_id", goods_id);
            rp.addQueryStringParameter ("comment", content);
            rp.addQueryStringParameter ("flower_num", "" + count);
            rp.addQueryStringParameter (
                    "member_name",
                    getSharedPreferences ("lbsq", Context.MODE_PRIVATE).getString (
                            MyConstant.KEY_MEMBERPHONE, ""));
            rp.addQueryStringParameter (
                    "member_id",
                    getSharedPreferences ("lbsq", Context.MODE_PRIVATE).getString (
                            MyConstant.KEY_MEMBERID, ""));
        } else if (type.equals ("order"))
        {
            //  todo          url="http://app.lingbushequ.com/mobile/api_order.php?commend=order_addcomment";
            url = "http://app.lingbushequ.com/mobile/api_order.php?commend=order_addcomment";
            rp.addQueryStringParameter ("comment", content);
            rp.addQueryStringParameter ("flower_num", "" + count);
            rp.addQueryStringParameter ("order_sn", "" + ordersn);
        }


        HttpUtils httpUtils = new HttpUtils ();
        httpUtils.send (HttpMethod.POST, url, rp,
                        new RequestCallBack<String> ()
                        {

                            @Override
                            public void onFailure(HttpException arg0, String arg1)
                            {
                                Tools.toast (activity,
                                             Tools.getStr (activity, R.string.NETWORKERROR));
                               dialogDismiss ();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> resultInfo)
                            {
                                String result = resultInfo.result;
                                Log.e ("log", result);
                                try
                                {
                                    JSONObject jo = new JSONObject (result);
                                    result = jo.getString ("code");
                                    if (result.equals ("200"))
                                    {
                                        if (jo.getString ("datas") != null)
                                        {
                                            Tools.toast (activity, Tools.getStr (activity,
                                                                                 R.string.JUDGESUCCESS));
                                            if (type.equals ("order"))
                                            {
                                                Intent intent = new Intent ();
                                                intent.putExtra ("success", true);
                                                setResult (8, intent);
                                            }
                                            AnimUtil.animBackSlideFinish(activity);
                                        }
                                    } else
                                    {
                                        Tools.toast (activity, Tools.getStr (activity,
                                                                             R.string.JUDGEFAIL));
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace ();
                                   dialogDismiss ();
                                }
                               dialogDismiss ();
                            }
                        });
    }
}
