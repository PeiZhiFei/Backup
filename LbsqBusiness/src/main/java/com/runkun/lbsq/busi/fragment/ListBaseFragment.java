package com.runkun.lbsq.busi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.L;

/**
 * 这是一个通用的分页的Listview
 */
public abstract class ListBaseFragment<T> extends BaseFragment
{
    protected ListView listView;
    protected TextView emptyTextView;
    protected ProgressBar footerPB;
    protected TextView footerTV;
    protected View footer;
    protected View view;

    protected boolean first = true;
    protected int pageSize = 10;
    protected int pageNum = 1;
    protected List<T> data = new ArrayList<>();

    //需要子类补全的参数
    protected RequestParams rp = new RequestParams();
    protected String url;
    protected BaseAdapter adapter;
    protected Class<T> cls;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_base, null);
        dialogInit();
        initView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (view != footer)
                {
                    itemClick(i);
//                    Order order = orders.get(i);
//                    Intent intent = new Intent(fragment, OrderInfoActivity.class);
//                    intent.putExtra("order", order.getOrder_sn());
//                    startActivity(intent);
//                    AnimUtil.animTo((Activity) fragment);
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
                {
                    if (view.getLastVisiblePosition() == view.getCount() - 1)
                    {
                        //小细节
                        if (listView.getTag().equals("true"))
                        {
                            loadData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount)
            {
            }
        });
        initType();
        listView.setAdapter(adapter);
        loadData();
        return view;
    }

    protected abstract void itemClick(int position);

    //子类设置rp，list，adapter,class实体类
    protected abstract void initType();

    protected void initView()
    {
        listView = (ListView) view.findViewById(R.id.list);
        Tools tools = new Tools();
        View view2 = tools.getEmptyView(fragment, 0);
        ((ViewGroup) listView.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        emptyTextView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //todo emptyview的点击事件
                loadData();
            }
        });
        listView.setEmptyView(view2);
        footer = LayoutInflater.from(fragment).inflate(
                R.layout.listview_footer, null);
        footerTV = (TextView) footer.findViewById(R.id.footer_tv);
        footerPB = (ProgressBar) footer.findViewById(R.id.footer_pb);
        listView.addFooterView(footer);
    }

    private void loadData()
    {
        rp.addQueryStringParameter("pagenumber", String.valueOf(pageNum++));
        rp.addQueryStringParameter ("pagesize", String.valueOf (pageSize));

        HttpUtils httpUtils = new HttpUtils(30000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onStart()
                    {
                        super.onStart();
                        if (first)
                        {
                            dialogProgress("加载中");
                        }
                        updateFooter(true, "加载中");
                        emptyTextView.setText("加载中");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        pageNum--;
                        if (first)
                        {
                            dialogDismiss();
                        }
                        updateFooter(false, "网络开小差了");
                        emptyTextView.setText("网络出错，点击重试");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        try
                        {
                            JSONObject jsonResult = new JSONObject(result);
                            L.l (result);
                            if (Tools.isSuccess(jsonResult))
                            {
                                JSONArray ja = jsonResult.getJSONArray("datas");
                                if (ja.length() > 0)
                                {
                                    String haveMore = jsonResult
                                            .getString("haveMore");
                                    listView.setTag(haveMore);
                                    data.addAll(JSON.parseArray(jsonResult.getString("datas"), cls));
                                    adapter.notifyDataSetChanged();
                                    if ("false".equals(haveMore))
                                    {
                                        updateFooter(false, "已加载全部");
                                    }
                                } else
                                {
                                    //listview为空就没有footer
                                    emptyTextView.setText("暂无信息");
                                }
                            } else
                            {
                                //这里其实就发生异常了404,400,listview为空就没有footer
                                emptyTextView.setText("数据解析异常");
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            updateFooter(false, "数据解析异常");
                            emptyTextView.setText("数据解析异常");
                            if (first)
                            {
                                dialogDismiss();
                            }
                        }
                        if (first)
                        {
                            dialogDismiss();
                        }
                        first = false;
                    }

                });
    }

    //更新footer状态
    protected void updateFooter(boolean loading, String info)
    {
        footerPB.setVisibility(loading ? View.VISIBLE : View.GONE);
        footerTV.setText(info);
    }

//    //footer的点击加载事件，现在用不到
//    @OnClick({R.id.footer_tv})
//    protected void onClick(View view)
//    {
//        if (view.getId() == R.id.footer_tv && listView.getTag() != null
//                && listView.getTag().equals("true"))
//        {
//            loadData();
//        }
//    }

}
