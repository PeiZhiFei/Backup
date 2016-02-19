package com.runkun.lbsq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.activity.ShopListActivity;
import com.runkun.lbsq.adapter.ShopAdapter;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;
import feifei.project.view.MyListView;

public class ShopListBaseFragment extends BaseFragment
{
    protected ShopListActivity shopListActivity;
    Shop shop;
    protected ShopAdapter adapter;
    // protected TestAdapter adapter;
    protected MyListView shopList;
    protected TextView emptyTextView;
    protected HttpUtils hu = new HttpUtils();
    protected List<Shop> listData = new ArrayList<Shop>();
    protected double lo;
    protected double la;
    protected View view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.shop = shopListActivity.shop;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        shopListActivity = (ShopListActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_shoplist, container, false);
        dialogInit();

        shopList = (MyListView) view.findViewById(R.id.shop_list);
        Tools tools = new Tools();
        View view2 = tools.getEmptyView(getActivity(), 0);
        ((ViewGroup) shopList.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        emptyTextView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (!sel)
//                {
//                    reloadPosition(null, false);
//                } else
//                {
                shopListActivity.query();
//                }


            }
        });
        shopList.setEmptyView(view2);
        shopList.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Shop shop = listData.get(position);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("shop", shop);
                ConfigUtil.write (getActivity (), MyConstant.KEY_GUIDE_FIRST,
                        false);
                getActivity().startActivity(intent);
                AnimUtil.animToSlideFinish(getActivity());
            }
        });
        adapter = new ShopAdapter(getActivity(), listData, R.layout.item_shop,
                true, true);
        shopList.setAdapter(adapter);
        return view;
    }

    public void reqShopList(RequestParams rp, final boolean select)
    {
        dialogProgress( Tools.getStr(fragment, R.string.LOADING));
        hu.send(HttpMethod.POST, MyConstant.URLSTORELIST, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        L.l (result);
                        listData.clear();
                        try
                        {
                            JSONObject jsonReult = new JSONObject(result);
                            listData.addAll(JSON.parseArray(jsonReult.getString("datas"), Shop.class));
                            adapter.notifyDataSetChanged();
                            emptyTextView.setText(select ? "该地区暂未开通服务哦\n点击切换" : "暂无超市");
                            sel = select;
                        } catch (JSONException e)
                        {
                            emptyTextView.setText("请手动选择吧");
                            Tools.toast(getActivity(),
                                    Tools.getStr(fragment, R.string.JSONERROR));
                            e.printStackTrace();
                            dialogDismiss();
                        }
                        dialogDismiss();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg)
                    {
                        if (getActivity() != null)
                        {
//                            emptyTextView.setText(Tools.getStr(fragment,
//                                    R.string.NETERRORCLICK));
                            emptyTextView.setText("网络开小差了，请手动选择吧");
                            dialogDismiss();
                            Tools.toast(shopListActivity, Tools.getStr(
                                    fragment, R.string.NETWORKERROR));
                        }
                    }
                });
    }

    boolean sel = false;

    // select:is again?
    protected void reloadPosition(String s, boolean select)
    {
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("lon", String.valueOf(lo));
        rp.addQueryStringParameter("lat", String.valueOf(la));
        if (select)
        {
            rp.addQueryStringParameter("mall_id", s);
        }
        emptyTextView.setText(Tools.getStr(fragment, R.string.EMPTYLOADING));
        listData.clear();
        adapter.notifyDataSetChanged();
        reqShopList(rp, select);
    }

    String tempString;

    public void reloads(String s)
    {
        tempString = s;
        reloadPosition(s, true);
    }
}
