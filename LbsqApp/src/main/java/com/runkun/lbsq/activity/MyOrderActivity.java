package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.adapter.OrderAdapter;
import com.runkun.lbsq.bean.Order;
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
import feifei.project.view.swipmenu.SwipeMenuListView;

public class MyOrderActivity extends BaseAcitivity
{
    @ViewInject(R.id.list)
    SwipeMenuListView listView;

    @ViewInject(R.id.footer_pb)
    protected ProgressBar footerPB;

    @ViewInject(R.id.footer_tv)
    protected TextView footerTV;

    protected int pageSize = 20;
    protected int pageNum = 1;
    protected TextView emptyTextView;
    protected String memberId;
    boolean more = true;
    OrderAdapter adapter;
    List<Order> orders = new ArrayList<> ();
    View footer;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_order);
        ViewUtils.inject (this);
        initActionbar ();
        setTitles ("我的订单");
        tint ();
        dialogInit();

        memberId = HttpHelper.getPrefParams (activity, "memberId");
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
        listView.addFooterView (footer);
        adapter = new OrderAdapter (this, orders, R.layout.item_order);
        listView.setAdapter (adapter);
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener ()
        {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l)
            {
                Order order = orders.get (i);
                Intent intent = new Intent (activity, OrderInfoActivity.class);
                intent.putExtra ("order", order.getOrder_sn ());
                startActivity(intent);
                AnimUtil.animToSlide(activity);
            }
        });
        listView.setOnScrollListener (new AbsListView.OnScrollListener ()
        {
            @Override
            public void onScrollStateChanged (AbsListView view, int scrollState)
            {
                if ( scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE )
                {
                    if ( view.getLastVisiblePosition () == view.getCount () - 1 )
                    {
                        //小细节
                        if ( more )
                        {
                            loadData ();
                        }
                    }
                }
            }

            @Override
            public void onScroll (AbsListView view, int firstVisibleItem,
                                  int visibleItemCount, int totalItemCount)
            {
            }
        });
        loadData ();
    }


    private void loadData ()
    {
       dialogProgress (activity,
                Tools.getStr (activity, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("member_id", ConfigUtil.readString (
                activity, MyConstant.KEY_MEMBERID, ""));
        rp.addQueryStringParameter ("pagenumber", String.valueOf (pageNum++));
        rp.addQueryStringParameter ("pagesize", String.valueOf (pageSize));

        HttpUtils httpUtils = new HttpUtils ();
        httpUtils.send (HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL_ORDER + "order_list", rp,
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
                        if ( "".equals (result) )
                        {
                            updateFooter (false,
                                    Tools.getStr (activity, R.string.NOGOODS));
                            emptyTextView.setText ("暂无订单");
                        }
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
                                    orders.addAll (JSON.parseArray (jsonResult.getString ("datas"), Order.class));
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
                                    }
                                } else
                                {
                                    more = false;
                                    updateFooter (false, "暂无订单");
                                    emptyTextView.setText ("暂无订单");
                                }
                               dialogDismiss ();
                                return;
                            }
                            updateFooter (false,
                                    Tools.getStr (activity, R.string.JSONERROR));
                            emptyTextView.setText (Tools.getStr (activity,
                                    R.string.JSONERROR));
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

                });
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

    @OnClick({R.id.footer_tv})
    protected void onClick (View view)
    {
        if ( view.getId () == R.id.footer_tv && listView.getTag () != null
                && listView.getTag ().equals ("true") )
        {
            loadData ();
        }
    }


}
