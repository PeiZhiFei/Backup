package com.runkun.lbsq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.adapter.CouponsAdapter;
import com.runkun.lbsq.bean.Coupons;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;
import feifei.project.view.ClearEditText;

/**
 *
 * 优惠卷Activity
 */
public class MyCouponsActivity extends BaseAcitivity implements View.OnClickListener
{
    ListView listView;

    @ViewInject(R.id.footer_pb)
    protected ProgressBar footerPB;

    @ViewInject(R.id.footer_tv)
    protected TextView footerTV;

    @ViewInject(R.id.exchange_value)
    private ClearEditText conpass;

    @ViewInject(R.id.exchange)
    private Button button;

    @ViewInject(R.id.actionbar_right2)
    private Button infomation;

    protected int pageSize = 10;
    protected int pageNum = 1;
    protected TextView emptyTextView;
    protected String memberId;
    private boolean more = true;
    private CouponsAdapter adapter;
    private List<Coupons> orders = new ArrayList<> ();
    private View footer;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons);
        ViewUtils.inject(this);
        initActionbar();
        setTitles("我的优惠券");
        tint();
        dialogInit();

        actionbar_right2.setVisibility(View.VISIBLE);
        actionbar_right2.setBackgroundResource(0);
        actionbar_right2.setText("使用说明");
        actionbar_right2.setTextSize(12);
        actionbar_right2.setTextColor(Color.YELLOW);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.rightMargin=Tools.dp2px(activity,15);
        actionbar_right2.setLayoutParams(layoutParams);
        actionbar_right2.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        actionbar_right2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(activity,InformationActivity.class));
            }
        });
        memberId = HttpHelper.getPrefParams (activity, MyConstant.KEY_MEMBERID);

        listView = (ListView) findViewById (R.id.list);
        Tools tools = new Tools ();
        View view2 = tools.getEmptyView (activity, 0);
        ((ViewGroup) listView.getParent ()).addView (view2);
        emptyTextView = tools.getEmptyText ();
        emptyTextView.setOnClickListener (new View.OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                loadData ();
            }
        });
        listView.setEmptyView (view2);

        footer = LayoutInflater.from (activity).inflate (
                R.layout.listview_footer, null);
        footerTV = (TextView) footer.findViewById (R.id.footer_tv);
        footerPB = (ProgressBar) footer.findViewById (R.id.footer_pb);
        footerTV.setOnClickListener (this);
        listView.addFooterView (footer);
        button.setOnClickListener (this);

        adapter = new CouponsAdapter(activity, orders, R.layout.item_coupons);
        listView.setAdapter (adapter);

        loadData ();
    }


    private void loadData ()
    {
       dialogProgress (activity,
                Tools.getStr (activity, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("member_id", ConfigUtil.readString (this, MyConstant.KEY_MEMBERID, ""));
        rp.addQueryStringParameter ("pagenumber", String.valueOf (pageNum++));
        rp.addQueryStringParameter ("pagesize", String.valueOf (pageSize));
        HttpUtils httpUtils = new HttpUtils ();
        httpUtils.send (HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL_COUPON + "member_coupons_list", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure (HttpException arg0, String arg1)
                    {
                        pageNum--;
                        more = false;
                        updateFooter (false,
                                Tools.getStr (activity, R.string.NETERRORCLICK));
                       dialogDismiss ();
                        emptyTextView.setText (Tools.getStr (activity,
                                R.string.NETERRORCLICK));
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        L.l (result);
                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            L.l (result);
                            if ( HttpHelper.isSuccess (jsonResult) )
                            {
                                JSONArray ja = jsonResult.getJSONArray ("datas");
                                if ( ja.length () > 0 )
                                {
                                    String haveMore = jsonResult
                                            .getString ("haveMore");
                                    listView.setTag (haveMore);
                                    orders.addAll (JSON.parseObject (jsonResult.getString ("datas"), new TypeReference<List<Coupons>> ()
                                    {
                                    }));
                                    adapter.notifyDataSetChanged ();
                                    if ( "true".equals (haveMore) )
                                    {
                                        updateFooter (false, Tools.getStr (
                                                activity,
                                                R.string.CLICKLOADINGMORE));
                                    } else
                                    {
                                        more = false;
                                        updateFooter (false, Tools.getStr (
                                                activity, R.string.ALLLOADED));
                                        emptyTextView.setText (Tools.getStr (
                                                activity, R.string.ALLLOADED));
                                    }
                                } else
                                {
                                    more = false;
                                    updateFooter (false, "暂无优惠券");
                                    emptyTextView.setText ("暂无优惠券");
                                }
                               dialogDismiss ();
                                return;
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                            updateFooter (false,
                                    Tools.getStr (activity, R.string.JSONERROR));
                            emptyTextView.setText (Tools.getStr (activity,
                                    R.string.JSONERROR));
                           dialogDismiss ();
                        }
                       dialogDismiss ();
                    }
                }

        );
    }

    protected void updateFooter (boolean loading, String info)
    {
        if ( loading )
        {
            footerTV.setVisibility (View.GONE);
            footerPB.setVisibility (View.VISIBLE);
        } else
        {
            footerPB.setVisibility (View.GONE);
            footerTV.setVisibility (View.VISIBLE);
            footerTV.setText (info);
        }
    }

    public void onClick (View view)
    {
        switch (view.getId ())
        {
            case R.id.footer_tv:
                if ( listView.getTag () != null
                        && listView.getTag ().equals ("true") )
                {
                    loadData ();
                }
                break;
            case R.id.exchange:
                String exchageValue = conpass.getText ().toString ().trim ();
                if ( Tools.isEmpty (exchageValue) )
                {
                    Tools.toast (this, "请输入优惠码");
                    AnimUtil.animShakeText (conpass);
                } else
                {
                    RequestParams rp1 = new RequestParams ();
                    rp1.addQueryStringParameter ("member_id", ConfigUtil.readString (
                            activity, MyConstant.KEY_MEMBERID, ""));
                    rp1.addQueryStringParameter ("coupon_sn", exchageValue);
                    requestExchage (rp1);
                }
                break;
        }
    }

    //请求兑换码
    public void requestExchage (RequestParams requestParams)
    {
       dialogProgress (this,
                "正在处理请稍候");
        HttpUtils hu = new HttpUtils ();
        hu.send (HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL_COUPON + "use_coupons_sn", requestParams,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure (HttpException paramHttpException,
                                           String paramString)
                    {
                        Tools.toast (getApplication (),
                                "请求处理失败");
                       dialogDismiss ();
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> paramResponseInfo)
                    {
                        String localObject = paramResponseInfo.result;
                        L.l (localObject);
                        try
                        {
                            JSONObject resultJson = new JSONObject (localObject);
                            String result = resultJson.getString ("code");
                            if ( "200".equals (result) )
                            {
                                String r = resultJson.getString ("datas");
                                if ( r.equals ("202") )
                                {
                                    Tools.toast (activity, "下一轮再来吧");
                                } else if ( r.equals ("203") )
                                {
                                    Tools.toast (activity, "优惠码已过期");
                                } else if ( r.equals ("204") )
                                {
                                    Tools.toast (activity, "优惠码已领过了");
                                } else if ( r.equals ("201") )
                                {
                                    Tools.toast (activity, "领取失败");
                                } else if ( r.equals ("200") )
                                {
                                    Tools.toast (activity, "领取成功");
                                    orders.clear ();
                                    adapter.notifyDataSetChanged ();
                                    emptyTextView.setText ("加载中……");
                                    updateFooter (true, "加载中……");
                                    pageNum = 1;
                                    loadData ();
                                }

                            }
                        } catch (JSONException e)
                        {
                            Tools.toast (getApplication (),
                                    "数据错误");
                            e.printStackTrace ();
                           dialogDismiss ();
                        }
                       dialogDismiss ();
                    }
                });
    }

}
