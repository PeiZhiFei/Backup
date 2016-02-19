package com.runkun.lbsq.busi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
public abstract class SimpleListFragmentTest<T> extends BaseFragment
{
    protected ListView listView;
    protected TextView emptyTextView;
    protected View view;
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
                {
                    itemClick(i);
                }
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
    }

    private void loadData()
    {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onStart()
                    {
                        super.onStart();
                        dialogProgress("加载中");
                        emptyTextView.setText("加载中");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        dialogDismiss();
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
                                    //通用list可以直接这么写
                                    data.addAll(JSON.parseArray(jsonResult.getString("datas"), cls));
                                    adapter.notifyDataSetChanged();
                                } else
                                {
                                    emptyTextView.setText("暂无信息");
                                }
                            } else
                            {
                                emptyTextView.setText("数据解析异常");
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            emptyTextView.setText("数据解析异常");
                        } finally
                        {
                            dialogDismiss();
                        }

                    }

                });
    }

    protected abstract void makeData(JSONArray jsonArray);


}
